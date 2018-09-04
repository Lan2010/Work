package com.tianzhixing.devicecomm.pojo;

import com.alibaba.fastjson.JSON;

public class PubPayload {
	// 推送类型
	private String type;
	// 推送对象
	private String mobile;
	// 标题
	private String title;
	// 内容
	private String content;
	// 数量
	private Integer badge = 1;
	// 铃声
	private String sound = "default";

	public PubPayload() {}
	
	public PubPayload(String type, String mobile, String title, String content, Integer badge, String sound) {
		this.type = type;
		this.mobile = mobile;
		this.title = title;
		this.content = content;
		this.badge = badge;
		this.sound = sound;
	}
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Integer getBadge() {
		return badge;
	}


	public void setBadge(Integer badge) {
		this.badge = badge;
	}


	public String getSound() {
		return sound;
	}


	public void setSound(String sound) {
		this.sound = sound;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

}
