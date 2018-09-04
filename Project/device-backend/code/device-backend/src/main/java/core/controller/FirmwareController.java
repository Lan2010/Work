package core.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.common.StatusCode;
import core.mapper.DeviceMapper;
import core.pojo.AdminUser;
import core.pojo.Condition;
import core.pojo.DevTaskDetail;
import core.pojo.Device;
import core.pojo.Firmware;
import core.pojo.FirmwareName;
import core.pojo.FirmwareTask;
import core.pojo.Page;
import core.pojo.ResultMessages;
import core.service.FirmwareService;
import core.service.IpkService;
import core.util.FileUtil;

/**
 * 
 * @Description:固件
 * @author: dev-lan
 * @date: 2018年7月18日
 */
@Controller
public class FirmwareController {
	private static Logger log = LoggerFactory.getLogger(FirmwareController.class);

	@Resource
	private IpkService ipkService;
	@Resource
	private FirmwareService firmwareService;
	@Resource
	public DeviceMapper deviceMapper;

	// 所有插件名称列表
	@RequestMapping(value = "/api/firmware/firmwareNamelist", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String listFirmwareName() {
		try {
			List<FirmwareName> firmwareNamelist = firmwareService.listFirmwareName();
			return new ResultMessages(StatusCode.OK, "请求成功", firmwareNamelist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/firmware/upload", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String uploadIpk(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") CommonsMultipartFile file, @ModelAttribute Firmware firmware) {
		System.out.println("------uploadIpk----");
		HttpSession session = request.getSession(false);
		try {
			Integer count = firmwareService.checkExit(firmware);
			if (count >= 1) {
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "该固件型号已存在该版本，请重输版本号!").toString();
			}
			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
		
			String basePath = request.getServletContext().getRealPath("/"); // linux路径
			// 获取文件需要上传到的路径
			String ipkPath = "file/firmware/";
			String path = basePath + ipkPath;
			firmware.setFirmware_md5(FileUtil.getMd5ByFile(file));
			// 1.文件上传
			String fileName = ipkService.uploadIpk(file, path);
			firmware.setUser_id(user.getUser_id());
			String dataPath = request.getScheme() + "://" + request.getServerName()  + "/"+ ipkPath + fileName;
			firmware.setFirmwarePath(dataPath);
			firmware.setAddTime(new Date());
			firmware.setStatus(1);
			System.out.println(firmware.toString());
			Integer result = firmwareService.saveFirmware(firmware);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "系统繁忙，请稍候再试！").toString();
			}
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessages(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessages(StatusCode.IO_EXCEPTION, "系统繁忙~").toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙，请稍后再试").toString();

		}
	}

	@RequestMapping(value = "/api/firmware/list", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String listFirmware(Firmware firmware, Page page) {
		try {
			List<Firmware> firmwarelist = firmwareService.listFirmware(firmware, page);
			Integer count = firmwareService.selectFirmwareCount(firmware);
			return new ResultMessages(StatusCode.OK, "请求成功", count, firmwarelist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/firmware/delete", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteFirmware(Integer firmwareId) {
		try {
			Firmware firmware = firmwareService.selectFirmwareById(firmwareId);
			// 1.删除数据库信息
			Integer result = firmwareService.deleteFirmware(firmwareId);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "删除失败，请稍候再试！").toString();
			}
			// 2.删除服务器硬盘中的文件
			String ipkPath = firmware.getFirmwarePath();
			if (ipkPath == null || ipkPath.length() <= 0) {
				return new ResultMessages(StatusCode.OK, "请求成功").toString();
			}
			File ipkfile = new File(ipkPath);
			if (ipkfile.exists()) {
				ipkfile.delete();
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

	// 某个固件的所有版本
	@RequestMapping(value = "/api/firmware/queryVersion", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String queryVersion(HttpServletRequest request, HttpServletResponse response, Integer firmwareNameId) {
		try {

			List<String> ipkVersionlist = firmwareService.queryVersion(firmwareNameId);
			JSONArray ja = new JSONArray();
			for (String version : ipkVersionlist) {
				JSONObject json = new JSONObject();
				json.put("version", version);
				ja.add(json);
			}
			return new ResultMessages(StatusCode.OK, "请求成功", ja).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	// 某个插件的所有设备
	@RequestMapping(value = "/api/firmware/queryDevList", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String queryDevList(Firmware firmware, Page page, Device device) {
		try {
			List<Device> devlist = firmwareService.listDev(firmware, page, device);
			Integer count = firmwareService.selectDevCount(firmware, device);
			return new ResultMessages(StatusCode.OK, "请求成功", count, devlist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	// 1.新建任务 - 输入多个设备编号
	@RequestMapping(value = "/api/firmware/addTaskByDevids", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addIpkTaskByDevids(HttpServletRequest request, HttpServletResponse response,
			FirmwareTask firmwareTask, String devNums) {
		if (devNums == null || devNums.length() <= 0) {
			return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备不能为空，请输入设备！").toString();
		}
		HttpSession session = request.getSession(false);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("firmwareNameId", firmwareTask.getFirmwareNameId());
			map.put("version", firmwareTask.getVersion());

			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			firmwareTask.setUser_id(user.getUser_id());
			firmwareTask.setAddTime(new Date());

			List<String> devnums = new ArrayList<String>();
			List<String> NoPath_devnums = new ArrayList<String>();
			List<String> error_devnums = new ArrayList<String>();

			for (String devnum : devNums.split(",")) {
				Integer count = deviceMapper.checkDevExit(devnum);
				if (count >= 1) {
					map.put("devNum", devnum);
					Firmware firmware = firmwareService.selectFirmwareByDevNum(map);
					if (firmware == null) {
						NoPath_devnums.add((String) devnum);
					} else {
						devnums.add(devnum);
					}
				} else {
					// 该设备不存在
					error_devnums.add(devnum);
				}
			}
			if (!error_devnums.isEmpty() || !NoPath_devnums.isEmpty()) {
				JSONObject json = new JSONObject();
				json.put("error_devnums", error_devnums);
				json.put("NoPath_devnums", NoPath_devnums);
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备输入有误，请检查输入内容！", json).toString();
			}
			firmwareTask.setDevTotal(devnums.size());
			// 1.任务信息保存数据库
			Integer result = firmwareService.addFirmwareTask(firmwareTask);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "新增失败，请稍候再试！").toString();
			}
			// 2.发送请求到设备
			String result02 = firmwareService.sendDevTask(firmwareTask, devnums);

			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "设备集合输入有误，请检查输入字符是否正确！").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	// 2.新建任务 - 条件搜索设备选择设备
	@RequestMapping(value = "/api/firmware/addTaskBySearch", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addIpkTaskBySearch(HttpServletRequest request, HttpServletResponse response,
			FirmwareTask firmwareTask, Condition condition) {
		HttpSession session = request.getSession(false);
		try {
			AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			firmwareTask.setUser_id(user.getUser_id());
			firmwareTask.setAddTime(new Date());

			// 设备列表反选时，devIds不能为空
			if (condition.getSelectType() == 0
					&& (condition.getDevIds() == null || condition.getDevIds().length() <= 0)) {
				return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "未选择设备，请选择设备！").toString();
			}
			List<Integer> devIds = new ArrayList<Integer>();
			List<Integer> devlist = firmwareService.AllDev(firmwareTask, condition);
			// 全选，devIds为removed;反选，devIds为added
			if (condition.getDevIds() == null || condition.getDevIds().length() <= 0) {
				devIds = devlist;
			} else {
				for (String s : condition.getDevIds().split(",")) {
					devIds.add(Integer.valueOf(s));
				}
				// 全选,升级设备= 刷选设备-devIds
				if (condition.getSelectType() == 1) {
					devlist.removeAll(devIds);
					devIds = devlist;
				}
			}
			firmwareTask.setDevTotal(devIds.size());
			Integer result = firmwareService.addFirmwareTask(firmwareTask);
			if (result <= 0) {
				return new ResultMessages(StatusCode.SQL_EXCEPTION, "新增失败，请稍候再试！").toString();
			}
			// 2.发送请求到设备
			String result02 = firmwareService.sendDevTask(firmwareTask, devIds);
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/firmware/tasklist", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String listFirmwareTask(HttpServletRequest request, HttpServletResponse response, FirmwareTask firmwareTask,
			Page page) {
		System.out.println("---------listFirmwareTask---------");
		try {
			System.out.println(firmwareTask.toString());
			Integer result = firmwareService.updateCompleteDev();
			List<FirmwareTask> firmwareTasklist = firmwareService.listFirmwareTask(firmwareTask, page);
			Integer count = firmwareService.selectFirmwareTaskCount(firmwareTask);
			return new ResultMessages(StatusCode.OK, "请求成功", count, firmwareTasklist).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/firmware/taskDetailById", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String queryFirmwareTaskById(Integer firmwareTaskId) {
		System.out.println("---------queryFirmwareTaskById---------");
		try {
			Integer result = firmwareService.updateCompleteDev();
			FirmwareTask firmwareTask = firmwareService.queryFirmwareTaskById(firmwareTaskId);
			return new ResultMessages(StatusCode.OK, "请求成功", firmwareTask).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/firmware/devDetailByTask", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String devDetailByTask(Integer firmwareTaskId, Page page) {
		try {
			List<DevTaskDetail> devTaskDetail = firmwareService.listDevTaskDetail(firmwareTaskId, page);
			Integer count = firmwareService.selectDevTaskDetailCount(firmwareTaskId);
			return new ResultMessages(StatusCode.OK, "请求成功", count, devTaskDetail).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

}
