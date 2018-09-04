package com.tianzhixing.devicecomm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取yml,获取redis的配置信息
 * @author dev-teng
 * @date 2018年7月2日
 */
@Component
@ConfigurationProperties(prefix = "redis.io")
public class RedisConfiguration {
	private String ip;
	private String prot;
	private String password;
	private String maxTotal;
	private String maxIdle;
	private String maxWaitMillis;
	private String timeOut;
	private String testOnBorrow;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getProt() {
		return prot;
	}
	public void setProt(String prot) {
		this.prot = prot;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(String maxTotal) {
		this.maxTotal = maxTotal;
	}
	public String getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}
	public String getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(String maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	public String getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(String testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	
}
