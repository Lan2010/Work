package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.DevTaskDetail;
import core.pojo.Shell;
import core.pojo.ShellTask;

public interface ShellMapper {

	/**
	 * 保存脚本信息
	 * 
	 * @param shell
	 * @return
	 */
	Integer saveShell(Shell shell) throws SQLException;

	/**
	 * 脚本列表
	 * 
	 * @param map
	 * @return
	 */
	List<Shell> listShell(Map<String, Object> map) throws SQLException;

	/**
	 * 脚本总数
	 * 
	 * @param shell
	 * @return
	 */
	Integer selectShellCount(Shell shell) throws SQLException;

	/**
	 * 删除脚本
	 * 
	 * @param shellId
	 * @return
	 */
	Integer deleteShell(Integer shellId) throws SQLException;

	/**
	 * 根据ID获取脚本信息
	 * 
	 * @param shellId
	 * @return
	 */
	Shell selectShellById(Integer shellId) throws SQLException;

	/**
	 * 保存任务信息
	 * 
	 * @param task
	 * @return
	 */
	Integer addTask(ShellTask task) throws SQLException;

	/**
	 * 更新某个任务设备完成数
	 * 
	 * @return
	 */
	Integer updateCompleteDev() throws SQLException;

	/**
	 * 分页查询任务列表
	 * 
	 * @param map
	 * @return
	 */
	List<ShellTask> listTask(Map<String, Object> map) throws SQLException;

	/**
	 * 任务总数
	 * 
	 * @param task
	 * @return
	 */
	Integer selectTaskCount(ShellTask task) throws SQLException;

	/**
	 * 某个任务详情
	 * 
	 * @param taskId
	 * @return
	 */
	ShellTask queryTaskDetailById(Integer taskId) throws SQLException;

	/**
	 * 脚本名称列表
	 * 
	 * @return
	 */
	List<Shell> listShellName() throws SQLException;

	Integer checkExit(String shellName) throws SQLException;

	/**
	 * 查看某个设备任务详情
	 * 
	 * @param ipkTaskId
	 * @return
	 */
	List<DevTaskDetail> listDevTaskDetail(Map<String, Object> map) throws SQLException;

	/**
	 * 任务设备总数
	 * 
	 * @param ipkTaskId
	 * @return
	 */
	Integer selectDevTaskDetailCount(Integer taskId) throws SQLException;

}
