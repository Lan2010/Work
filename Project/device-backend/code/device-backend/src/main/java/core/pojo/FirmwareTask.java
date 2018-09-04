package core.pojo;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @Description:插件任务（安装/卸载）
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public class FirmwareTask {

	private Integer firmwareTaskId; // 自增主键
	private String taskName;// 任务名称
	private String taskDesc;// 任务描述

	private Integer firmwareId; // 插件ID
	private Integer firmwareNameId; // 名称ID
	private String firmwareName; // 名称
	private String nickName; // 中文名称
	private String version; // 固件版本

	private List<Integer> devId; // 设备ID
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date addTime; // 任务新增时间
	private Integer user_id; // 用户ID
	private String user_name;// 账户名称

	private Integer status; // 任务状态，0--已发起，1--已完成

	private Integer devTotal; // 设备总数
	private Integer completedNum; // 已完成数

	public Integer getFirmwareTaskId() {
		return firmwareTaskId;
	}

	public void setFirmwareTaskId(Integer firmwareTaskId) {
		this.firmwareTaskId = firmwareTaskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public Integer getFirmwareId() {
		return firmwareId;
	}

	public void setFirmwareId(Integer firmwareId) {
		this.firmwareId = firmwareId;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Integer> getDevId() {
		return devId;
	}

	public void setDevId(List<Integer> devId) {
		this.devId = devId;
	}

	public java.util.Date getAddTime() {
		return addTime;
	}

	public void setAddTime(java.util.Date addTime) {
		this.addTime = addTime;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDevTotal() {
		return devTotal;
	}

	public void setDevTotal(Integer devTotal) {
		this.devTotal = devTotal;
	}

	public Integer getCompletedNum() {
		return completedNum;
	}

	public void setCompletedNum(Integer completedNum) {
		this.completedNum = completedNum;
	}

	@Override
	public String toString() {
		return "FirmwareTask [firmwareTaskId=" + firmwareTaskId + ", taskName=" + taskName + ", taskDesc=" + taskDesc
				+ ", firmwareId=" + firmwareId + ", firmwareNameId=" + firmwareNameId + ", firmwareName=" + firmwareName
				+ ", nickName=" + nickName + ", version=" + version + ", devId=" + devId + ", addTime=" + addTime
				+ ", user_id=" + user_id + ", user_name=" + user_name + ", status=" + status + ", devTotal=" + devTotal
				+ ", completedNum=" + completedNum + ", getFirmwareTaskId()=" + getFirmwareTaskId() + ", getTaskName()="
				+ getTaskName() + ", getTaskDesc()=" + getTaskDesc() + ", getFirmwareId()=" + getFirmwareId()
				+ ", getFirmwareNameId()=" + getFirmwareNameId() + ", getFirmwareName()=" + getFirmwareName()
				+ ", getNickName()=" + getNickName() + ", getVersion()=" + getVersion() + ", getDevId()=" + getDevId()
				+ ", getAddTime()=" + getAddTime() + ", getUser_id()=" + getUser_id() + ", getUser_name()="
				+ getUser_name() + ", getStatus()=" + getStatus() + ", getDevTotal()=" + getDevTotal()
				+ ", getCompletedNum()=" + getCompletedNum() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

}