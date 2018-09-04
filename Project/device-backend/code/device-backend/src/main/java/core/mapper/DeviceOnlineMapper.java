package core.mapper;

import java.sql.SQLException;

import core.pojo.DeviceOnline;

/**
 * @author dev-jin
 * @date 2018年6月21日
 */
public interface DeviceOnlineMapper {
	
	public Integer addDeviceOnline(DeviceOnline deviceOnline)throws SQLException;
	
	public void updateDeviceOnline(DeviceOnline deviceOnline)throws SQLException;
}
