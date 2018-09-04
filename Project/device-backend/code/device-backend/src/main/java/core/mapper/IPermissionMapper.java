package core.mapper;

import java.sql.SQLException;
import java.util.List;

import core.pojo.MenuInfo;

public interface IPermissionMapper {

	List<MenuInfo> queryMenusByRole(Short role) throws SQLException;

}
