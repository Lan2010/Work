package core.controller;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import core.common.StatusCode;
import core.pojo.DeviceOnline;
import core.pojo.ResultMessages;
import core.service.DeviceOnlineService;

@Controller
public class DeviceOnlineController {

	@Resource
	private DeviceOnlineService deviceOnlineService;

	@RequestMapping(value = "/api/device/addDeviceOnline", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String add(DeviceOnline deviceOnline) {
		try {
			deviceOnlineService.addDeviceOnline(deviceOnline);
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
		
	}
	
	@RequestMapping(value = "/api/device/updateDeviceOnline", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String delete(DeviceOnline deviceOnline) {
		try {
			deviceOnlineService.updateDeviceOnline(deviceOnline);
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
		
	}

	}

