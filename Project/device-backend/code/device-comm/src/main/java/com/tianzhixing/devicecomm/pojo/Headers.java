package com.tianzhixing.devicecomm.pojo;

/**
 * 订阅的Message 的headers字段内容
 * @author dev-teng
 * @date 2018年6月27日
 */
public class Headers {
	private String mqtt_receivedRetained;
	private String id;
	private Boolean mqtt_duplicate;
	private String mqtt_receivedTopic;//消息主题
	private Short mqtt_receivedQos;
	private Long timestamp;
	public String getMqtt_receivedRetained() {
		return mqtt_receivedRetained;
	}
	public void setMqtt_receivedRetained(String mqtt_receivedRetained) {
		this.mqtt_receivedRetained = mqtt_receivedRetained;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getMqtt_duplicate() {
		return mqtt_duplicate;
	}
	public void setMqtt_duplicate(Boolean mqtt_duplicate) {
		this.mqtt_duplicate = mqtt_duplicate;
	}
	public String getMqtt_receivedTopic() {
		return mqtt_receivedTopic;
	}
	public void setMqtt_receivedTopic(String mqtt_receivedTopic) {
		this.mqtt_receivedTopic = mqtt_receivedTopic;
	}
	public Short getMqtt_receivedQos() {
		return mqtt_receivedQos;
	}
	public void setMqtt_receivedQos(Short mqtt_receivedQos) {
		this.mqtt_receivedQos = mqtt_receivedQos;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
