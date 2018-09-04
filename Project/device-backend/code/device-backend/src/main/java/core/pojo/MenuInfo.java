package core.pojo;

import java.util.List;

/**
 * 页面菜单信息
 * 
 * @author dev-lan
 * @date 2018年5月17日
 */
public class MenuInfo {
	private Integer menu_id; // 自增主键
	private String icon; // 图表样式类型
	private String menu_path; // 菜单路径
	private String menu_name; // 菜单名称
	private Integer parent_id; // 父级菜单ID
	private Integer level; // 父级菜单ID
	private List<MenuInfo> childMenus;// 子菜单

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMenu_path() {
		return menu_path;
	}

	public void setMenu_path(String menu_path) {
		this.menu_path = menu_path;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<MenuInfo> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<MenuInfo> childMenus) {
		this.childMenus = childMenus;
	}

	@Override
	public String toString() {
		return "MenuInfo [menu_id=" + menu_id + ", icon=" + icon + ", menu_path=" + menu_path + ", menu_name="
				+ menu_name + ", parent_id=" + parent_id + ", level=" + level + ", childMenus=" + childMenus + "]";
	}

}
