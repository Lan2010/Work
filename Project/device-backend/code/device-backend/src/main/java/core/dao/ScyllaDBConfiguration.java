package core.dao;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import core.common.Constant;
import core.util.CommonUtils;

/**
 * ScyllaDB配置
 * @author dev-teng
 * @date 2018年6月27日
 */
@Component
public class ScyllaDBConfiguration {
	private String url;
	private String port;
	private String keyspace;
	private String username;
	private String password;
	
	@PostConstruct
	public void init(){
		this.url = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH,"cassandra.org.url");
		this.port = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH,"cassandra.org.port");
		this.keyspace = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH,"cassandra.org.keyspace");
		this.username = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH,"cassandra.org.username");
		this.password = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH,"cassandra.org.password");
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getKeyspace() {
		return keyspace;
	}
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
