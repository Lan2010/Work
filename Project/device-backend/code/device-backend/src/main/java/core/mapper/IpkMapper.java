package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.Device;
import core.pojo.Ipk;
import core.pojo.IpkName;
import core.pojo.IpkTask;

public interface IpkMapper {

	/**
	 * 插入插件信息
	 * 
	 * @param ipk
	 * @return
	 * @throws SQLException
	 */
	Integer saveIpk(Ipk ipk) throws SQLException;

	/**
	 * 插件列表
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Ipk> listIpk(Map<String, Object> map) throws SQLException;

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
	 * 新增插件任务
	 * 
	 * @param ipkTask
	 * @return
	 * @throws SQLException
	 */
	Integer addIpkTask(IpkTask ipkTask) throws SQLException;

	/**
	 * 插件名列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	List<IpkName> listIpkName() throws SQLException;

	/**
	 * 检查该插件是否已存在该版本
	 * 
	 * @param ipk
	 * @return
	 * @throws SQLException
	 */
	Integer checkExit(Ipk ipk) throws SQLException;

	/**
	 * 某个插件的所有版本
	 * 
	 * @param ipkNameId
	 * @return
	 */
	List<String> queryIpkVersion(Integer ipkNameId) throws SQLException;

	/**
	 * 某个插件的设备列表
	 * 
	 * @param map
	 * @return
	 */
	List<Device> listDev(Map<String, Object> map) throws SQLException;

	/**
	 * 某个插件的设备总数
	 * 
	 * @param ipk
	 * @param device
	 * @return
	 */
	Integer selectDevCount(Map<String, Object> map) throws SQLException;

	List<Integer> AllDev(Map<String, Object> map) throws SQLException;

	/**
	 * 根据设备编号获取对应ipk
	 * 
	 * @param map
	 * @return
	 */
	Ipk selectIpkByDevNum(Map<String, Object> map) throws SQLException;

	/**
	 * 根据设备ID获取对应ipk
	 * 
	 * @param map
	 * @return
	 */
	Ipk selectIpkByDevId(Map<String, Object> map) throws SQLException;

	/**
	 * 根据插件名ID获取插件名
	 * 
	 * @param ipkNameId
	 * @return
	 * @throws SQLException
	 */
	String getIpkNameByID(Integer ipkNameId) throws SQLException;

	/**
	 * 根据ID获取插件
	 * 
	 * @param ipkId
	 * @return
	 * @throws SQLException
	 */
	Ipk selectIpkById(Integer ipkId) throws SQLException;

	List<Device> listDevByRemove(Map<String, Object> map) throws SQLException;

	Integer selectDevCountByRemove(Map<String, Object> map) throws SQLException;

	List<Integer> AllDevByRemove(Map<String, Object> map) throws SQLException;

}
