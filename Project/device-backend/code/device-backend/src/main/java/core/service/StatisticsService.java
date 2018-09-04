package core.service;

import java.sql.SQLException;

import com.alibaba.fastjson.JSONObject;

public interface StatisticsService {
	/**
	 * 统计所有设备数及其上线、未上线数
	 * @return
	 * @throws SQLException
	 */
	public JSONObject countAll()throws SQLException;
	
	/**
	 * 统计所有绑定的设备数及其当前在线数和今日在线数
	 * @return
	 * @throws SQLException
	 */
	public JSONObject countBind()throws SQLException;
	
	/**
	 * 统计所有未绑定的设备数及其当前在线数、今日在线数、未上线数
	 * @return
	 * @throws SQLException
	 */
	public JSONObject countUnbound()throws SQLException;
	
	
}
