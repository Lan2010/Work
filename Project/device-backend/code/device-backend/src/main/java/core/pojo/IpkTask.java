package core.pojo;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @Description:插件任务（安装/卸载）
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public class IpkTask {

	private Integer ipkTaskId; // 自增主键
	private String taskName;// 任务名称
	private String taskDesc;// 任务描述
	private Short startType; // 启动类型，0--立即启动 1--定时启动
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime; // 启动时间
	private Short operateType; // 操作类型，0--安装插件 1--卸载插件
	private Integer ipkId; // 插件ID
	private Integer ipkNameId; // 插件名称ID
	private String ipkName; // 插件名称
	private String ipkNickName; // 插件中文名称
	private String version; // 插件版本

	private List<Integer> devId; // 设备ID
	private String append; // 升级方式
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date addTime; // 任务新增时间
	private Integer user_id; // 用户ID
	private String user_name;//账户名称

	private Integer status; // 任务状态，0--已发起，1--已完成

	private Integer rate; // 任务进度 ？%（0--100）
	private Integer devTotal; // 设备总数
	private Integer completedNum; // 已完成数
	public Integer getIpkTaskId() {
		return ipkTaskId;
	}
	public void setIpkTaskId(Integer ipkTaskId) {
		this.ipkTaskId = ipkTaskId;
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
	public Short getStartType() {
		return startType;
	}
	public void setStartType(Short startType) {
		this.startType = startType;
	}
	public java.util.Date getStartTime() {
		return startTime;
	}
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	public Short getOperateType() {
		return operateType;
	}
	public void setOperateType(Short operateType) {
		this.operateType = operateType;
	}
	public Integer getIpkId() {
		return ipkId;
	}
	public void setIpkId(Integer ipkId) {
		this.ipkId = ipkId;
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
	public String getAppend() {
		return append;
	}
	public void setAppend(String append) {
		this.append = append;
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
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
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
		return "IpkTask [ipkTaskId=" + ipkTaskId + ", taskName=" + taskName + ", taskDesc=" + taskDesc + ", startType="
				+ startType + ", startTime=" + startTime + ", operateType=" + operateType + ", ipkId=" + ipkId
				+ ", ipkNameId=" + ipkNameId + ", ipkName=" + ipkName + ", ipkNickName=" + ipkNickName + ", version="
				+ version + ", devId=" + devId + ", append=" + append + ", addTime=" + addTime + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", status=" + status + ", rate=" + rate + ", devTotal=" + devTotal
				+ ", completedNum=" + completedNum + "]";
	}


}