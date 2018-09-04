package core.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.common.Constant;
import core.common.StatusCode;
import core.pojo.AdminUser;
import core.pojo.Page;
import core.pojo.ResultMessages;
import core.service.IUserService;
import core.util.EncryptUtil;

@Controller
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	@Resource
	private IUserService iUserService;

	// @RequestMapping(value = "/api/user", produces = "application/json;
	// charset=utf-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String getUser(AdminUser adminUser) {
	// try {
	// AdminUser adUser = iUserService.getUserByName(adminUser);
	// return new ResultMessages(StatusCode.OK, "请求成功", adUser).toString();
	// } catch (Exception e) {
	// logger.error(e.getMessage());
	// return new ResultMessages(StatusCode.SQL_EXCEPTION, "系统繁忙~").toString();
	// }
	// }

	@RequestMapping(value = "/api/passModify", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String update(HttpServletRequest request, AdminUser adminUser) {
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				AdminUser a = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				String password = EncryptUtil.encodeByMD5(adminUser.getPassword() + Constant.LOGIN_SECRETKEY);
				adminUser.setPassword(password);
				adminUser.setUser_name(a.getUser_name());
				int result = iUserService.updateAdminUser(adminUser);
				return new ResultMessages(StatusCode.OK, "请求成功").toString();
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

	@RequestMapping(value = "/api/user/list", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String listUser(HttpServletRequest request, AdminUser user, Page page) {
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				if (adminUser.getRole_id() != 0) {
					return new ResultMessages(StatusCode.NOT_AUTH, "用户无权限！").toString();
				}
				// 查找用户的总记录数
				int count = iUserService.selectUserCount(user);
				List<AdminUser> userList = iUserService.listUser(user, page);
				if (userList == null) {
					return new ResultMessages(StatusCode.SQL_EXCEPTION, "请求超时").toString();
				}
				return new ResultMessages(StatusCode.OK, "请求成功", count, userList).toString();
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

	@RequestMapping(value = "/api/user/add", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String addUser(HttpServletRequest request, @ModelAttribute AdminUser user) {
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				if (adminUser.getRole_id() != 0) {
					return new ResultMessages(StatusCode.NOT_AUTH, "用户无权限！").toString();
				}
				String password = EncryptUtil.encodeByMD5(user.getPassword() + Constant.LOGIN_SECRETKEY);
				user.setPassword(password);
				user.setCreate_time(new Date());
				user.setStatus((short) 1);
				// 检查用户名是否存在
				int count = iUserService.checkExit(user.getUser_name());
				if (count >= 1) {
					return new ResultMessages(StatusCode.ERROR_REQ_PARAM, "用户已存在，请重新输入用户名！").toString();
				}
				// 新增用户
				int result = iUserService.addUser(user, request);
				if (result <= 0) {
					return new ResultMessages(StatusCode.SQL_EXCEPTION, "请求超时").toString();
				}
				return new ResultMessages(StatusCode.OK, "请求成功").toString();
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

	@RequestMapping(value = "/api/user/disable", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String disableUser(HttpServletRequest request, @ModelAttribute AdminUser user) {
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				if (adminUser.getRole_id() != 0) {
					return new ResultMessages(StatusCode.NOT_AUTH, "用户无权限！").toString();
				}
				user.setModify_time(new Date());
				int result = iUserService.updateAdminUser(user);
				return new ResultMessages(StatusCode.OK, "请求成功").toString();
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

	@RequestMapping(value = "/api/user/changePasswd", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String changePasswd(HttpServletRequest request, @ModelAttribute AdminUser user) {
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_USER);
				if (adminUser.getRole_id() != 0) {
					return new ResultMessages(StatusCode.NOT_AUTH, "用户无权限！").toString();
				}
				if (user.getPassword() == null || user.getPassword().equals("")) {
					return new ResultMessages(StatusCode.PASSWORD_IS_NULL, "请输入密码！").toString();
				}
				String password = EncryptUtil.encodeByMD5(user.getPassword() + Constant.LOGIN_SECRETKEY);
				user.setPassword(password);
				user.setStatus((short) 1);
				user.setModify_time(new Date());
				int result = iUserService.updateAdminUser(user);
				return new ResultMessages(StatusCode.OK, "请求成功").toString();
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
