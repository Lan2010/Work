package core.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import core.pojo.Condition;
import core.pojo.DevTaskDetail;
import core.pojo.Device;
import core.pojo.Ipk;
import core.pojo.IpkName;
import core.pojo.IpkTask;
import core.pojo.Page;
import core.pojo.Shell;

/**
 * 
 * @Description:插件
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public interface IpkTaskService {

	/**
	 * 分页查询插件任务
	 * 
	 * @param ipkTask
	 * @param page
	 * @return
	 */
	List<IpkTask> listIpkTask(IpkTask ipkTask, Page page) throws SQLException;

	/**
	 * 插件任务
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
	Integer updateCompleteDev() throws SQLException;

	/**
	 * 设备完成任务详情
	 * 
	 * @param ipkTaskId
	 * @param page
	 * @return
	 */
	List<DevTaskDetail> listDevTaskDetail(Integer ipkTaskId, Page page) throws SQLException;

	/**
	 * 任务设备总数
	 * 
	 * @param ipkTaskId
	 * @return
	 */
	Integer selectDevTaskDetailCount(Integer ipkTaskId) throws SQLException;
	
	/**
	 * 查看某个插件任务详情
	 * @param ipkTaskId
	 * @return
	 */
	IpkTask queryIpkTaskById(Integer ipkTaskId)throws SQLException;

}
