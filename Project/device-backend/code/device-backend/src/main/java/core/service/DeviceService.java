package core.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import core.pojo.Condition;
import core.pojo.Device;
import core.pojo.Page;
import core.pojo.ParamsAppoint;
import io.nats.client.Message;

/**
 * @author dev-jin
 * @date 2018年6月16日
 */
public interface DeviceService {
	
	public List<Map<String, Object>> getDevice(Device device, Page page,Integer onlineStatus,Integer isBind) throws SQLException;
	
	public Integer getDeviceCount(Device device,Integer onlineStatus,Integer isBind) throws SQLException;
	
	public Integer addDevice(Device device)throws SQLException;
	
	public Device getNumber(String number)throws SQLException;
	
	public Device getDeviceById(Integer id)throws SQLException;
	
	public void updateDevice(Device device) throws SQLException;
	
	public Integer importDevice(MultipartFile file,HttpServletRequest request) throws Exception;
	
	/**
	 * 处理消息队列的内容，更新入库
	 * @param message 从消息队列nats中订阅的消息
	 * 格式：{
     * 	"id": 12,
     * 	"createTime": 1530691812831, //消息生成时间
     * 	"platformFrom": "WKWXAPP",
     * 	"clientPlatformType": "WKWXAPP",
     * 	"mobile": "1388888888",//绑定的手机号，即用户账号
     * 	"deviceId": "d_id01",
     * 	"deviceType": "d_type",
     * 	"deviceMac": "d_mac",
     * 	"deviceIp": "d_ip",
     * 	"deviceModel": "d_mo",
     * 	"deviceOperType": "d_p",
     * 	"operationTime": 1530691812832, //绑定或解绑的操作时间
     * 	"operationType": 1【操作类型(0=解除绑定，1=绑定)】
     * }
	 * @throws SQLException
	 */
	public void updateBind(Message message);
	
	
	/**
	 * 获取设备可配置参数信息
	 * @return
	 * @throws SQLException
	 */
	List<ParamsAppoint> getParamsAppoint()throws SQLException;

	/**
	 * 根据条件获取所有设备ID
	 * @param condition
	 * @return
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
