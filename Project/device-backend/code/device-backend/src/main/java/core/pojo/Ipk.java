package core.pojo;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @ipkDescription:插件信息
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public class Ipk {

	private Integer ipkId; // 自增主键
	private Integer ipkNameId; // 插件名称ID
	private String ipkName; // 插件名称
	private String ipkNickName; // 插件中文名称
	private String model; // 设备型号
	private String version; // 插件版本
	private String ipkDesc; // 插件描述
	private String ipkPath; // 插件文件保存路径
	private Integer user_id; // 用户ID
	private String user_name; // 用户名
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date addTime; // 入库时间
	private Integer status; // 插件状态 1:正常 0:失效
	private String ipk_md5; // MD5值

	private String devNum; // 设备编号

	public Integer getIpkId() {
		return ipkId;
	}

	public void setIpkId(Integer ipkId) {
		this.ipkId = ipkId;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Integer getIpkNameId() {
		return ipkNameId;
	}

	public void setIpkNameId(Integer ipkNameId) {
		this.ipkNameId = ipkNameId;
	}

	public String getIpkName() {
		return ipkName;
	}

	public void setIpkName(String ipkName) {
		this.ipkName = ipkName;
	}

	public String getIpkNickName() {
		return ipkNickName;
	}

	public void setIpkNickName(String ipkNickName) {
		this.ipkNickName = ipkNickName;
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

	public String getIpkDesc() {
		return ipkDesc;
	}

	public void setIpkDesc(String ipkDesc) {
		this.ipkDesc = ipkDesc;
	}

	public String getIpkPath() {
		return ipkPath;
	}

	public void setIpkPath(String ipkPath) {
		this.ipkPath = ipkPath;
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

	public String getIpk_md5() {
		return ipk_md5;
	}

	public void setIpk_md5(String ipk_md5) {
		this.ipk_md5 = ipk_md5;
	}

	public String getDevNum() {
		return devNum;
	}

	public void setDevNum(String devNum) {
		this.devNum = devNum;
	}

	@Override
	public String toString() {
		return "Ipk [ipkId=" + ipkId + ", ipkNameId=" + ipkNameId + ", ipkName=" + ipkName + ", ipkNickName="
				+ ipkNickName + ", model=" + model + ", version=" + version + ", ipkDesc=" + ipkDesc + ", ipkPath="
				+ ipkPath + ", user_id=" + user_id + ", addTime=" + addTime + ", status=" + status + ", ipk_md5="
				+ ipk_md5 + ", devNum=" + devNum + "]";
	}



}
