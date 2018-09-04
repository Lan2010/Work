package com.tianzhixing.devicecomm.common.nats;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tianzhixing.devicecomm.config.NatsConfiguration;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;

@Component
public class NatsComponent{
	private static Logger log = LoggerFactory.getLogger(NatsComponent.class);
	
	@Autowired
	private NatsConfiguration natsConfiguration;
	
	private ConnectionFactory cf;

	/**
	 * sub消息处理
	 */
	@PostConstruct
	public void handleMessage() {
		cf = new ConnectionFactory(natsConfiguration.getUrl());
	}

	/**
	 * 发布消息，提供给运营平台的数据
	 * @param msg
	 * @param subject
	 */
	public void publish4oms(String subject,String msg) {
		Connection nc = null;
		try {
			nc = cf.createConnection();
			byte[] data = msg.getBytes();// message payload,内容为json格式，转换成字节数组
			nc.publish(subject, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Exception.", e);
		} finally {
			if (!nc.isClosed())
				nc.close();
		}
	}
}
