package core.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.Condition;
import core.pojo.DevTaskDetail;
import core.pojo.Device;
import core.pojo.Firmware;
import core.pojo.FirmwareName;
import core.pojo.FirmwareTask;
import core.pojo.Page;

/**
 * 
 * @Description:固件
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public interface FirmwareService {
	/**
	 * 固件名下拉列表
	 * 
	 * @return
	 */
	List<FirmwareName> listFirmwareName() throws SQLException;

	/**
	 * 检查该固件是否已存在该版本
	 * 
	 * @param ipk
	 * @return
	 */
	Integer checkExit(Firmware firmware) throws SQLException;

	/**
	 * 数据库新增固件信息
	 * 
	 * @param firmware
	 * @return
	 * @throws SQLException
	 */
	Integer saveFirmware(Firmware firmware) throws SQLException;

	/**
	 * 固件列表
	 * 
	 * @param firmware
	 * @param page
	 * @return
	 * @throws SQLException
	 */
	List<Firmware> listFirmware(Firmware firmware, Page page) throws SQLException;

	/**
	 * 固件总数
	 * 
	 * @param firmware
	 * @return
	 * @throws SQLException
	 */
	Integer selectFirmwareCount(Firmware firmware) throws SQLException;

	/**
	 * 根据ID查找固件
	 * 
	 * @param firmwareId
	 * @return
	 * @throws SQLException
	 */
	Firmware selectFirmwareById(Integer firmwareId) throws SQLException;

	/**
	 * 删除固件
	 * 
	 * @param firmwareId
	 * @return
	 * @throws SQLException
	 */
	Integer deleteFirmware(Integer firmwareId) throws SQLException;

	/**
	 * 某个固件的所有版本
	 * 
	 * @param ipkNameId
	 * @return
	 * @throws SQLException
	 */
	List<String> queryVersion(Integer firmwareNameId) throws SQLException;

	/**
	 * 某个固件的设备列表
	 * 
	 * @param firmware
	 * @param page
	 * @param device
	 * @return
	 * @throws SQLException
	 */
	List<Device> listDev(Firmware firmware, Page page, Device device) throws SQLException;

	/**
	 * 某个固件的设备总数
	 * 
	 * @param firmware
	 * @param device
	 * @return
	 * @throws SQLException
	 */
	Integer selectDevCount(Firmware firmware, Device device) throws SQLException;

	/**
	 * 固件任务
	 * 
	 * @param firmwareTask
	 * @return
	 * @throws SQLException
	 */
	Integer addFirmwareTask(FirmwareTask firmwareTask) throws SQLException;

	/**
	 * 某个固件刷选的所有设备
	 * 
	 * @param firmwareTask
	 * @param condition
	 * @return
	 * @throws SQLException
	 */
	List<Integer> AllDev(FirmwareTask firmwareTask, Condition condition) throws SQLException;

	/**
	 * 发送固件任务到接入层
	 * 
	 * @param firmwareTask
	 * @param devnums
	 * @return
	 * @throws SQLException
	 */
	String sendDevTask(FirmwareTask firmwareTask, List<?> devnums) throws SQLException;

	/**
	 * 根据设备编号获取对应固件
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Firmware selectFirmwareByDevNum(Map<String, Object> map) throws SQLException;

	/**
	 * 更新固件任务
	 * 
	 * @return
	 * @throws SQLException
	 */
	Integer updateCompleteDev() throws SQLException;

	/**
	 * 固件任务列表
	 * 
	 * @param firmwareTask
	 * @param page
	 * @return
	 * @throws SQLException
	 */
	List<FirmwareTask> listFirmwareTask(FirmwareTask firmwareTask, Page page) throws SQLException;

	/**
	 * 固件任务总数
	 * 
	 * @param firmwareTask
	 * @return
	 * @throws SQLException
	 */
	Integer selectFirmwareTaskCount(FirmwareTask firmwareTask) throws SQLException;

	/**
	 * 根据ID查看固件任务详情
	 * 
	 * @param firmwareTaskId
	 * @return
	 * @throws SQLException
	 */
	FirmwareTask queryFirmwareTaskById(Integer firmwareTaskId) throws SQLException;

	/**
	 * 设备完成任务详情
	 * @param firmwareTaskId
	 * @param page
	 * @return
	 * @throws SQLException
	 */
	List<DevTaskDetail> listDevTaskDetail(Integer firmwareTaskId, Page page) throws SQLException;

	/**
	 * 设备完成任务总数
	 * @param firmwareTaskId
	 * @return
	 * @throws SQLException
	 */
	Integer selectDevTaskDetailCount(Integer firmwareTaskId) throws SQLException;

}
