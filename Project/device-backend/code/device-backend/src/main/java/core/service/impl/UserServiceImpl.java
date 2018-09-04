package core.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.common.StatusCode;
import core.component.NatsComponent;
import core.mapper.IUserMapper;
import core.mapper.LoginLogMapper;
import core.pojo.AdminUser;
import core.pojo.LoginLog;
import core.pojo.Page;
import core.pojo.ResultMessages;
import core.service.IUserService;
import core.service.LoginLogService;
import core.util.CommonUtils;
import core.util.EncryptUtil;

/**
 * 用户管理业务层
 * 
 * @author dev-jin
 * @date 2018年6月20日
 */
@Service
public class UserServiceImpl implements IUserService {
	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Resource
	private IUserMapper iUserDao;
	@Resource
	public NatsComponent natsComponent;
	@Resource
	public LoginLogMapper loginLogMapper;
	@Resource
	public LoginLogService loginLogService;

	@Override
	public String getAdminUserByName(AdminUser adminUser, HttpServletRequest request, HttpServletResponse response)
			throws SQLException {
		HttpSession session = request.getSession();
		String password = adminUser.getPassword();
		String user_name = adminUser.getUser_name();
		// String validateCode=request.getParameter("validateCode");
		if (password == null || "".equals(password)) {
			return new ResultMessages(StatusCode.PASSWORD_IS_NULL, "请输入密码").toString();
		}
		if (user_name == null || "".equals(user_name)) {
			return new ResultMessages(StatusCode.ACCOUNT_IS_NULL, "请输入用户名").toString();
		}
		// if (session!=null &&
		// !validateCode.equals(session.getAttribute(AppConstant.SESSION_VALIDATION_CODE)))
		// {
		// return new ResultMessages(StatusCode.ERROR_VALIDATION_CODE,
		// "验证码不正确，请重新输入").toString();
		// }
		try {
			password = EncryptUtil.encodeByMD5(password + Constant.LOGIN_SECRETKEY);// 数据库存储的密码；
			adminUser = iUserDao.getAdminUserByName(user_name);
			if (adminUser != null) {// 从本地数据库查询用户信息
				String passwordDB = adminUser.getPassword();
				if (password.equals(passwordDB)) {
					Cookie cookie_user;
					adminUser.setPassword(null);
					session.setAttribute(Constant.SESSION_USER, adminUser);
					String nickName = adminUser.getNickname();
					if (nickName == null || nickName.length() <= 0) {
						nickName = adminUser.getUser_name();
					}
					cookie_user = new Cookie("nickName", URLEncoder.encode(nickName, "utf-8"));
					cookie_user.setMaxAge(Constant.COOKIE_MAX_AGE);
					response.addCookie(cookie_user);
					response.addHeader("Set-Cookie", "nickName=" + URLEncoder.encode(nickName, "utf-8")
							+ ";Path=/;Max-Age=" + Constant.COOKIE_MAX_AGE + ";");// 设置cookie
					// 记录登录日志
					logLogin(adminUser);
					if (adminUser.getStatus() != 1) {
						return new ResultMessages(StatusCode.DISABLE_USER, "登录失败,用户被禁用!").toString();
					}
					// 提供登录信息给运营平台
					loginLogService.login4oms(request, 1);
					return new ResultMessages(StatusCode.OK, "登录成功").toString();
				}
			}
			// else {
			// JSONObject json = new JSONObject();
			// json.put("ipAddress", Util.getIpAddr(request));
			// json.put("username", user_name);
			// json.put("password",password);
			// String url=QLY_SIGN_IN_URL+"/2/account/qly-signin";
			// String result = HttpUtils.httpPostJSON(url,json.toJSONString());
			// JSONObject resultJSON = JSONObject.parseObject(result);
			// if(resultJSON != null && (Integer)resultJSON.get("code") == 0) {
			// JSONObject data = (JSONObject)resultJSON.get("data");
			// adminUser = new AdminUser();
			// String nickName = (String) data.get("nickName");
			// adminUser.setUser_name(user_name);
			// adminUser.setNickname(nickName);
			// adminUser.setRole_id((short)1);
			// session.setAttribute(AppConstant.SESSION_USER, adminUser);
			// response.addHeader("Set-Cookie","nickName="+URLEncoder.encode(adminUser.getNickname(),
			// "utf-8")+";Path=/;Max-Age="+AppConstant.COOKIE_MAX_AGE+";");//设置cookie
			// }
			// return result;
			// }
			return new ResultMessages(StatusCode.ACCOUNT_OR_PWD_ERROR, "用户名或密码不正确").toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("不支持的编码.", e);
			return new ResultMessages(StatusCode.ERROR, "不支持的编码").toString();
		}
	}

	/**
	 * 记录登录日志
	 * 
	 * @param adminUser
	 */
	private void logLogin(AdminUser adminUser) {
		try {
			LoginLog loginLog = new LoginLog();
			loginLog.setLoginTime(new Date());
			loginLog.setNickName(adminUser.getNickname());
			loginLog.setUserName(adminUser.getUser_name());
			loginLogMapper.addLoginLog(loginLog);
		} catch (SQLException e) {
			// return new ResultMessages(StatusCode.ERROR, "登录失败,录入日志发生错误").toString();
			e.printStackTrace();
			log.error("登录失败,录入日志发生错误.", e);
		}
	}

	public static void main(String[] args) throws Exception {
		// String name="teng";
		// String password="123456";
		// password=EncryptUtil.encodeByMD5(password);
		// name=EncryptUtil.encodeByMD5(name);
		// password = EncryptUtil.encodeByMD5(password+name);
		// System.out.println("前端密码："+password);
		// System.out.println("数据库密码："+EncryptUtil.encodeByMD5(password+AppConstant.LOGIN_SECRETKEY));
		// JSONObject json = new JSONObject();
		// String ipAddress = Util.getIpAddr(request);
		// json.put("ipAddress", "0.0.0.0");
		// json.put("username", "luoyt");
		// json.put("password",
		// EncryptUtil.encodeByMD5(EncryptUtil.encodeByMD5("555555")+EncryptUtil.encodeByMD5("luoyt")));
		// String url="http://qlyapi.100msh.com/2/account/qly-signin";
		// String result = HttpUtils.httpPostJSON(url,json.toJSONString());
		// System.out.println(result);
		System.out.println(EncryptUtil.encryptBASE64("sischain".getBytes()).replaceAll("\r\n", ""));
		System.out.println(EncryptUtil.encodeByMD5("c2lzY2hhaW4=" + Constant.LOGIN_SECRETKEY));
	}

	@Override
	public Integer allotAccount(AdminUser adminUser) throws SQLException {
		String password = EncryptUtil.encodeByMD5(adminUser.getPassword() + Constant.LOGIN_SECRETKEY);
		adminUser.setPassword(password);
		// TODO 分配角色
		iUserDao.insertAdminUser(adminUser);
		return null;
	}

	@Override
	public AdminUser getUserByName(AdminUser adminUser) throws SQLException {
		String userName = adminUser.getUser_name();
		adminUser = iUserDao.getAdminUserByName(userName);
		return adminUser;
	}

	@Override
	public Integer updateAdminUser(AdminUser adminUser) throws SQLException {
		return iUserDao.updateAdminUser(adminUser);
	}

	@Override
	public int selectUserCount(AdminUser user) throws SQLException {
		return iUserDao.selectUserCount(user);
	}

	@Override
	public List<AdminUser> listUser(AdminUser user, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(user);
		map.put("pageSize", page.getPageSize());
		map.put("start", page.getStart());
		return iUserDao.listUser(map);

	}

	private Map<String, Object> setCondition(AdminUser user) {
		if (user != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("user_name", user.getUser_name());
			condition.put("nickname", user.getNickname());
			condition.put("cell_phone", user.getCell_phone());
			condition.put("status", user.getStatus());
			condition.put("create_time", user.getCreate_time());
			condition.put("role_id", user.getRole_id());
			condition.put("modify_time", user.getModify_time());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public Integer addUser(AdminUser user, HttpServletRequest request) throws SQLException {
		// 提供注册信息给运营平台
		register4oms(user, request);
		return iUserDao.insertAdminUser(user);
	}

	@Override
	public Integer checkExit(String user_name) throws SQLException {
		return iUserDao.checkExit(user_name);

	}

	/**
	 * 发布注册消息到nats
	 * 
	 * @param user
	 * @param request
	 */
	@Override
	public void register4oms(AdminUser user, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("id", CommonUtils.randomID(8));
		json.put("createTime", System.currentTimeMillis());
		json.put("platformFrom", Constant.PLATFORM_NAME);
//		json.put("clientPlatformType", Constant.PLATFORM_NAME);
		json.put("mobile", user.getCell_phone());
		// json.put("email","");
		// json.put("realName", user.getUser_name());
		// json.put("idCard", "");
		json.put("nickName", user.getNickname());
		json.put("userFromType", "RANDOM");
		// json.put("avatar", "");
		json.put("userOperType", "reg");
		// json.put("wxID", "");
		// json.put("qqID", "");
		// json.put("sinaWeiBoID","");
		json.put("regTime", System.currentTimeMillis());
		try {
			json.put("ip", CommonUtils.getIpAddr(request));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			log.error("未知主机.", e);
		} finally {
			json.put("ip", "0.0.0.0");
			natsComponent.publish4oms("oms.subject.user.basic.info", json.toJSONString());
		}
	}
	
	
}
