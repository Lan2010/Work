package core.mapper;

import java.sql.SQLException;

import core.pojo.User;

/**
 * 用户管理Mapper层
 * @author dev-teng
 * @date 2018年6月12日
 */
public interface UserMapper {

	/**
	 * 根据openid查询用户信息
	 * @param openid
	 * @return
	 */
	User selectOne(String openid)throws SQLException;;

	/**
	 * 新增用户信息
	 * @param user
	 */
	void insert(User user)throws SQLException;;
	
	/**
	 * 根据主键查询用户信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	User selectOneByID(Integer userId)throws SQLException;


}
