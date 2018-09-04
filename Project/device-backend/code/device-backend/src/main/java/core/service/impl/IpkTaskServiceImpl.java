package core.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import core.mapper.IpkTaskMapper;
import core.pojo.DevTaskDetail;
import core.pojo.IpkTask;
import core.pojo.Page;
import core.service.IpkTaskService;

@Service
public class IpkTaskServiceImpl implements IpkTaskService {
	@Resource
	public IpkTaskMapper ipkTaskMapper;

	private Map<String, Object> setCondition(IpkTask ipkTask) {
		if (ipkTask != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("taskName", ipkTask.getTaskName());
			condition.put("operateType", ipkTask.getOperateType());
			condition.put("ipkName", ipkTask.getIpkName());
			condition.put("ipkNickName", ipkTask.getIpkNickName());
			condition.put("append", ipkTask.getAppend());
			condition.put("addTime", ipkTask.getAddTime());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public List<IpkTask> listIpkTask(IpkTask ipkTask, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(ipkTask);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return ipkTaskMapper.listIpkTask(map);
	}

	@Override
	public Integer selectIpkTaskCount(IpkTask ipkTask) throws SQLException {
		return ipkTaskMapper.selectIpkTaskCount(ipkTask);
	}

	@Override
	public Integer updateCompleteDev() throws SQLException {
		return ipkTaskMapper.updateCompleteDev();
	}

	@Override
	public List<DevTaskDetail> listDevTaskDetail(Integer ipkTaskId, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ipkTaskId", ipkTaskId);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return ipkTaskMapper.listDevTaskDetail(map);
	}

	@Override
	public Integer selectDevTaskDetailCount(Integer ipkTaskId) throws SQLException {
		return ipkTaskMapper.selectDevTaskDetailCount(ipkTaskId);
	}

	@Override
	public IpkTask queryIpkTaskById(Integer ipkTaskId) throws SQLException {
		return ipkTaskMapper.queryIpkTaskById(ipkTaskId);
	}

}
