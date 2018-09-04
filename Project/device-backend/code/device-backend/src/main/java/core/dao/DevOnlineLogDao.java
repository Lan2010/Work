package core.dao;

import java.util.Map;

public interface DevOnlineLogDao {
	/**
	 * 查询设备上下线日志
	 * @param map
	 */
	Integer countToday(Map<String,Object> map);
	
}
