package core.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.mapper.DeviceMapper;
import core.mapper.IpkMapper;
import core.pojo.Condition;
import core.pojo.Device;
import core.pojo.Ipk;
import core.pojo.IpkName;
import core.pojo.IpkTask;
import core.pojo.Page;
import core.service.IpkService;
import core.util.CommonUtils;
import core.util.FileUtil;
import core.util.HttpClientUtil;

@Service
public class IpkServiceImpl implements IpkService {
	@Resource
	public IpkMapper ipkMapper;
	@Resource
	public DeviceMapper deviceMapper;

	@Override
	public List<IpkName> listIpkName() throws SQLException {
		return ipkMapper.listIpkName();
	}

	@Override
	public Integer checkExit(Ipk ipk) throws SQLException {
		return ipkMapper.checkExit(ipk);

	}

	@Override
	public String uploadIpk(CommonsMultipartFile audio, String path) throws IOException {
		return FileUtil.uploadFile(audio, path);

	}

	@Override
	public Integer saveIpk(Ipk ipk) throws SQLException {
		return ipkMapper.saveIpk(ipk);

	}

	@Override
	public List<Ipk> listIpk(Ipk ipk, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(ipk);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return ipkMapper.listIpk(map);
	}

	@Override
	public Integer selectIpkCount(Ipk ipk) throws SQLException {
		return ipkMapper.selectIpkCount(ipk);
	}

	private Map<String, Object> setCondition(Ipk ipk) {
		if (ipk != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("ipkId", ipk.getIpkId());
			condition.put("ipkName", ipk.getIpkName());
			condition.put("ipkNameId", ipk.getIpkNameId());
			condition.put("model", ipk.getModel());
			condition.put("version", ipk.getVersion());
			condition.put("ipkDesc", ipk.getIpkDesc());
			condition.put("ipkPath", ipk.getIpkPath());
			condition.put("user_id", ipk.getUser_id());
			condition.put("addTime", ipk.getAddTime());
			condition.put("status", ipk.getStatus());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	private Map<String, Object> setCondition(Ipk ipk, Device device) {
		if (ipk != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("ipkId", ipk.getIpkId());
			condition.put("ipkName", ipk.getIpkName());
			condition.put("ipkNameId", ipk.getIpkNameId());
			condition.put("model", ipk.getModel());
			condition.put("version", ipk.getVersion());
			condition.put("ipkDesc", ipk.getIpkDesc());
			condition.put("ipkPath", ipk.getIpkPath());
			condition.put("user_id", ipk.getUser_id());
			condition.put("addTime", ipk.getAddTime());
			condition.put("status", ipk.getStatus());
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
	public Integer deleteIpk(Integer ipkId) throws SQLException {
		return ipkMapper.deleteIpk(ipkId);
	}

	@Override
	public List<String> queryVersion(Integer ipkNameId) throws SQLException {
		return ipkMapper.queryIpkVersion(ipkNameId);

	}

	@Override
	public Integer addIpkTask(IpkTask ipkTask) throws SQLException {
		return ipkMapper.addIpkTask(ipkTask);
	}

	@Override
	public List<Device> listDev(Ipk ipk, Page page, Device device) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(ipk, device);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return ipkMapper.listDev(map);
	}

	@Override
	public Integer selectDevCount(Ipk ipk, Device device) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(ipk, device);
		return ipkMapper.selectDevCount(map);
	}

	@Override
	public List<Device> listDevByRemove(Ipk ipk, Page page, Device device) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(ipk, device);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return ipkMapper.listDevByRemove(map);
	}

	@Override
	public Integer selectDevCountByRemove(Ipk ipk, Device device) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(ipk, device);
		return ipkMapper.selectDevCountByRemove(map);
	}

	@Override
	public List<Integer> AllDev(IpkTask ipkTask, Condition condition) {
		List<Integer> devId = new ArrayList<Integer>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ipkNameId", ipkTask.getIpkNameId());
			map.put("version", ipkTask.getVersion());
			// 添加设备条件
			map.put("dev_model", condition.getModel());
			map.put("belongUnitId", condition.getBelongUnitId());
			map.put("isBind", condition.getIsBind());
			map.put("isOnlined", condition.getIsOnlined());
			devId = ipkMapper.AllDev(map);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return devId;
	}

	@Override
	public List<Integer> AllDevByRemove(IpkTask ipkTask, Condition condition) throws SQLException {
		List<Integer> devId = new ArrayList<Integer>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ipkNameId", ipkTask.getIpkNameId());
			map.put("version", ipkTask.getVersion());
			// 添加设备条件
			map.put("dev_model", condition.getModel());
			map.put("belongUnitId", condition.getBelongUnitId());
			map.put("isBind", condition.getIsBind());
			map.put("isOnlined", condition.getIsOnlined());
			devId = ipkMapper.AllDevByRemove(map);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return devId;
	}

	@Override
	public String sendDevTask(IpkTask ipkTask, List<?> devnums) {
		String result = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ipkNameId", ipkTask.getIpkNameId());
			map.put("version", ipkTask.getVersion());
			String notify_url = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH, "io.dev.notify.url")
					+ "install_ipk";
			List<String> devNums = new ArrayList<String>();
			JSONObject json = new JSONObject();
			JSONArray ja = new JSONArray();
			for (Object devnum : devnums) {
				JSONObject js = new JSONObject();
				JSONObject conf = new JSONObject();
				Ipk ipk = new Ipk();
				map.put("devNum", devnum);
				if (devnum instanceof String) {
					if (ipkTask.getOperateType() == (short) 1) {
						devNums.add((String) devnum);
					} else {
						ipk = ipkMapper.selectIpkByDevNum(map);
						js.put("dev_num", devnum);
					}
				} else if (devnum instanceof Integer) {
					ipk = ipkMapper.selectIpkByDevId(map);
					js.put("dev_num", ipk.getDevNum());
					devNums.add(ipk.getDevNum());
				}
				if (ipkTask.getOperateType() != (short) 1) {
					conf.put("dl_url", ipk.getIpkPath());
					conf.put("ipk_md5", ipk.getIpk_md5());
					conf.put("append", ipkTask.getAppend());
					conf.put("notify_url", notify_url);
				}
				js.put("conf", conf);
				ja.add(js);
			}
			JSONObject postJson = new JSONObject();
			postJson.put("total", devnums.size());
			postJson.put("ipkTaskId", ipkTask.getIpkTaskId());

			String token = CommonUtils.getToken();
			// 1.卸载插件
			if (ipkTask.getOperateType() == (short) 1) {
				String url = Constant.SISCHAIN_URL + "/dev/removeIpk.do?token=" + token + "";
				JSONObject json01 = new JSONObject();
				JSONObject json02 = new JSONObject();
				json02.put("pkg", ipkMapper.getIpkNameByID(ipkTask.getIpkNameId()));
				json01.put("devNums", devNums);
				json01.put("conf", json02);
				postJson.put("data", json01);
				result = HttpClientUtil.doPostJson(url, postJson.toString());
			} else {
				// 2.安装插件
				postJson.put("data", ja);
				String url = Constant.SISCHAIN_URL + "/dev/installIpk.do?token=" + token + "";
				result = HttpClientUtil.doPostJson(url, postJson.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Ipk selectIpkById(Integer ipkId) throws SQLException {
		return ipkMapper.selectIpkById(ipkId);

	}

}
