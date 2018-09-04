package core.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import core.pojo.Condition;
import core.pojo.Device;
import core.pojo.Ipk;
import core.pojo.IpkName;
import core.pojo.IpkTask;
import core.pojo.Page;

/**
 * 
 * @Description:插件
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public interface IpkService {
	/**
	 * 插件名下拉列表
	 * 
	 * @return
	 */
	List<IpkName> listIpkName() throws SQLException;

	/**
	 * 上传插件
	 * 
	 * @param file
	 * @param path
	 * @return
	 */
	String uploadIpk(CommonsMultipartFile file, String path) throws IOException;

	/**
	 * 数据库新增插件信息
	 * 
	 * @param ipk
	 * @return
	 * @throws SQLException
	 */
	Integer saveIpk(Ipk ipk) throws SQLException;

	/**
	 * 插件列表
	 * 
	 * @param ipk
	 * @param page
	 * @return
	 */
	List<Ipk> listIpk(Ipk ipk, Page page) throws SQLException;

	/**
	 * 插件总数
	 * 
	 * @param ipk
	 * @return
	 */
	Integer selectIpkCount(Ipk ipk) throws SQLException;

	/**
	 * 删除插件
	 * 
	 * @param ipkId
	 * @return
	 */
	Integer deleteIpk(Integer ipkId) throws SQLException;

	/**
	 * 安装插件任务
	 * 
	 * @param ipkTask
	 * @return
	 */
	Integer addIpkTask(IpkTask ipkTask) throws SQLException;

	/**
	 * 检查该插件是否已存在该版本
	 * 
	 * @param ipk
	 * @return
	 */
	Integer checkExit(Ipk ipk) throws SQLException;

	/**
	 * 某个插件的所有版本
	 * 
	 * @param ipkNameId
	 * @return
	 * @throws SQLException
	 */
	List<String> queryVersion(Integer ipkNameId) throws SQLException;

	/**
	 * 某个插件的设备列表
	 * 
	 * @param ipk
	 * @param page
	 * @param device
	 * @return
	 */
	List<Device> listDev(Ipk ipk, Page page, Device device) throws SQLException;

	/**
	 * 某个插件的设备总数
	 * 
	 * @param ipk
	 * @param device
	 * @return
	 */
	Integer selectDevCount(Ipk ipk, Device device) throws SQLException;

	/**
	 * 某个插件刷选的所有设备
	 * 
	 * @param ipk
	 * @param page
	 * @param device
	 * @return
	 */
	List<Integer> AllDev(IpkTask ipkTask, Condition condition) throws SQLException;

	/**
	 * 发送插件任务到接入层
	 * 
	 * @param ipkTask
	 * @param devnums
	 * @return
	 */
	String sendDevTask(IpkTask ipkTask, List<?> devnums) throws SQLException;

	/**
	 * 根据ID查找插件
	 * 
	 * @param ipkId
	 * @return
	 * @throws SQLException
	 */
	Ipk selectIpkById(Integer ipkId) throws SQLException;

	/**
	 * 卸载插件--某个插件的设备列表
	 * @param ipk
	 * @param page
	 * @param device
	 * @return
	 * @throws SQLException
	 */
	List<Device> listDevByRemove(Ipk ipk, Page page, Device device) throws SQLException;

	/**
	 *  卸载插件--某个插件的设备总数
	 * @param ipk
	 * @param device
	 * @return
	 * @throws SQLException
	 */
	Integer selectDevCountByRemove(Ipk ipk, Device device) throws SQLException;

	List<Integer> AllDevByRemove(IpkTask ipkTask, Condition condition)throws SQLException;

}
