package com.tianzhixing.devicecomm.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tianzhixing.devicecomm.common.StatusCode;
import com.tianzhixing.devicecomm.mapper.DeviceMapper;
import com.tianzhixing.devicecomm.mqtt.MqttGateway;
import com.tianzhixing.devicecomm.pojo.ResultMessage;
import com.tianzhixing.devicecomm.pojo.Task;
import com.tianzhixing.devicecomm.service.DeviceService;

@RestController
public class MqttPubController {
	private static Logger log = LoggerFactory.getLogger(MqttPubController.class);

	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private MqttGateway mqttGateway;
	@Autowired
	private DeviceService deviceService;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

	// 设置配置
	@ResponseBody
	@RequestMapping(value = "/dev/setConfig.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String setConfig(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		String devNum = json.getString("dev_num");
		JSONArray conf = json.getJSONArray("conf");
		if (conf.size() <= 0 || devNum == null || devNum.length() <= 0) {
			return new ResultMessage(StatusCode.ERROR_REQ_PARAM, "系统繁忙~").toString();
		}
		String taskId = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
		String topic = "s/dev/" + devNum + "/rctl/set_config/" + taskId;
		try {
			mqttGateway.sendToMqtt(topic, 2, false, json.toString());
			Task task = new Task(taskId, devNum, "setConfig", -1);
			task.setStartTime(new Date());
			System.out.println("task:" + task.toString());
			int result = deviceMapper.startTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SYSTEM_IS_BUSY, "系统繁忙~").toString();
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 读取设备配置
	@ResponseBody
	@RequestMapping(value = "/dev/getConfig.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getConfig(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		String devNum = json.getString("dev_num");
		JSONArray conf = json.getJSONArray("conf");
		if (conf.size() <= 0 || devNum == null || devNum.length() <= 0) {
			return new ResultMessage(StatusCode.ERROR_REQ_PARAM, "系统繁忙~").toString();
		}
		String taskId = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
		String topic = "s/dev/" + devNum + "/rctl/get_config/" + taskId;
		try {
			mqttGateway.sendToMqtt(topic, 2, false, json.toString());
			Task task = new Task(taskId, devNum, "getConfig", -1);
			task.setStartTime(new Date());
			System.out.println("task:" + task.toString());
			int result = deviceMapper.startTask(task);
			String confs = deviceService.getConf(taskId, devNum);
			if (confs == null) {
				return new ResultMessage(StatusCode.NO_CONFIG, "设备获取配置失败！").toString();
			}
			ResultMessage resultmessage = new ResultMessage(StatusCode.OK, "请求成功");
			resultmessage.put("conf", JSONArray.parseArray(confs));
			return resultmessage.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		} catch (JSONException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.ERROR_JSON, "系统繁忙~").toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SYSTEM_IS_BUSY, "系统繁忙~").toString();
		}
	}

	// 安装插件
	@ResponseBody
	@RequestMapping(value = "/dev/installIpk.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String installIpk(@RequestBody JSONObject json) {
		System.out.println("-----------01----------");
		System.out.println("installIpk json:" + json);
		JSONArray data = json.getJSONArray("data");
		Integer ipkTaskId = json.getInteger("ipkTaskId");
		if (data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				JSONObject job = data.getJSONObject(i);
				String devNum = job.getString("dev_num");
				JSONObject conf =data.getJSONObject(i).getJSONObject("conf");
				String taskid = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
				String topic = "s/dev/" + devNum + "/rctl/install_ipk/" + taskid;
				mqttGateway.sendToMqtt(topic, 2, false, conf.toString());
				Task task = new Task(taskid, devNum, "installIpk", new Date(), -1, ipkTaskId);
				System.out.println("task:" + task.toString());
				try {
					int result = deviceMapper.startTask(task);
					int result01 = deviceMapper.addMiddleTask(task);

				} catch (SQLException e) {
					e.printStackTrace();
					log.error(e.getMessage());
					return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
				}
			}
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 设备端回复 * 安装插件
	@ResponseBody
	@RequestMapping(value = "/dev/reply/install_ipk", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String replyinstallIpk(HttpServletRequest request) {
		System.out.println("-----------03----------");
		String status = request.getParameter("status");
		String devNum = request.getParameter("devid");
		String taskid = request.getParameter("taskid");

		Task task = new Task(taskid, devNum, "installIpk", Integer.valueOf(status));
		task.setReplayTime(new Date());
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 移除插件
	@ResponseBody
	@RequestMapping(value = "/dev/removeIpk.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String removeIpk(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		JSONArray devNums = json.getJSONObject("data").getJSONArray("devNums");
		JSONObject payload = json.getJSONObject("data").getJSONObject("conf");
		Integer ipkTaskId = json.getInteger("ipkTaskId");
		for (int i = 0; i < devNums.size(); i++) {
			String devNum = devNums.getString(i);
			String taskid = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
			String topic = "s/dev/" + devNum + "/rctl/remove_ipk/" + taskid;
			try {
				mqttGateway.sendToMqtt(topic, 2, false, payload.toString());
				Task task = new Task(taskid, devNum, "removeIpk", new Date(), -1, ipkTaskId);
				System.out.println("task:" + task.toString());
				int result = deviceMapper.startTask(task);
				int result01 = deviceMapper.addMiddleTask(task);

			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 获取插件信息
	@ResponseBody
	@RequestMapping(value = "/dev/infoIpk.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String infoIpk(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		String[] devNums = json.getString("dev_num").split(",");
		JSONObject payload = json.getJSONObject("data");
		for (String devNum : devNums) {
			String taskid = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
			String topic = "s/dev/" + devNum + "/rctl/info_ipk/" + taskid;
			mqttGateway.sendToMqtt(topic, 2, false, payload.toString());
			Task task = new Task(taskid, devNum, "infoIpk", -1);
			task.setStartTime(new Date());
			try {
				int result = deviceMapper.startTask(task);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
			}
			System.out.println("task:" + task.toString());
		}

		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 系统重启
	@ResponseBody
	@RequestMapping(value = "/dev/reboot.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String reboot(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		String[] devNums = json.getString("dev_num").split(",");
		if (devNums.length <= 0) {
			return new ResultMessage(StatusCode.ERROR_REQ_PARAM, "系统繁忙~").toString();
		}
		String payload = json.getJSONObject("data").toString();
		for (String devNum : devNums) {
			String taskid = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
			String topic = "s/dev/" + devNum + "/rctl/reboot/" + taskid;
			try {
				mqttGateway.sendToMqtt(topic, 2, false, payload);
				Task task = new Task(taskid, devNum, "reboot", -1);
				task.setStartTime(new Date());
				System.out.println("task:" + task.toString());
				int result = deviceMapper.startTask(task);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return new ResultMessage(StatusCode.SYSTEM_IS_BUSY, "系统繁忙~").toString();
			}
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 执行短脚本
	@ResponseBody
	@RequestMapping(value = "/dev/shell.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String shell(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		JSONArray devNums = json.getJSONObject("data").getJSONArray("devNums");
		JSONObject payload = json.getJSONObject("data").getJSONObject("conf");
		Integer taskId = json.getInteger("taskId");
		for (int i = 0; i < devNums.size(); i++) {
			String devNum = devNums.getString(i);
			String taskid = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
			String topic = "s/dev/" + devNum + "/rctl/shell/" + taskid;
			try {
				mqttGateway.sendToMqtt(topic, 2, false, payload.toString());
				Task task = new Task(taskid, devNum, "shell", new Date(), -1, taskId);
				System.out.println("task:" + task.toString());
				int result = deviceMapper.startTask(task);
				int result01 = deviceMapper.addshellMiddleTask(task);

			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 设备端回复 * 执行短脚本
	@ResponseBody
	@RequestMapping(value = "/dev/reply/shell", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String replyshell(HttpServletRequest request) {
		System.out.println("-----------03----------");
		String status = request.getParameter("status");
		String devNum = request.getParameter("devid");
		String taskid = request.getParameter("taskid");
		Task task = new Task(taskid, devNum, "shell", Integer.valueOf(status));
		task.setStartTime(new Date());
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 升级固件
	@ResponseBody
	@RequestMapping(value = "/dev/firmware.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String firmware(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		Integer taskId = json.getInteger("firmwareTaskId");
		JSONArray datas = json.getJSONArray("data");
		for (int i = 0; i < datas.size(); i++) {
			JSONObject data = datas.getJSONObject(i);
			String devNum = data.getString("dev_num");
			JSONObject payload = data.getJSONObject("conf");
			String taskid = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
			String topic = "s/dev/" + devNum + "/rctl/firmware/" + taskid;
			try {
				mqttGateway.sendToMqtt(topic, 2, false, payload.toString());
				Task task = new Task(taskid, devNum, "firmware", new Date(), -1, taskId);
				System.out.println("task:" + task.toString());
				int result = deviceMapper.startTask(task);
				int result01 = deviceMapper.addfirmwareMiddleTask(task);

			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 设备端回复 * 执行短脚本
	@ResponseBody
	@RequestMapping(value = "/dev/reply/firmware", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String firmware(HttpServletRequest request) {
		System.out.println("-----------03----------");
		String status = request.getParameter("status");
		String devNum = request.getParameter("devid");
		String taskid = request.getParameter("taskid");
		Task task = new Task(taskid, devNum, "firmware", Integer.valueOf(status));
		task.setStartTime(new Date());
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.replayTask(task);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		}
		return new ResultMessage(StatusCode.OK, "请求成功").toString();
	}

	// 更改设备系统用户密码
	@ResponseBody
	@RequestMapping(value = "/dev/setPasswd.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String setPasswd(@RequestBody JSONObject json) {
		System.out.println("json:" + json);
		String devNum = json.getString("dev_num");
		JSONObject payload = json.getJSONObject("data");
		String taskId = sdf.format(new Date()) + devNum + (int) ((Math.random() * 9 + 1) * 1000);
		String topic = "s/dev/" + devNum + "/rctl/set_passwd/" + taskId;
		mqttGateway.sendToMqtt(topic, 2, false, payload.toString());
		Task task = new Task(taskId, devNum, "setPasswd", -1);
		task.setStartTime(new Date());
		System.out.println("task:" + task.toString());
		try {
			int result = deviceMapper.startTask(task);
			Integer reply = deviceService.getSetPasswdReply(taskId, devNum);
			if (reply ==0 ) {
				return new ResultMessage(StatusCode.OK, "请求成功").toString();	
			}
			return new ResultMessage(StatusCode.CHANGE_PASSWD_FAILED, "修改设备系统用户密码失败！").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessage(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		}
	}

}
