package core.service.impl;

import java.sql.SQLException;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.mapper.DeviceOnlineMapper;
import core.pojo.DeviceOnline;
import core.service.DeviceOnlineService;

@Service
public class DeviceOnlineServiceImpl implements DeviceOnlineService{

	@Resource
	public DeviceOnlineMapper deviceOnlineMapper;
	

	@Override
	public Integer addDeviceOnline(DeviceOnline deviceOnline) throws SQLException {
		Integer counts = deviceOnlineMapper.addDeviceOnline(deviceOnline);
		return counts;
	}

	@Override
	public void updateDeviceOnline(DeviceOnline deviceOnline) throws SQLException {
		deviceOnlineMapper.updateDeviceOnline(deviceOnline);
	}

}
