package com.tianzhixing.devicecomm.pojo;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 设备信息表
 * 
 * @author dev-teng
 * @date 2018年6月26日
 */
public class Device {
	private Integer devId; // 自增编号
	private String number; // 编号
	private String model; // 设备型号
	private Integer belongUnitId;// 所属单位编码
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date productTtime;// 生产日期
	private String productTtimeStr;
	private String mac; // mac地址
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date addTime; // 入库时间
	private String addTimeStr;
	private Integer isOnlined; //是否在线过
	private Integer tag; // 0:正常 1:删除

	public Device() {

	}


	public Integer getDevId() {
		return devId;
	}


	public void setDevId(Integer devId) {
		this.devId = devId;
	}


	public Integer getIsOnlined() {
		return isOnlined;
	}

	public void setIsOnlined(Integer isOnlined) {
		this.isOnlined = isOnlined;
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

	public void setBelongUnitId(Integer belongUnitId) {
		this.belongUnitId = belongUnitId;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public int getBelongUnitId() {
		return belongUnitId;
	}

	public void setBelongUnitId(int belongUnitId) {
		this.belongUnitId = belongUnitId;
	}

	public java.util.Date getProductTtime() {
		return productTtime;
	}

	public void setProductTtime(java.util.Date productTtime) {
		this.productTtime = productTtime;
	}

	public String getProductTtimeStr() {
		if (productTtimeStr != null) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(productTtimeStr);
		} else {
			return "";
		}
	}

	public void setProductTtimeStr(String productTtimeStr) throws Exception {
		if (productTtimeStr != null && !productTtimeStr.trim().equals("")) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
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

	public String getAddTimeStr() {
		if (addTimeStr != null) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(addTimeStr);
		} else {
			return "";
		}
	}

	public void setAddTimeStr(String addTimeStr) throws Exception {
		if (addTimeStr != null && !addTimeStr.trim().equals("")) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			this.addTime = sdf.parse(addTimeStr);
		} else
			this.addTime = null;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

}
