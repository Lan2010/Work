package core.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import core.mapper.IPermissionMapper;
import core.pojo.AdminUser;
import core.pojo.MenuInfo;
import core.service.IPermissionService;


/**
 * 用户权限业务层
 * 
 * @author dev-lan
 * @date 2018年5月17日
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

	@Autowired
	private IPermissionMapper iPermissionMapper;

	/**
	 * 根据用户获取菜单json
	 */
	@Override
	public JSONObject getMenuJsonByUser(AdminUser adminUser) throws SQLException {
		// 获取用户menulist
		List<MenuInfo> menulist = queyMenuByUserName(adminUser);
		if (null == menulist || menulist.isEmpty()) {
			return null;
		}
		// 存放对象
		JSONObject jObject = new JSONObject();
		// 数组
		JSONArray jArray = new JSONArray();
		// 最后的菜单list
		List<MenuInfo> newmenuList = new ArrayList<MenuInfo>();
		try {
			// 一级菜单
			for (MenuInfo menu : menulist) {
				if (menu.getParent_id().equals(0)) {
					newmenuList.add(menu);
				}
			}
			// 为一级菜单设置子菜单
			for (MenuInfo menu : newmenuList) {
				menu.setChildMenus(getChild(menu.getMenu_id(), menulist));
				// 内层json
				JSONObject innerjObject = new JSONObject();
				// 子层json数组
				JSONArray innerjArray = new JSONArray();

				for (MenuInfo innermenu : menu.getChildMenus()) {
					// 子层json
					JSONObject childjObject = new JSONObject();
					childjObject.put("name", innermenu.getMenu_name());
					childjObject.put("url", innermenu.getMenu_path());
					childjObject.put("icon", innermenu.getIcon());
					innerjArray.add(childjObject);
				}
				innerjObject.put("name", menu.getMenu_name());
				innerjObject.put("url", menu.getMenu_path());
				innerjObject.put("icon", menu.getIcon());
				innerjObject.put("sub", innerjArray);
				jArray.add(innerjObject);
			}
			jObject.put("code", 0);
			jObject.put("msg", "请求成功");
			jObject.put("data", jArray);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return jObject;
	}

	/**
	 * 根据用户roleId查询菜单list
	 */
	@Override
	public List<MenuInfo> queyMenuByUserName(AdminUser adminUser) throws SQLException {
		// 根据role获取菜单
		Short role = adminUser.getRole_id();
		List<MenuInfo> menus = iPermissionMapper.queryMenusByRole(role);

		// 去重菜单数组
		LinkedHashSet<MenuInfo> set = new LinkedHashSet<MenuInfo>(menus.size());
		set.addAll(menus);
		menus.clear();
		menus.addAll(set);

		return menus;

	}

	/**
	 * 获取子菜单
	 * 
	 * @param parent_id
	 * @param menulist
	 * @return
	 */
	private List<MenuInfo> getChild(Integer parent_id, List<MenuInfo> menulist) {
		// 子菜单
		List<MenuInfo> childList = new ArrayList<>();
		for (MenuInfo menu : menulist) {
			// 遍历所有节点，将父菜单id与传过来的id比较
			if (menu.getParent_id() != null & menu.getParent_id().equals(parent_id)) {
				childList.add(menu);
			}
		}
		if (childList.size() == 0) {
			return null;
		}
		return childList;
	}

}
