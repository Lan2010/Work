package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.DevTaskDetail;
import core.pojo.Device;
import core.pojo.Firmware;
import core.pojo.FirmwareName;
import core.pojo.FirmwareTask;

public interface FirmwareMapper {

	List<FirmwareName> listFirmwareName() throws SQLException;

	Integer checkExit(Firmware firmware) throws SQLException;

	Integer saveFirmware(Firmware firmware) throws SQLException;

	List<Firmware> listFirmware(Map<String, Object> map) throws SQLException;

	Integer selectFirmwareCount(Firmware firmware) throws SQLException;

	Firmware selectFirmwareById(Integer firmwareId) throws SQLException;

	Integer deleteFirmware(Integer firmwareId) throws SQLException;

	List<String> queryVersion(Integer firmwareNameId) throws SQLException;

	List<Device> listDev(Map<String, Object> map) throws SQLException;

	Integer selectDevCount(Map<String, Object> map) throws SQLException;

	Firmware selectFirmwareByDevNum(Map<String, Object> map) throws SQLException;

	Integer addFirmwareTask(FirmwareTask firmwareTask) throws SQLException;

	Firmware selectFirmwareByDevId(Map<String, Object> map) throws SQLException;

	List<Integer> AllDev(Map<String, Object> map) throws SQLException;

	Integer updateCompleteDev() throws SQLException;

	List<FirmwareTask> listFirmwareTask(Map<String, Object> map) throws SQLException;

	Integer selectFirmwareTaskCount(FirmwareTask firmwareTask) throws SQLException;

	FirmwareTask queryFirmwareTaskById(Integer firmwareTaskId) throws SQLException;

	List<DevTaskDetail> listDevTaskDetail(Map<String, Object> map) throws SQLException;

	Integer selectDevTaskDetailCount(Integer firmwareTaskId) throws SQLException;

}
