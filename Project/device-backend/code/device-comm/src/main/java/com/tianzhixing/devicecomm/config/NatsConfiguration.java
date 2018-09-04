package com.tianzhixing.devicecomm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



/**
 * 读取yml，获取配置参数
 * @author dev-teng
 * @date 2018年7月23日
 */
@Component
@ConfigurationProperties(prefix = "io.nats.client")
public class NatsConfiguration {
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}

