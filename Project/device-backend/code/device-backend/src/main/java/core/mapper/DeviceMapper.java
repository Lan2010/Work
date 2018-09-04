package core.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import core.pojo.Condition;
import core.pojo.Device;

/**
 * 充电宝设备的信息Mapper层
 * @author dev-jin
 * @date 2018年6月16日
 */
public interface DeviceMapper {

	public List<Map<String, Object>> getDevice(Map<String,Object> map)throws SQLException;
	
	public Integer getDeviceCount(Map<String, Object> map)throws SQLException;

	public Integer addDevice(Device device)throws SQLException;
	
	public Device getNumber(String number)throws SQLException;
	
	public Device getDeviceById(Integer id)throws SQLException;
	
	public void updateDevice(Device device)throws SQLException;

	public void updateBind(Device device)throws SQLException;
	
	/**
	 * 所有有效设备数
	 * @return
	 * @throws SQLException
	 */
	public Integer countAll()throws SQLException;
	
	/**
	 * 统计上线和未上线的设备数
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String,Object>> countOnlined()throws SQLException;
	
	/**
	 * 绑定的设备数 
	 * @return
	 * @throws SQLException
	 */
	public Integer countBind()throws SQLException;
	
	/**
	 * 绑定的设备当前在线数 
	 * @return
	 * @throws SQLException
	 */
	public Integer countBindOnline()throws SQLException;
	
	/**
	 * 未绑定的设备数
	 * @return
	 * @throws SQLException
	 */
	public Integer countUnbound()throws SQLException;
	
	/**
	 * 未绑定设备当前在线数
	 * @return
	 * @throws SQLException
	 */
	public Integer countUnboundOnline()throws SQLException;
	
	/**
	 * 未绑定设备未上线数
	 * @return
	 * @throws SQLException
	 */
	public Integer countUnboundUnonlined()throws SQLException;

	/**
	 * 根据设备编号检查设备是否存在
	 * @param devnum
	 * @return
	 */
	public Integer checkDevExit(String devNum)throws SQLException;

	/**
	 *  根据条件获取所有设备ID
	 * @param condition
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> allDevId(Condition condition)throws SQLException;

	/**
	 * 修改设备密码
	 * @param device
	 * @return
	 * @throws SQLException
	 */
	public Integer changePasswd(Device device)throws SQLException;
}
