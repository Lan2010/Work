package core.pojo;


import org.springframework.format.annotation.DateTimeFormat;

/**
 * 设备信息表
 * @author dev-jin
 * @date 2018年6月16日
 */
public class Device {
	private Integer id;          // 自增编号
	private String number;   // 编号
	private String model;  // 设备型号
	private Integer belongUnitId;// 所属单位编码
	@DateTimeFormat(pattern = "yyyy-MM-dd")  
	private java.util.Date productTtime;//生产日期
	private String productTtimeStr; 
	private String mac;  // mac地址
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date addTime; //入库时间
	private Integer isBind;// 是否绑定，0未绑定，1绑定
	private String bindAccount;//绑定的账户（星链app的账号）
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date bindTime; //绑定时间（绑定星链app的时间）
	private Integer isOnlined;// 是否上过线，0未上过线，1上过线
	private Integer tag;  //1:正常  0:删除
	private String devPasswd;   // 设备密码

	
	public Device() {
		
	}
	
	
	public Integer getIsBind() {
		return isBind;
	}


	public void setIsBind(Integer isBind) {
		this.isBind = isBind;
	}


	public String getDevPasswd() {
		return devPasswd;
	}


	public void setDevPasswd(String devPasswd) {
		this.devPasswd = devPasswd;
	}


	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getBelongUnitId() {
		return belongUnitId;
	}


	public void setBelongUnitId(Integer belongUnitId) {
		this.belongUnitId = belongUnitId;
	}


	public Integer getIsOnlined() {
		return isOnlined;
	}


	public void setIsOnlined(Integer isOnlined) {
		this.isOnlined = isOnlined;
	}


	public Integer getTag() {
		return tag;
	}


	public void setTag(Integer tag) {
		this.tag = tag;
	}


	public java.util.Date getProductTtime() {
		return productTtime;
	}
	public void setProductTtime(java.util.Date productTtime) {
		this.productTtime = productTtime;
	}
	public String getProductTtimeStr() {
		if (productTtimeStr != null) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			return sdf.format(productTtimeStr);
		} else {
			return "";
		}
	}
	public void setProductTtimeStr(String productTtimeStr) throws Exception {
		if (productTtimeStr != null && !productTtimeStr.trim().equals("")) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			this.productTtime = sdf.parse(productTtimeStr);
		} else
			this.productTtime = null;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public java.util.Date getAddTime() {
		return addTime;
	}
	public void setAddTime(java.util.Date addTime) {
		this.addTime = addTime;
	}
	
	public void setAddTimeStr(String addTimeStr) throws Exception {
		if (addTimeStr != null && !addTimeStr.trim().equals("")) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			this.addTime = sdf.parse(addTimeStr);
		} else
			this.addTime = null;
	}
	

	public String getBindAccount() {
		return bindAccount;
	}

	public void setBindAccount(String bindAccount) {
		this.bindAccount = bindAccount;
	}

	public java.util.Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(java.util.Date bindTime) {
		this.bindTime = bindTime;
	}


	@Override
	public String toString() {
		return "Device [id=" + id + ", number=" + number + ", model=" + model + ", belongUnitId=" + belongUnitId
				+ ", productTtime=" + productTtime + ", productTtimeStr=" + productTtimeStr + ", mac=" + mac
				+ ", addTime=" + addTime + ", isBind=" + isBind + ", bindAccount=" + bindAccount + ", bindTime="
				+ bindTime + ", isOnlined=" + isOnlined + ", tag=" + tag + "]";
	}
	
}
