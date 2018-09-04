package com.tianzhixing.devicecomm.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface  MqttGateway {
	
	void sendToMqtt(String payload);
	
    void sendToMqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);

	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
	
	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos,  @Header(MqttHeaders.RETAINED) Boolean retain,String payload);


}
