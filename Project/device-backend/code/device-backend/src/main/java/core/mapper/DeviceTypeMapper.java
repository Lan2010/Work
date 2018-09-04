package core.mapper;

import java.sql.SQLException;
import java.util.List;


import core.pojo.DeviceType;

/**
 * 充电宝设备的信息Mapper层
 * @author dev-jin
 * @date 2018年6月16日
 */
public interface DeviceTypeMapper {

	public List<DeviceType> getDeviceType()throws SQLException;
	
	public Integer addDeviceType(DeviceType deviceType)throws SQLException;
	
	public DeviceType getCode(String code)throws SQLException;
	
	public void updateDeviceType(DeviceType deviceType)throws SQLException;
}
