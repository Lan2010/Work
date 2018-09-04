package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.DevTaskDetail;
import core.pojo.IpkTask;

public interface IpkTaskMapper {

	/**
	 * 分页查询插件任务
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<IpkTask> listIpkTask(Map<String, Object> map) throws SQLException;

	/**
	 * 插件任务总数
	 * 
	 * @param ipkTask
	 * @return
	 */
	Integer selectIpkTaskCount(IpkTask ipkTask) throws SQLException;

	/**
	 * 更新某个任务设备完成数
	 * 
	 * @return
	 */
	Integer updateCompleteDev();

	/**
	 * 设备完成任务详情
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<DevTaskDetail> listDevTaskDetail(Map<String, Object> map) throws SQLException;

	/**
	 * 任务设备总数
	 * 
	 * @param ipkTaskId
	 * @return
	 * @throws SQLException
	 */
	Integer selectDevTaskDetailCount(Integer ipkTaskId) throws SQLException;

	/**
	 * 查看某个任务详情
	 * @param ipkTaskId
	 * @return
	 * @throws SQLException
	 */
	IpkTask queryIpkTaskById(Integer ipkTaskId)throws SQLException;

}
