package core.pojo;


import org.springframework.format.annotation.DateTimeFormat;

/**
 * 设备在线情况表
 * @author dev-jin
 * @date 2018年6月21日
 */
public class DeviceOnline {
	private String devNumber;    // 设备编号
	private Integer onlineStatus; // 是否在线1:在线 0:不在线
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date recordTime;  //生成记的时间
	
	
	public DeviceOnline() {
	}

	

	public String getDevNumber() {
		return devNumber;
	}



	public void setDevNumber(String devNumber) {
		this.devNumber = devNumber;
	}

	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public java.util.Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(java.util.Date recordTime) {
		this.recordTime = recordTime;
	}


	
	
}
