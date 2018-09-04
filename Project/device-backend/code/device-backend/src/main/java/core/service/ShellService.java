package core.service;

import java.sql.SQLException;
import java.util.List;

import core.pojo.Condition;
import core.pojo.DevTaskDetail;
import core.pojo.Page;
import core.pojo.Shell;
import core.pojo.ShellTask;

/**
 * 
 * @Description:插件
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public interface ShellService {

	/**
	 * 保存脚本
	 * 
	 * @param shell
	 * @return
	 */
	Integer saveShell(Shell shell) throws SQLException;

	/**
	 * 脚本列表
	 * 
	 * @param shell
	 * @param page
	 * @return
	 */
	List<Shell> listShell(Shell shell, Page page) throws SQLException;

	/**
	 * 列表总数
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
	 * 根据ID获取插件信息
	 * 
	 * @param shellId
	 * @return
	 * @throws SQLException
	 */
	Shell selectShellById(Integer shellId) throws SQLException;

	/**
	 * 新增任务
	 * 
	 * @param task
	 * @return
	 * @throws SQLException
	 */
	Integer addTask(ShellTask task) throws SQLException;

	/**
	 * 发送设备任务
	 * 
	 * @param task
	 * @param devnums
	 * @return
	 * @throws SQLException
	 */
	String sendDevTask(ShellTask task, List<?> devnums) throws SQLException;

	/**
	 * 更新某个任务设备完成数
	 * 
	 * @return
	 */
	Integer updateCompleteDev() throws SQLException;

	/**
	 * 任务列表
	 * 
	 * @param task
	 * @param page
	 * @return
	 */
	List<ShellTask> listTask(ShellTask task, Page page) throws SQLException;

	/**
	 * 任务总数
	 * 
	 * @param task
	 * @return
	 * @throws SQLException
	 */
	Integer selectTaskCount(ShellTask task) throws SQLException;

	/**
	 * 某个任务详情
	 * 
	 * @param taskId
	 * @return
	 * @throws SQLException
	 */
	ShellTask queryTaskDetailById(Integer taskId) throws SQLException;

	/**
	 * 脚本名字下拉列表
	 * 
	 * @return
	 */
	List<Shell> listShellName() throws SQLException;

	/**
	 * 检查脚本名是否已存在
	 * 
	 * @param shellName
	 * @return
	 */
	Integer checkExit(String shellName) throws SQLException;

	/**
	 * 查看某个设备任务详情
	 * @param ipkTaskId
	 * @return
	 */
	List<DevTaskDetail> listDevTaskDetail(Integer taskId, Page page)throws SQLException;

	/**
	 * 任务设备总数
	 * 
	 * @param ipkTaskId
	 * @return
	 */
	Integer selectDevTaskDetailCount(Integer taskId)throws SQLException;

}
