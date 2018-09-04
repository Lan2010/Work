package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.DeviceLog;

/**
 * @author dev-jin
 * @date 2018年6月20日
 */
public interface DeviceLogMapper {

	public Integer addDeviceLog(DeviceLog deviceLog)throws SQLException;
	
	public List<DeviceLog> getDeviceLog(Map<String,Object> map)throws SQLException;
	
	public Integer getDeviceLogCount(Map<String, Object> map)throws SQLException;
}
