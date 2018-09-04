package core.service;

import java.sql.SQLException;
import java.util.List;


import core.pojo.DeviceType;

/**
 * @author dev-jin
 * @date 2018年6月16日
 */
public interface DeviceTypeService {
	
	public List<DeviceType> getDeviceType() throws SQLException;
	
	public Integer addDeviceType(DeviceType deviceType)throws SQLException;
	
	public DeviceType getCode(String code)throws SQLException;
	
	public void updateDeviceType(DeviceType deviceType) throws SQLException;
}
