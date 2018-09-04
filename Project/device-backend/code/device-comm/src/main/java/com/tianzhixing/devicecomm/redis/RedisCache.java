package com.tianzhixing.devicecomm.redis;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tianzhixing.devicecomm.config.RedisConfiguration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis API 相关操作
 * @author seamar
 * 
 */
@Component
public class RedisCache {
	
	private static Logger log = LoggerFactory.getLogger(RedisCache.class);
	
	@Autowired
	private RedisConfiguration redisConfiguration;

	private static JedisPool jedisPool = null;

	/**
	 * 创建jedis池，不存在则创建新的
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	@PostConstruct
	public void  initRedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(redisConfiguration.getMaxTotal()));
		config.setMaxIdle(Integer.valueOf(redisConfiguration.getMaxIdle()));
		config.setMaxWaitMillis(Integer.valueOf(redisConfiguration.getMaxWaitMillis()));
		config.setTestOnBorrow(Boolean.valueOf(redisConfiguration.getTestOnBorrow()));
		config.setTestOnReturn(true);
		config.setBlockWhenExhausted(false);
		if (!StringUtils.isEmpty(redisConfiguration.getPassword())) {
			jedisPool = new JedisPool(config, redisConfiguration.getIp(), 
					Integer.valueOf(redisConfiguration.getProt()), 
					Integer.valueOf(redisConfiguration.getTimeOut()), 
					redisConfiguration.getPassword());
		} else {
			jedisPool = new JedisPool(config, redisConfiguration.getIp(), 
					Integer.valueOf(redisConfiguration.getProt()), 
					Integer.valueOf(redisConfiguration.getTimeOut()));
		}
	}

	public synchronized Jedis getJedis() {
		Jedis jedis = null;
		try {
			if (jedisPool != null) {//
				jedis = jedisPool.getResource();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
		}
		return jedis;
	}

	private  void closeJedis(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public  void set(String key, String value, Integer expire) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				jedis.set(key, value);
				if (expire != null) {
					jedis.expire(key, expire);
				}
			} else {
				log.error("Jedis is null");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			closeJedis(jedis);
		}
	}

	public  void updateExpire(String key, Integer expire) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null && jedis.get(key) != null) {
				jedis.expire(key, expire);// TODO 设置过期时间永久有效
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 获取字符串类型的key对应value值
	 * 
	 * @param key
	 * @return
	 */
	public  String getString(String key) {
		Jedis jedis = getJedis();
		String value = null;
		try {
			if (jedis != null) {
				value = jedis.get(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			closeJedis(jedis);
		}
		return value;
	}
	
	/**
	 * 是否存在该key
	 * @param key
	 * @return
	 */
	public Boolean isExitKey(String key){
		Jedis jedis = getJedis();
		try {
			if (jedis.exists(key)) {
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			closeJedis(jedis);
		}
		return false;		
	}
	
	
	public void del(String key) {
		Jedis jedis = null;
		try {
			if (jedisPool != null) {
				jedis = jedisPool.getResource();
				log.info("key......"+key);
				if (jedis != null && jedis.get(key) != null) {
					jedis.del(key);// TODO 设置过期时间
					log.info("删除" + key );
				} else {
					log.error("delete redis key failed!");
				}
			}
		} catch (Exception e) {
			log.error("delete redis key failed!", e);
			closeJedis(jedis);
		} finally {
			closeJedis(jedis);
		}
		
	}
}
