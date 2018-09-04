package com.tianzhixing.devicecomm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ScyllaDB配置
 * @author dev-teng
 * @date 2018年6月27日
 */
@Component
@ConfigurationProperties(prefix = "cassandra.org")
public class ScyllaDBConfiguration {
	private String url;
	private String port;
	private String keyspace;
	private String username;
	private String password;
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
