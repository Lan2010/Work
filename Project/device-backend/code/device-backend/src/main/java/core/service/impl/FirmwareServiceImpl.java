package core.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.mapper.DeviceMapper;
import core.mapper.FirmwareMapper;
import core.mapper.IpkMapper;
import core.pojo.Condition;
import core.pojo.DevTaskDetail;
import core.pojo.Device;
import core.pojo.Firmware;
import core.pojo.FirmwareName;
import core.pojo.FirmwareTask;
import core.pojo.Page;
import core.service.FirmwareService;
import core.util.CommonUtils;
import core.util.HttpClientUtil;

@Service
public class FirmwareServiceImpl implements FirmwareService {
	@Resource
	public IpkMapper ipkMapper;
	@Resource
	public FirmwareMapper firmwareMapper;
	@Resource
	public DeviceMapper deviceMapper;

	@Override
	public List<FirmwareName> listFirmwareName() throws SQLException {
		return firmwareMapper.listFirmwareName();
	}

	@Override
	public Integer checkExit(Firmware firmware) throws SQLException {
		return firmwareMapper.checkExit(firmware);

	}

	@Override
	public Integer saveFirmware(Firmware firmware) throws SQLException {
		return firmwareMapper.saveFirmware(firmware);

	}

	@Override
	public List<Firmware> listFirmware(Firmware firmware, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(firmware);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return firmwareMapper.listFirmware(map);
	}

	@Override
	public Integer selectFirmwareCount(Firmware firmware) throws SQLException {
		return firmwareMapper.selectFirmwareCount(firmware);
	}

	@Override
	public Firmware selectFirmwareById(Integer firmwareId) throws SQLException {
		return firmwareMapper.selectFirmwareById(firmwareId);
	}

	@Override
	public Integer deleteFirmware(Integer firmwareId) throws SQLException {
		return firmwareMapper.deleteFirmware(firmwareId);
	}

	@Override
	public List<String> queryVersion(Integer firmwareNameId) throws SQLException {
		return firmwareMapper.queryVersion(firmwareNameId);

	}

	@Override
	public List<Device> listDev(Firmware firmware, Page page, Device device) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(firmware, device);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return firmwareMapper.listDev(map);
	}

	@Override
	public Integer selectDevCount(Firmware firmware, Device device) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(firmware, device);
		return firmwareMapper.selectDevCount(map);
	}

	@Override
	public Firmware selectFirmwareByDevNum(Map<String, Object> map) throws SQLException {
		return firmwareMapper.selectFirmwareByDevNum(map);
	}

	@Override
	public Integer addFirmwareTask(FirmwareTask firmwareTask) throws SQLException {
		return firmwareMapper.addFirmwareTask(firmwareTask);

	}

	@Override
	public String sendDevTask(FirmwareTask firmwareTask, List<?> devnums) throws SQLException {
		String result = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("firmwareNameId", firmwareTask.getFirmwareNameId());
			map.put("version", firmwareTask.getVersion());
			String notify_url = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH, "io.dev.notify.url")
					+ "firmware";
			List<String> devNums = new ArrayList<String>();
			JSONObject json = new JSONObject();
			JSONArray ja = new JSONArray();
			for (Object devnum : devnums) {
				JSONObject js = new JSONObject();
				JSONObject conf = new JSONObject();
				Firmware firmware = new Firmware();
				map.put("devNum", devnum);
				if (devnum instanceof String) {
					firmware = firmwareMapper.selectFirmwareByDevNum(map);
					js.put("dev_num", devnum);
				} else if (devnum instanceof Integer) {
					firmware = firmwareMapper.selectFirmwareByDevId(map);
					js.put("dev_num", firmware.getDevNum());
					devNums.add(firmware.getDevNum());
				}
				conf.put("dl_url", firmware.getFirmwarePath());
				conf.put("firmware_md5", firmware.getFirmware_md5());
				conf.put("notify_url", notify_url);
				js.put("conf", conf);
				ja.add(js);
			}
			JSONObject postJson = new JSONObject();
			postJson.put("total", devnums.size());
			postJson.put("firmwareTaskId", firmwareTask.getFirmwareTaskId());
			String token = CommonUtils.getToken();
			postJson.put("data", ja);
			String url = Constant.SISCHAIN_URL + "/dev/firmware.do?token=" + token + "";
			result = HttpClientUtil.doPostJson(url, postJson.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Integer> AllDev(FirmwareTask firmwareTask, Condition condition) throws SQLException {
		List<Integer> devId = new ArrayList<Integer>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("firmwareNameId", firmwareTask.getFirmwareNameId());
			map.put("version", firmwareTask.getVersion());
			// 添加设备条件
			map.put("dev_model", condition.getModel());
			map.put("belongUnitId", condition.getBelongUnitId());
			map.put("isBind", condition.getIsBind());
			map.put("isOnlined", condition.getIsOnlined());
			devId = firmwareMapper.AllDev(map);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return devId;
	}

	@Override
	public Integer updateCompleteDev() throws SQLException {
		return firmwareMapper.updateCompleteDev();
	}

	@Override
	public List<FirmwareTask> listFirmwareTask(FirmwareTask firmwareTask, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(firmwareTask);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return firmwareMapper.listFirmwareTask(map);
	}

	@Override
	public Integer selectFirmwareTaskCount(FirmwareTask firmwareTask) throws SQLException {
		return firmwareMapper.selectFirmwareTaskCount(firmwareTask);
	}

	private Map<String, Object> setCondition(FirmwareTask firmwareTask) {
		if (firmwareTask != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("taskName", firmwareTask.getTaskName());
			condition.put("firmwareName", firmwareTask.getFirmwareName());
			condition.put("nickName", firmwareTask.getNickName());
			condition.put("status", firmwareTask.getStatus());

			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	private Map<String, Object> setCondition(Firmware firmware) {
		if (firmware != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("firmwareId", firmware.getFirmwareId());
			condition.put("firmwareName", firmware.getFirmwareName());
			condition.put("firmwareNameId", firmware.getFirmwareNameId());
			condition.put("model", firmware.getModel());
			condition.put("version", firmware.getVersion());
			condition.put("firmwareDesc", firmware.getFirmwareDesc());
			condition.put("firmwarePath", firmware.getFirmwarePath());
			condition.put("user_id", firmware.getUser_id());
			condition.put("addTime", firmware.getAddTime());
			condition.put("status", firmware.getStatus());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	private Map<String, Object> setCondition(Firmware firmware, Device device) {
		if (firmware != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("firmwareId", firmware.getFirmwareId());
			condition.put("firmwareName", firmware.getFirmwareName());
			condition.put("firmwareNameId", firmware.getFirmwareNameId());
			condition.put("model", firmware.getModel());
			condition.put("version", firmware.getVersion());
			condition.put("firmwareDesc", firmware.getFirmwareDesc());
			condition.put("firmwarePath", firmware.getFirmwarePath());
			condition.put("user_id", firmware.getUser_id());
			condition.put("addTime", firmware.getAddTime());
			condition.put("status", firmware.getStatus());
			// 添加设备条件
			condition.put("dev_model", device.getModel());
			condition.put("belongUnitId", device.getBelongUnitId());
			condition.put("isBind", device.getIsBind());
			condition.put("isOnlined", device.getIsOnlined());

			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public FirmwareTask queryFirmwareTaskById(Integer firmwareTaskId) throws SQLException {
		return firmwareMapper.queryFirmwareTaskById(firmwareTaskId);
	}

	@Override
	public List<DevTaskDetail> listDevTaskDetail(Integer firmwareTaskId, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firmwareTaskId", firmwareTaskId);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return firmwareMapper.listDevTaskDetail(map);
	}

	@Override
	public Integer selectDevTaskDetailCount(Integer firmwareTaskId) throws SQLException {
		return firmwareMapper.selectDevTaskDetailCount(firmwareTaskId);

	}

}
