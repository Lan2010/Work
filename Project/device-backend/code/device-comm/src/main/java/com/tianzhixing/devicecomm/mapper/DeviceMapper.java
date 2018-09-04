package com.tianzhixing.devicecomm.mapper;

import java.sql.SQLException;
import java.util.Map;

import com.tianzhixing.devicecomm.pojo.Device;
import com.tianzhixing.devicecomm.pojo.Task;

public interface DeviceMapper {
//	Device getDevice(Integer devid)throws SQLException;

	int online(Map<String, Object> map) throws SQLException;

	int updateDev(Map<String, Object> map) throws SQLException;

	//开始任务
	int startTask(Task task) throws SQLException;
	
	//插入任务中间表
	int addMiddleTask(Task task)throws SQLException;
	
	//设备回复任务
	int replayTask(Task task) throws SQLException;
	
	//根据DevNum获取设备信息
	Device getDevByNum(String devNum)throws SQLException;

	//插入固件任务中间表
	int addfirmwareMiddleTask(Task task)throws SQLException;

	//插入脚本任务中间表
	int addshellMiddleTask(Task task);





}
