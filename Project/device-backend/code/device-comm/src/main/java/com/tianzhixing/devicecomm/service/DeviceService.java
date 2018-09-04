package com.tianzhixing.devicecomm.service;

import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSONObject;

public interface DeviceService {

	//设备上下线
	void dosign(Message<?> message);

	//设备端回复•设置配置
	void getSetReply(Message<?> message);

	//设备端回复•获取配置
	void getGetReply(Message<?> message);
	
	//设备端回复•安装插件
	void getInstallIpkReply(Message<?> message);

	//设备端回复•移除插件
	void getRemoveIpkReply(Message<?> message);

	//设备端回复•获取插件信息
	void getInfoIpkReply(Message<?> message);

	//设备端回复•系统重启
	void getRebootReply(Message<?> message);

	//设备端回复•执行短脚本
	void getShellReply(Message<?> message);

	//获取设备回复配置
	String getConf(String taskId,String devNum);

	// 设备端回复•更改终端登录密码
	void getSetPasswdReply(Message<?> message);
	
	// 设备端回复•执行短脚本
	void getfirmwareReply(Message<?> message);

	//获取更改设备系统用户密码结果
	Integer getSetPasswdReply(String taskId, String devNum);


}
