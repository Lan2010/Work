package core.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import core.dao.DevOnlineLogDao;
import core.mapper.DeviceMapper;
import core.service.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService {
	@Resource
	private DeviceMapper deviceMapper;
	@Resource
	private DevOnlineLogDao devOnlineLogDao;

	@Override
	public JSONObject countAll() throws SQLException {
		JSONObject entity =  new JSONObject();
		List<Map<String, Object>> onlineList = deviceMapper.countOnlined();
		Integer is_onlined = 0;
		
		for(Map<String, Object> map : onlineList) {
			is_onlined = (Integer) map.get("is_onlined");
			if(is_onlined==0) {
				entity.put("unonlined",  map.get("count"));
			}else if(is_onlined==1) {
				entity.put("onlined",  map.get("count"));
			}
		}
		Integer countAll = deviceMapper.countAll();
		entity.put("total", countAll);
		return entity;
	}

	@Override
	public JSONObject countBind() throws SQLException {
		JSONObject result = new JSONObject();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("online_status", 1);
		Integer today = devOnlineLogDao.countToday(map);
		Integer bind =  deviceMapper.countBind();
		Integer bindOnline =  deviceMapper.countBindOnline();
		result.put("total", bind);
		result.put("online", bindOnline);
		result.put("today", today);
		return result;
	}

	@Override
	public JSONObject countUnbound() throws SQLException {
		JSONObject result = new JSONObject();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("online_status", 0);
		Integer today = devOnlineLogDao.countToday(map);
		Integer unbound =  deviceMapper.countUnbound();
		Integer unboundOnline =  deviceMapper.countUnboundOnline();
		Integer unboundUnonlined =  deviceMapper.countUnboundUnonlined();
		result.put("total", unbound);
		result.put("online", unboundOnline);
		result.put("unonlined", unboundUnonlined);
		result.put("today", today);
		return result;
	}

}
