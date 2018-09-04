package com.tianzhixing.devicecomm.mqtt;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.tianzhixing.devicecomm.common.ssl.SSLClient;
import com.tianzhixing.devicecomm.config.MqttConfiguration;

/**
 * MQTT生产端
 * 
 * @author Administrator
 *
 */
@Configuration
public class MqttOutboundConfiguration {
	@Autowired
	private MqttConfiguration mqttConfiguration;
	@Autowired
	private SSLClient sSLClient;

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		String[] array = mqttConfiguration.getUrl().split(",");
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(array);
		options.setUserName(mqttConfiguration.getUsername());
		options.setPassword(mqttConfiguration.getPassword().toCharArray());
		// 接受离线消息
		options.setCleanSession(false);
		try {
			SSLSocketFactory sSLSocketFactory = sSLClient.getSSLSocketFactory(mqttConfiguration);
			options.setSocketFactory(sSLSocketFactory);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		String[] clientIds = mqttConfiguration.getClientId().split(",");
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
				clientIds[1], mqttClientFactory());
		messageHandler.setAsync(true);
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

}
