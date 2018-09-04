package core.pojo;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 设备操作日志
 * @author dev-jin
 * @date 2018年6月20日
 */
public class DeviceLog {
	private Integer id;           // 自增编号
	private Integer orderNum;     // 操作顺序号
	private String operation; // 操作
	private String user;      //操作人
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date operTime;//操作时间
	private String mac;       //设备mac地址
	
	public DeviceLog() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}




	public Integer getOrderNum() {
		return orderNum;
	}




	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}




	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public java.util.Date getOperTime() {
		return operTime;
	}

	public void setOperTime(java.util.Date operTime) {
		this.operTime = operTime;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	
}
