package core.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.common.StatusCode;
import core.pojo.AdminUser;
import core.pojo.Device;
import core.pojo.DeviceLog;
import core.pojo.Page;
import core.pojo.ParamsAppoint;
import core.pojo.ResultMessages;
import core.service.DeviceLogService;
import core.service.DeviceService;
import core.util.CommonUtils;
import core.util.EncryptUtil;
import core.util.HttpClientUtil;

@Controller
public class DeviceController {

	@Resource
	private DeviceService deviceService;
	@Resource
	private DeviceLogService deviceLogService;

	@RequestMapping(value = "/api/device/list", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String list(Device device, Page page, Integer onlineStatus, Integer isBind) {
		try {
			System.out.println(device.toString());
			List<Map<String, Object>> list = deviceService.getDevice(device, page, onlineStatus, isBind);
			Integer resultCount = deviceService.getDeviceCount(device, onlineStatus, isBind);
			return new ResultMessages(StatusCode.OK, "请求成功", resultCount, list).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/device/addDevice", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String add(@ModelAttribute Device device, HttpServletRequest request) {
		String number = device.getNumber();
		DeviceLog deviceLog = new DeviceLog();
		HttpSession session = request.getSession(false);
		try {
			Device m = deviceService.getNumber(number);
			if (m != null) {
				return new ResultMessages(StatusCode.ERROR, "该编号已存在,请重新填写").toString();
			}
			if (session != null && session.getAttribute(Constant.SESSION_USER) != null) {
				AdminUser ad = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				device.setAddTime(new Date());
				device.setTag(1);
				device.setIsOnlined(0);
				device.setBelongUnitId(Constant.SUBORDINATE_UNITS_TZX);
				String defaultDevPasswd = CommonUtils.getPropertiesValue(Constant.APPLICATION_CONFIG_PATH, "dev.default.password");
				device.setDevPasswd(defaultDevPasswd);
				Integer count = deviceService.addDevice(device);
				if (count > 0) {
					deviceLog.setMac(device.getMac());
					deviceLog.setOperTime(new Date());
					deviceLog.setUser(ad.getUser_name());
					deviceLog.setOperation("用户" + ad.getUser_name() + "添加了一台设备编号为" + device.getNumber() + "的设备");
					deviceLogService.addDeviceLog(deviceLog);
				}
			}
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}

	}

	@RequestMapping(value = "/api/device/deleteDevice", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@ModelAttribute Device device, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		DeviceLog deviceLog = new DeviceLog();
		try {
			if (session != null && session.getAttribute(Constant.SESSION_USER) != null) {
				AdminUser ad = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				Device device2 = deviceService.getDeviceById(device.getId());
				deviceLog.setMac(device.getMac());
				deviceLog.setOperTime(new Date());
				deviceLog.setUser(ad.getUser_name());
				deviceLog.setOperation("用户" + ad.getUser_name() + "删除了一台设备编号为" + device2.getNumber() + "的设备");
				device.setTag(0);
				deviceService.updateDevice(device);
				deviceLogService.addDeviceLog(deviceLog);
			}
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}

	}

	@RequestMapping(value = "/api/device/updateDevice", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String update(@ModelAttribute Device device, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		DeviceLog deviceLog = new DeviceLog();
		try {
			if (session != null && session.getAttribute(Constant.SESSION_USER) != null) {
				AdminUser ad = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				deviceLog.setMac(device.getMac());
				deviceLog.setOperTime(new Date());
				deviceLog.setUser(ad.getUser_name());
				deviceLog.setOperation("用户" + ad.getUser_name() + "修改了一台设备编号为" + device.getNumber() + "的设备");
				deviceService.updateDevice(device);
				deviceLogService.addDeviceLog(deviceLog);
			}
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}

	}

	@RequestMapping(value = "/api/device/importDevice", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String importDevice(@RequestParam("excel") MultipartFile file, HttpServletRequest request) {
		try {
			Integer returnInt = deviceService.importDevice(file, request);
			if (returnInt != null) {
				if (returnInt > 0) {
					return new ResultMessages(StatusCode.OK, "成功导入" + returnInt + "台设备型号").toString();
				} else if (returnInt.equals(-2)) {
					return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "录入的设备编号已存在").toString();
				} else if (returnInt.equals(-1)) {
					return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "设备编号格式不正确").toString();
				} else if (returnInt.equals(-3)) {
					return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "设备型号不能为空").toString();
				} else {
					return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
				}
			} else {
				return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}

	}

	@RequestMapping(value = "/api/device/setConfig", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String setConfig(/* SetConfig setConfig, */@RequestBody JSONObject json) {
		try {
			// String url = "http://127.0.0.1:8080/device-backend/getJson";
			String token = getToken();
			String url = Constant.SISCHAIN_URL + "/dev/setConfig.do?token=" + token + "";
			// String url = "http://192.168.11.102:8080/tzx/setConfig";
			// String ssString =
			// "{\"dev_num\":\""+dev_num+"\",\"data\":{\"conf\":[{\"o\":\"newprobe.probe.enable\",\"v\":\""+setConfig.getProbeEnable()+"\"},{\"o\":\"newprobe.probe.server\",\"v\":\""+setConfig.getProbeServer()+"\"},{\"o\":\"newprobe.probe.port\",\"v\":\""+setConfig.getProbePort()+"\"},{\"o\":\"newprobe.probe.capcnt\",\"v\":\""+setConfig.getProbeCapcnt()+"\"},{\"o\":\"wireless.@wifi-iface[0].ssid\",\"v\":\""+setConfig.getWifi()+"\"}]}}";
			String ssString = json.toJSONString();
			String result = HttpClientUtil.doPostJson(url, ssString);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/device/reboot", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String reboot(Integer reboot, String dev_num) {
		try {
			// String url = "http://127.0.0.1:8080/device-backend/getJson";
			String token = getToken();
			String url = Constant.SISCHAIN_URL + "/dev/reboot.do?token=" + token + "";
			String ssString = "{\"dev_num\":\"" + dev_num + "\",\"data\":{\"reboot\":\"" + reboot + "\"}}";
			// String ssString =
			// "{\"dev_num\":\"98780E0A0287\",\"data\":{\"reboot\":\"1\"}}";
			String result = HttpClientUtil.doPostJson(url, ssString);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/device/getDeviceDetail", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String getDeviceDetail(String dev_num) {
		try {
			// String url = "http://127.0.0.1:8080/device-backend/getJson";
			String token = getToken();
			if (token == null) {
				return new ResultMessages(StatusCode.INVALID_TOKEN, "token is invalid").toString();
			}
			JSONObject postJson = new JSONObject();
			JSONArray conf = new JSONArray();
			String url = Constant.SISCHAIN_URL + "/dev/getConfig.do?token=" + token + "";
			List<ParamsAppoint> list = deviceService.getParamsAppoint();
			for (ParamsAppoint p : list) {
				conf.add(p.getDevParam());
			}
			postJson.put("conf", conf);
			postJson.put("dev_num", dev_num);
			// String ssString =
			// "{\"dev_num\":\""+dev_num+"\",\"conf\":[\"newprobe.probe.enable\",\"newprobe.probe.server\",\"newprobe.probe.port\",\"newprobe.probe.capcnt\",\"wireless.@wifi-iface[0].ssid\"]}";
			String ssString = postJson.toJSONString();
			// String postResult = "{\"code\":0," +
			// "\"msg\":\"请求成功\"," +
			// "\"conf\": [ \r\n" +
			// " {\"o\":\"newprobe.probe.enable\", \"v\":\"1\"}, \r\n" +
			// " {\"o\":\"newprobe.probe.server\", \"v\":\"192.168.11.29\"}, \r\n" +
			// " {\"o\":\"newprobe.probe.port\", \"v\":\"3333\"},\r\n" +
			// " {\"o\":\"newprobe.probe.capcnt\", \"v\":\"2000\"} ,\r\n" +
			// " {\"o\":\"wireless.@wifi-iface[0].ssid\", \"v\":\"ssid\"}\r\n" +
			// "]}";
			String postResult = HttpClientUtil.doPostJson(url, ssString);
			String result = parseJson(postResult, list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/device/setPasswd", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String setPasswd(String devNum, String user, String passwd) {
		try {
			Device device = deviceService.getDeviceById(Integer.valueOf(devNum));
			device.setDevPasswd(passwd);
			//1.保存数据库
			Integer code = deviceService.changePasswd(device);
			//2.发送设备
			// String url = "http://127.0.0.1:8080/device-backend/getJson";
			String token = getToken();
			String url = Constant.SISCHAIN_URL + "/dev/setPasswd.do?token=" + token + "";
			JSONObject postJson = new JSONObject();
			JSONObject json = new JSONObject();
			json.put("user", user);
			json.put("passwd", passwd);			
			postJson.put("dev_num", device.getNumber());
			postJson.put("data", json);
			String result = HttpClientUtil.doPostJson(url, postJson.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	/**
	 * 解析并封装json
	 * 
	 * @param source
	 * @param list
	 * @return
	 */
	private String parseJson(String source, List<ParamsAppoint> list) {
		JSONObject parse = JSON.parseObject(source);
		JSONArray data = (JSONArray) parse.get("conf");
		if (data == null || data.isEmpty()) {
			return source;
		}
		JSONObject ov = null;
		ParamsAppoint p = null;
		for (Object o : data) {
			ov = (JSONObject) o;
			for (ParamsAppoint temp : list) {
				if (ov.get("o") != null && ov.get("o").equals(temp.getDevParam())) {
					temp.setValue((String) ov.get("v"));
				}
			}
		}
		return new ResultMessages(StatusCode.OK, "请求成功").put("conf", list).toString();
	}

	public String getToken() throws JSONException {
		String appid = "comm13a61d4d0fd6";
		String randomString = RandomStringUtils.randomAlphanumeric(8);
		long now = System.currentTimeMillis();
		String appsecret = "deadb2e78c35db589ac0cb9a50ce6293";
		String signature = EncryptUtil.encodeBySHA1(appid + randomString + now + appsecret);
		// String url = "http://127.0.0.1:8080/device-backend/getJson";
		String url = Constant.SISCHAIN_URL + "/token?appid=" + appid + "&randomString=" + randomString + "&now=" + now
				+ "&signature=" + signature + "";
		String result = HttpClientUtil.doGet(url);
		JSONObject json = JSONObject.parseObject(result);
		if (json == null || json.isEmpty()) {
			return null;
		}
		Integer code = (Integer) json.get("code");
		if (0 == code) {
			JSONObject data = (JSONObject) json.get("data");
			if (data == null) {
				return null;
			}
			String obj = (String) data.get("token");
			return obj;
		} else {
			return null;
		}
	}

}
