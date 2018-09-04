package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.LoginLog;

/**
 * @author dev-jin
 * @date 2018年6月20日
 */
public interface LoginLogMapper {

	public List<LoginLog> getLoginLog(Map<String,Object> map)throws SQLException;
	
	public Integer getLoginLogCount(Map<String, Object> map)throws SQLException;
	
	public Integer addLoginLog(LoginLog loginLog)throws SQLException;
	
	public void updateLoginLog(LoginLog loginLog)throws SQLException;
	
	public LoginLog getOneLoginLog()throws SQLException;
}
