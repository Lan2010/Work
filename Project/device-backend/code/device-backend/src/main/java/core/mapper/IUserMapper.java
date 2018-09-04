package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.AdminUser;

public interface IUserMapper {
	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param user_name
	 * @return
	 * @throws Exception
	 */
	AdminUser getAdminUserByName(String user_name) throws SQLException;

	/**
	 * 更具用户表ID查询用户信息
	 * 
	 * @param user_id
	 * @return
	 * @throws SQLException
	 */
	AdminUser getAdminUserByID(Integer user_id) throws SQLException;

	/**
	 * 
	 * @param adminUser
	 * @return
	 * @throws SQLException
	 */
	Integer insertAdminUser(AdminUser adminUser) throws SQLException;

	Integer updateAdminUser(AdminUser adminUser) throws SQLException;

	/**
	 * 获取用户列表
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<AdminUser> listUser(Map<String, Object> map) throws SQLException;

	/**
	 * 查询用户总数
	 * @param user 
	 * 
	 * @return
	 */
	Integer selectUserCount(AdminUser user) throws SQLException;

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	Integer addUser(AdminUser user) throws SQLException;

	/**
	 * 检查用户是否已存在
	 * 
	 * @param user_name
	 * @return
	 */
	Integer checkExit(String user_name) throws SQLException;

}
