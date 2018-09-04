package core.service;

import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import core.pojo.AdminUser;
import core.pojo.MenuInfo;


/**
 * 
 *  用户菜单权限管理层
 * @author dev-lan
 * @date 2018年5月18日
 *
 */
public interface IPermissionService {
	/**
	 * 根据用户获取菜单权限
	 * @param adminUser	
	 * @return
	 * @throws SQLException
	 */
	List <MenuInfo> queyMenuByUserName(AdminUser adminUser) throws SQLException;

	/**
	 * 根据用户roleId获取菜单json
	 * @param adminUser	
	 * @return
	 * @throws SQLException
	 */
	JSONObject getMenuJsonByUser(AdminUser adminUser) throws SQLException;

	
}
