package core.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import core.pojo.LoginLog;
import core.pojo.Page;

/**
 * @author dev-jin
 * @date 2018年6月20日
 */
public interface LoginLogService {
	
	public List<LoginLog> getLoginLog(LoginLog loginLog,Page page) throws SQLException;
	
	public Integer getLoginLogCount(LoginLog loginLog) throws SQLException;
	
	public Integer addLoginLog(LoginLog loginLog)throws SQLException;
	
	void login4oms(HttpServletRequest request,Integer operationType);
	
}
