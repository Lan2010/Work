package com.tianzhixing.devicecomm.dao;

import java.util.Map;

public interface DevOnlineLogDao {
	/**
	 * 插入设备上下线日志
	 * @param map
	 */
	void insertLog(Map<String,Object> map);
	
}
