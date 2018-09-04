package core.controller;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.common.Constant;
import core.common.StatusCode;
import core.pojo.AdminUser;
import core.pojo.DeviceLog;
import core.pojo.Page;
import core.pojo.ResultMessages;
import core.service.DeviceLogService;

@Controller
public class DeviceLogController {

	@Resource
	private DeviceLogService deviceLogService;

	@RequestMapping(value = "/api/device/deviceLog", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String list(HttpServletRequest request, DeviceLog deviceLog, Page page) {
		List<DeviceLog> list;
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				if (adminUser.getRole_id() != 0) {
					// 普通人员，只能查询自己的操作日志
					if (deviceLog.getUser() == null) {
						deviceLog.setUser(adminUser.getUser_name());
					}
					if (!deviceLog.getUser().equals(adminUser.getUser_name())) {
						return new ResultMessages(StatusCode.OK, "请求成功", 0, null).toString();
					}
				}
				list = deviceLogService.getDeviceLog(deviceLog, page);
				Integer resultCount = deviceLogService.getDeviceLogCount(deviceLog);
				return new ResultMessages(StatusCode.OK, "请求成功", resultCount, list).toString();

			} else {
				return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "身份过期,请重新登陆").toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}
}
