package core.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.mapper.DeviceLogMapper;
import core.pojo.DeviceLog;
import core.pojo.Page;
import core.service.DeviceLogService;

@Service
public class DeviceLogServiceImpl implements DeviceLogService{

	@Resource
	public DeviceLogMapper deviceLogMapper;
	
	@Override
	public Integer addDeviceLog(DeviceLog deviceLog) throws SQLException {
		Integer counts = deviceLogMapper.addDeviceLog(deviceLog);
		return counts;
	}
	
	@Override
	public List<DeviceLog> getDeviceLog(DeviceLog deviceLog, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(deviceLog);
		map.put("size", page.getPageSize());
		map.put("start", page.getStart());
		List<DeviceLog> merchants = deviceLogMapper.getDeviceLog(map);
		return merchants;
	}
	
	@Override
	public Integer getDeviceLogCount(DeviceLog deviceLog) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(deviceLog);
		return deviceLogMapper.getDeviceLogCount(map);
	}
	
	private Map<String, Object> setCondition(DeviceLog deviceLog) {
		if (deviceLog != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("order_num", deviceLog.getOrderNum());
			condition.put("id", deviceLog.getId());
			condition.put("operation", deviceLog.getOperation());
			condition.put("user", deviceLog.getUser());
			condition.put("oper_time", deviceLog.getOperTime());
			condition.put("mac", deviceLog.getMac());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}
	
}
