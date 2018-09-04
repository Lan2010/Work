package core.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import core.common.Constant;
import core.common.StatusCode;
import core.mapper.LoginLogMapper;
import core.pojo.AdminUser;
import core.pojo.LoginLog;
import core.pojo.ResultMessages;
import core.service.IPermissionService;
import core.service.IUserService;
import core.service.LoginLogService;
import core.util.ValidationCodeUtil;

/**
 * 用户登录注销
 * 
 * @author dev-jin
 * @date 2018年6月20日
 */
@Controller
public class LoginController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Resource
	private IUserService iUserService;
	@Resource
	private IPermissionService iPermissionService;
	@Resource
	private LoginLogMapper loginLogMapper;
	@Resource
	private LoginLogService loginLogService;

	/**
	 * 后台用户登录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/api/login", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String userLogin(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute AdminUser adminUser) {
		String result;
		try {
			result = iUserService.getAdminUserByName(adminUser, request, response);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("SQLException.",e);
			return new ResultMessages(StatusCode.SQL_EXCEPTION, "系统繁忙").toString();
		}

	}

	@RequestMapping(value = "/api/logout", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(Constant.SESSION_USER) != null) {
			try {
				LoginLog loginLog = loginLogMapper.getOneLoginLog();
				loginLog.setLoginOutTime(new Date());
				loginLogMapper.updateLoginLog(loginLog);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultMessages(StatusCode.ERROR, "注销失败,录入日志发生错误").toString();
			}
			session.removeAttribute(Constant.SESSION_USER);
			//提供登录信息给运营平台
			loginLogService.login4oms(request,0);
			return new ResultMessages(StatusCode.OK, "注销成功").toString();
		} else {
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/validateCode", method = RequestMethod.GET)
	public void validateCode(HttpServletRequest request, HttpServletResponse response) {
		String validationCode = null;
		HttpSession session = request.getSession();
		try {
			// 指定输出的内容类型
			String mimeType = request.getSession().getServletContext().getMimeType("validationCode.png");
			response.setContentType(mimeType);
			// 指定不要缓存
			response.setHeader("Expires", "-1");
			response.setHeader("Cache-control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			// 生成验证码图片并输出
			OutputStream out = response.getOutputStream();
			validationCode = ValidationCodeUtil.createValidationCodeImage(out);// 生成随机验证码的4个字符的字符串
			// session.setAttribute(AppConstant.SESSION_VALIDATION_CODE, validationCode);//
			// 将生成的验证码保存在session中
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/insertP", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String list(AdminUser adminUser) {
		try {
			adminUser.setUser_name("admin");
			adminUser.setPassword("admin");
			Integer resultCount = iUserService.allotAccount(adminUser);
			return new ResultMessages(StatusCode.OK, "请求成功", resultCount).toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	@RequestMapping(value = "/api/login/menu", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String userLoginmenu(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		JSONObject jObject = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_USER);
			// 用户已登录
			if (adminUser != null && adminUser.getRole_id() == null) {
				adminUser.setRole_id((short) 0);
			}
			// 根据用户roleId获取菜单json
			jObject = iPermissionService.getMenuJsonByUser(adminUser);
			session.setAttribute(Constant.SESSION_USERMENU, jObject);
		}
		return new ResultMessages(StatusCode.OK, "请求成功", jObject).toString();
	}

}
