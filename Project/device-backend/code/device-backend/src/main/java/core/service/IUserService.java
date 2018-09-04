package core.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.pojo.AdminUser;
import core.pojo.Page;

public interface IUserService {
	/**
	 * 根据用户名和密码获取用户信息
	 * @param user_name
	 * @param password 前端加盐策略：md5(用户输入的密码+md5(user_name))
	 * @return
	 * @throws SQLException
	 */
	String getAdminUserByName(AdminUser userInfo,HttpServletRequest request,HttpServletResponse response) throws SQLException;

	/**
	 * 分配后台管理人员账户
	 * @param adminUser
	 * @return
	 */
	Integer allotAccount(AdminUser adminUser)throws SQLException;
	
	AdminUser getUserByName(AdminUser adminUser) throws SQLException;
	
	public Integer updateAdminUser(AdminUser adminUser) throws SQLException;

	/**
	 * 用户总数
	* @return
	 * @throws SQLException 
	 */
	int selectUserCount(AdminUser user) throws SQLException;

	/**
	 * 新增用户
	* @param user
	 * @param request 
	* @return
	 * @throws SQLException 
	 */
	Integer addUser(AdminUser user, HttpServletRequest request) throws SQLException;

	/**
	 * 检查用户是否已存在
	* @param user_name
	* @return
	* @throws SQLException
	 */
	Integer checkExit(String user_name) throws SQLException;

	/**
	 *  用户列表
	 * @param user
	 * @param page
	 * @return
	 */
	List<AdminUser> listUser(AdminUser user, Page page) throws SQLException;



	/**
	 * 发布注册消息到nats
	 * @param user
	 * @param request
	 */
	void register4oms(AdminUser user, HttpServletRequest request);

	
}
