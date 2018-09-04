package core.service.impl;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.component.NatsComponent;
import core.mapper.LoginLogMapper;
import core.pojo.LoginLog;
import core.pojo.Page;
import core.service.LoginLogService;
import core.util.CommonUtils;

@Service
public class LoginLogServiceImpl implements LoginLogService{
	private static Logger log = LoggerFactory.getLogger(LoginLogServiceImpl.class);
	
	@Resource
	public LoginLogMapper loginLogMapper;
	@Resource
	public NatsComponent natsComponent;  

	
	@Override
	public List<LoginLog> getLoginLog(LoginLog loginLog, Page page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(loginLog);
		map.put("size", page.getPageSize());
		map.put("start", page.getStart());
		List<LoginLog> merchants = loginLogMapper.getLoginLog(map);
		return merchants;
	}
	
	@Override
	public Integer getLoginLogCount(LoginLog loginLog) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(loginLog);
		return loginLogMapper.getLoginLogCount(map);
	}

	@Override
	public Integer addLoginLog(LoginLog loginLog) throws SQLException {
		Integer counts = loginLogMapper.addLoginLog(loginLog);
		return counts;
	}
	
	private Map<String, Object> setCondition(LoginLog loginLog) {
		if (loginLog != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("nick_name", loginLog.getNickName());
			condition.put("id", loginLog.getId());
			condition.put("user_name", loginLog.getUserName());
			condition.put("ip", loginLog.getIp());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}

	
	/**
	 * 发布登录/注销消息到nats
	 * @param request
	 * @param operationType
	 */
	@Override
	public void login4oms(HttpServletRequest request,Integer operationType){
		JSONObject json = new JSONObject();
		json.put("id", CommonUtils.randomID(8));
		json.put("createTime", System.currentTimeMillis());
		json.put("operationTime", System.currentTimeMillis());
		json.put("platformFrom", Constant.PLATFORM_NAME);
		json.put("operationType", operationType);
		try {
			json.put("ip", CommonUtils.getIpAddr(request));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			log.error("未知主机.",e);
		}finally {
			json.put("ip", "0.0.0.0");
			natsComponent.publish4oms("oms.subject.user.login-logout",json.toJSONString());
		}
	}
	
}
