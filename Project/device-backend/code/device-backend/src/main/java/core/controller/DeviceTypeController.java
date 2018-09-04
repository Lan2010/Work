package core.controller;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import core.common.StatusCode;
import core.pojo.DeviceType;
import core.pojo.ResultMessages;
import core.service.DeviceTypeService;

@Controller
public class DeviceTypeController {

	
	@Resource
	private DeviceTypeService deviceTypeService;
	@RequestMapping(value = "/api/device/modelList", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String modelList() {	
		List<DeviceType> list;
		try {
			list = deviceTypeService.getDeviceType();
			return new ResultMessages(StatusCode.OK, "请求成功", list).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
		
	}
	
	@RequestMapping(value = "/api/device/addDeviceType", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String add(DeviceType deviceType) {
		String code = deviceType.getCode();
		String name = deviceType.getName();
		try {
			if (code==""||TextUtils.isEmpty(code)){
				return new ResultMessages(StatusCode.ERROR, "输入的内容不能为空").toString();
			}
			if (name==""||TextUtils.isEmpty(name)){
				return new ResultMessages(StatusCode.ERROR, "输入的内容不能为空").toString();
			}
			DeviceType m = deviceTypeService.getCode(code);
			if (m != null) {
				return new ResultMessages(StatusCode.ERROR, "该型号经存在,请重新填写").toString();
			}
			deviceTypeService.addDeviceType(deviceType);
			return new ResultMessages(StatusCode.OK, "请求成功").toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
		
	}
	
	@RequestMapping(value = "/api/device/deleteDeviceType", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String delete(DeviceType deviceType) {
		try {
			deviceType.setTag(0);
			deviceTypeService.updateDeviceType(deviceType);
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

