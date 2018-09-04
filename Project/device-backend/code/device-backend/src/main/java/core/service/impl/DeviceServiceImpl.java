package core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.component.NatsComponent;
import core.mapper.DeviceMapper;
import core.mapper.ParamsAppointMapper;
import core.pojo.AdminUser;
import core.pojo.Condition;
import core.pojo.Device;
import core.pojo.DeviceLog;
import core.pojo.Page;
import core.pojo.ParamsAppoint;
import core.service.DeviceLogService;
import core.service.DeviceService;
import core.util.CommonUtils;
import core.util.ExcelUtil;
import core.util.TimeUtil;
import io.nats.client.Message;

@Service
public class DeviceServiceImpl implements DeviceService{
	private static Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);
	@Resource
	private NatsComponent natsComponent;
	@Resource
	public DeviceMapper deviceMapper;
	@Resource
	public ParamsAppointMapper paramsAppointMapper;
	@Resource
	private DeviceLogService deviceLogService;
	@Override
	public List<Map<String, Object>> getDevice(Device device, Page page,Integer onlineStatus,Integer isBind) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(device);
		map.put("size", page.getPageSize());
		map.put("start", page.getStart());
		map.put("onlineStatus", onlineStatus);
		map.put("isBind", isBind);
		List<Map<String, Object>> merchants = deviceMapper.getDevice(map);
		return merchants;
	}
	
	@Override
	public Integer getDeviceCount(Device device,Integer onlineStatus,Integer isBind) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = setCondition(device);
		map.put("onlineStatus", onlineStatus);
		map.put("isBind", isBind);
		return deviceMapper.getDeviceCount(map);
	}
	
	private Map<String, Object> setCondition(Device device) {
		if (device != null) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("dev_id", device.getId());
			condition.put("dev_number", device.getNumber());
			condition.put("dev_model", device.getModel());
			condition.put("belong_unit_id", device.getBelongUnitId());
			condition.put("product_time", device.getProductTtime());
			condition.put("mac", device.getMac());
			condition.put("add_time", device.getAddTime());
			condition.put("bind_account", device.getBindAccount());
			condition.put("bind_time", device.getBindTime());
			condition.put("is_onlined", device.getIsOnlined());
			return condition;
		} else {
			return new HashMap<String, Object>();
		}
	}
	
	@Override
	public Integer addDevice(Device device) throws SQLException {
		Integer counts = deviceMapper.addDevice(device);
		if(counts>0) {
			//发布设备登记消息到nats
			regDev4oms(device);
		}
		return counts;
	}

	/**
	 * 发布设备登记消息到nats
	 * @param device
	 */
	private void  regDev4oms(Device device){
		JSONObject json = new JSONObject();
		json.put("id", CommonUtils.randomID(8));
		json.put("createTime", System.currentTimeMillis());
		json.put("platformFrom", Constant.PLATFORM_NAME);
		json.put("deviceId", device.getId());
		json.put("deviceType", "TZX");//TODO 建立设备类型
		if(device.getMac()!=null) {
			json.put("deviceModel", device.getMac());
		}
		json.put("deviceMac", device.getModel());
		json.put("checkinTime", System.currentTimeMillis());
		json.put("bindStatus", 0);
		natsComponent.publish4oms("oms.subject.device.checkin", json.toJSONString());
	}
	
	@Override
	public Device getNumber(String number) throws SQLException {
		Device device = deviceMapper.getNumber(number);
		return device;
	}
	
	@Override
	public Device getDeviceById(Integer id) throws SQLException {
		Device device = deviceMapper.getDeviceById(id);
		return device;
	}

	@Override
	public void updateDevice(Device device) throws SQLException {
		deviceMapper.updateDevice(device);
	}

	@Override
	public Integer importDevice(MultipartFile file,HttpServletRequest request) throws Exception {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String model = request.getParameter("model");
			if(model==null || model.isEmpty()) {
				return -3;
			}
			Integer returnInt = 0;
			List<Map> resourceList = new ArrayList<Map>();
			InputStream is = file.getInputStream();
			Workbook wb = new XSSFWorkbook(is);
			String[][] result = ExcelUtil.readToArray(wb, 1, 0, (short) 0);
			for (int i = 0; i < result.length; i++) {
				String[] temp = result[i];
				String number = temp[0];
				//String model = temp[1];
				Device device = new Device();
				StringBuffer mac=new StringBuffer(number);
				int index;
				for(index=2;index<mac.length();index+=3){
					mac.insert(index,':');
				}
				device.setNumber(number);
				device.setModel(model);
				device.setMac(mac.toString());
				device.setAddTime(new Date());
				device.setTag(1);
				device.setIsOnlined(0);
				device.setBelongUnitId(Constant.SUBORDINATE_UNITS_TZX);
				Device d = deviceMapper.getNumber(device.getNumber());
				String str = "^[0-9a-z]{14}$";
			    Pattern pattern = Pattern.compile(str);
			    Matcher matcher = pattern.matcher(number);
			    matcher.matches();
			    if(!matcher.matches()){
			    	return -1;
			    }
				if (d != null) {
					return -2;
				}else{
					DeviceLog deviceLog = new DeviceLog();
					HttpSession session = request.getSession(false);
					if (session != null && session.getAttribute(Constant.SESSION_USER) != null) {
						AdminUser ad = (AdminUser)session.getAttribute(Constant.SESSION_USER);
						deviceLog.setMac(device.getMac());
						deviceLog.setOperTime(new Date());
						deviceLog.setUser(ad.getUser_name());
						deviceMapper.addDevice(device);
						deviceLog.setOperation("用户"+ad.getUser_name()+"添加了一台设备编号为"+device.getNumber()+"的设备");
						deviceLogService.addDeviceLog(deviceLog);
						//发布设备登记消息到nats
						regDev4oms(device);
					}
				}
				Map tempMap = new HashMap<String,String>();
				tempMap.put("save_state", "添加成功 ");
				tempMap.put("number", device.getNumber());
				tempMap.put("model", device.getModel());
				resourceList.add(tempMap);
				returnInt++;
			}

			// 保存excel结果
			String sheetName = "设备";
			List<String> titleList = new ArrayList<String>();// 表头
			List<String> columList = new ArrayList<String>();// 表列指定名：sql中指定的伪列名

			titleList.add("编号");
			columList.add("number");
//			titleList.add("设备型号");
//			columList.add("model");
			titleList.add("导入结果");
			columList.add("save_state");
			
			String filePath =  request.getServletContext().getRealPath("/")+"excel"+File.separator;
			File fileDir = new File(filePath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			String fileName = "_"
					+ TimeUtil.nowTimestampToString().replace(" ", "").replace("-", "").replace(":", "") + ".xlsx";
			// 准备表格
			XSSFWorkbook workBook = ExcelUtil.createWorkBook2007(resourceList, sheetName, titleList, columList);
			// 输出流
			FileOutputStream out = new FileOutputStream(filePath + File.separator + fileName);
			workBook.write(out);
			out.close();
			return returnInt;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void updateBind(Message message){
		Device device = new Device();
		JSONObject json = null;
		try {
			json = JSON.parseObject(new String(message.getData()));
			if(json!=null) {
				String bindAccount = (String)json.get("mobile");
				Long bindTime = (Long)json.get("operationTime");
				String number = (String)json.get("deviceId");
				device.setBindAccount(bindAccount);
				device.setBindTime(new Date(bindTime==null ?0:bindTime));
				device.setNumber(number);
				System.out.println("update bind-user:"+json);
				deviceMapper.updateBind(device);
			}else {
				log.warn("json is null");
			}
		}catch (JSONException e) {
			log.error("can not cast to JSONObject.",e);
		} catch (SQLException e) {
			log.error("line:"+215,e);
		}
	}
	public static void main(String[] args) {
		Date date = new Date(0);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
	}

	@Override
	public List<ParamsAppoint> getParamsAppoint() throws SQLException {
		return paramsAppointMapper.getParamsAppoint();
	}

	@Override
	public List<Integer> allDevId(Condition condition) throws SQLException {
		return deviceMapper.allDevId(condition);
	}

	@Override
	public Integer changePasswd(Device device) throws SQLException {
		return deviceMapper.changePasswd(device);
	}
}
