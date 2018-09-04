package core.pojo;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @ipkDescription:固件信息
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public class Firmware {

	private Integer firmwareId; // 自增主键
	private Integer firmwareNameId; // 名称ID
	private String firmwareName; // 名称
	private String nickName; // 中文名称
	private String model; // 设备型号
	private String version; // 版本
	private String firmwareDesc; // 描述
	private String firmwarePath; // 文件保存路径
	private Integer user_id; // 用户ID
	private String user_name; // 用户名

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date addTime; // 入库时间
	private Integer status; // 状态 1:正常 0:失效
	private String firmware_md5; // MD5值
	private String devNum; // 设备编号
	
	public Integer getFirmwareId() {
		return firmwareId;
	}

	public void setFirmwareId(Integer firmwareId) {
		this.firmwareId = firmwareId;
	}

	public String getDevNum() {
		return devNum;
	}

	public void setDevNum(String devNum) {
		this.devNum = devNum;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Integer getFirmwareNameId() {
		return firmwareNameId;
	}

	public void setFirmwareNameId(Integer firmwareNameId) {
		this.firmwareNameId = firmwareNameId;
	}

	public String getFirmwareName() {
		return firmwareName;
	}

	public void setFirmwareName(String firmwareName) {
		this.firmwareName = firmwareName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFirmwareDesc() {
		return firmwareDesc;
	}

	public void setFirmwareDesc(String firmwareDesc) {
		this.firmwareDesc = firmwareDesc;
	}

	public String getFirmwarePath() {
		return firmwarePath;
	}

	public void setFirmwarePath(String firmwarePath) {
		this.firmwarePath = firmwarePath;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public java.util.Date getAddTime() {
		return addTime;
	}

	public void setAddTime(java.util.Date addTime) {
		this.addTime = addTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFirmware_md5() {
		return firmware_md5;
	}

	public void setFirmware_md5(String firmware_md5) {
		this.firmware_md5 = firmware_md5;
	}

	@Override
	public String toString() {
		return "Firmware [firmwareId=" + firmwareId + ", firmwareNameId=" + firmwareNameId + ", firmwareName="
				+ firmwareName + ", nickName=" + nickName + ", model=" + model + ", version=" + version
				+ ", firmwareDesc=" + firmwareDesc + ", firmwarePath=" + firmwarePath + ", user_id=" + user_id
				+ ", addTime=" + addTime + ", status=" + status + ", firmware_md5=" + firmware_md5 + "]";
	}

}
