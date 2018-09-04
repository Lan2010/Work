package core.service;

import java.sql.SQLException;

import core.pojo.DeviceOnline;

/**
 * @author dev-jin
 * @date 2018年6月21日
 */
public interface DeviceOnlineService {
	
	public Integer addDeviceOnline(DeviceOnline deviceOnline)throws SQLException;
	
	public void updateDeviceOnline(DeviceOnline deviceOnline) throws SQLException;
}
