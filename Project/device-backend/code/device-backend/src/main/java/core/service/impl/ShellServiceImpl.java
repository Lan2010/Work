package core.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.mapper.DeviceMapper;
import core.mapper.ShellMapper;
import core.pojo.DevTaskDetail;
import core.pojo.Device;
import core.pojo.Page;
import core.pojo.Shell;
import core.pojo.ShellTask;
import core.service.ShellService;
import core.util.CommonUtils;
import core.util.HttpClientUtil;

@Service
public class ShellServiceImpl implements ShellService {
	@Resource
	public ShellMapper shellMapper;
	@Resource
	public DeviceMapper deviceMapper;

	@Override
	public Integer saveShell(Shell shell) throws SQLException {
		return shellMapper.saveShell(shell);

	}

	@Override
	public List<Shell> listShell(Shell shell, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(shell);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return shellMapper.listShell(map);
	}

	@Override
	public Integer selectShellCount(Shell shell) throws SQLException {
		return shellMapper.selectShellCount(shell);
	}

	private Map<String, Object> setCondition(Shell shell) {
		if (shell != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("shellId", shell.getShellId());
			condition.put("shellName", shell.getShellName());
			condition.put("user_id", shell.getUser_id());
			condition.put("user_name", shell.getUser_name());
			condition.put("addTime", shell.getAddTime());
			condition.put("status", shell.getStatus());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public Integer deleteShell(Integer shellId) throws SQLException {
		return shellMapper.deleteShell(shellId);

	}

	@Override
	public Shell selectShellById(Integer shellId) throws SQLException {
		return shellMapper.selectShellById(shellId);

	}

	@Override
	public Integer addTask(ShellTask task) throws SQLException {
		return shellMapper.addTask(task);
	}

	@Override
	public String sendDevTask(ShellTask task, List<?> devnums) throws SQLException {
		String result = null;
		try {
			Shell shell = shellMapper.selectShellById(task.getShellId());
			JSONObject conf = new JSONObject();
			conf.put("dl_url", shell.getShellPath());
			// 任务类型，0--脚本 1--固件
			String token = CommonUtils.getToken();
			String url = Constant.SISCHAIN_URL + "/dev/shell.do?token=" + token + "";
			String notify_url = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH, "io.dev.notify.url")
					+ "shell";
			conf.put("shell_md5", shell.getShell_md5());
			conf.put("notify_url", notify_url);

			JSONObject js = new JSONObject();
			List<String> devNums = new ArrayList<String>();

			for (Object devnum : devnums) {
				if (devnum instanceof Integer) {
					Device dev = deviceMapper.getDeviceById((Integer) devnum);
					devNums.add(dev.getNumber());
				} else {
					devNums.add((String) devnum);
				}
			}

			js.put("devNums", devNums);
			js.put("conf", conf);
			JSONObject postJson = new JSONObject();
			postJson.put("total", devNums.size());
			postJson.put("taskId", task.getTaskId());
			postJson.put("data", js);

			result = HttpClientUtil.doPostJson(url, postJson.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Integer updateCompleteDev() throws SQLException {
		return shellMapper.updateCompleteDev();
	}

	@Override
	public List<ShellTask> listTask(ShellTask task, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(task);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return shellMapper.listTask(map);
	}

	private Map<String, Object> setCondition(ShellTask task) {
		if (task != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("taskName", task.getTaskName());
			condition.put("shellName", task.getShellName());
			condition.put("user_name", task.getUser_name());
			condition.put("addTime", task.getAddTime());
			condition.put("status", task.getStatus());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public Integer selectTaskCount(ShellTask task) throws SQLException {
		return shellMapper.selectTaskCount(task);
	}

	@Override
	public ShellTask queryTaskDetailById(Integer taskId) throws SQLException {
		return shellMapper.queryTaskDetailById(taskId);

	}

	@Override
	public List<Shell> listShellName() throws SQLException {
		return shellMapper.listShellName();

	}

	@Override
	public Integer checkExit(String shellName) throws SQLException {
		return shellMapper.checkExit(shellName);
	}

	@Override
	public List<DevTaskDetail> listDevTaskDetail(Integer taskId, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskId", taskId);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return shellMapper.listDevTaskDetail(map);
	}

	@Override
	public Integer selectDevTaskDetailCount(Integer taskId) throws SQLException {
		return shellMapper.selectDevTaskDetailCount(taskId);
	}
}
