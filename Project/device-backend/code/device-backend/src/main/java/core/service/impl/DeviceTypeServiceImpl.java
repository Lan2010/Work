package core.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.mapper.DeviceTypeMapper;
import core.pojo.DeviceType;
import core.service.DeviceTypeService;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService{

	@Resource
	public DeviceTypeMapper deviceTypeMapper;
	
	@Override
	public List<DeviceType> getDeviceType() throws SQLException {
		List<DeviceType> merchants = deviceTypeMapper.getDeviceType();
		return merchants;
	}

	@Override
	public Integer addDeviceType(DeviceType deviceType) throws SQLException {
		Integer counts = deviceTypeMapper.addDeviceType(deviceType);
		return counts;
	}

	@Override
	public DeviceType getCode(String code) throws SQLException {
		DeviceType deviceType = deviceTypeMapper.getCode(code);
		return deviceType;
	}

	@Override
	public void updateDeviceType(DeviceType deviceType) throws SQLException {
		deviceTypeMapper.updateDeviceType(deviceType);
	}

}
