package core.service;

import java.sql.SQLException;
import java.util.List;

import core.pojo.DeviceLog;
import core.pojo.Page;

/**
 * @author dev-jin
 * @date 2018年6月20日
 */
public interface DeviceLogService {
	
	public List<DeviceLog> getDeviceLog(DeviceLog deviceLog,Page page) throws SQLException;
	
	public Integer getDeviceLogCount(DeviceLog deviceLog) throws SQLException;
	
	public Integer addDeviceLog(DeviceLog deviceLog)throws SQLException;
	
}
