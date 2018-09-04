package core.pojo;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class ShellTask {
	private Integer taskId; // 自增主键
	private String taskName;// 任务名称
	private String taskDesc;// 任务描述
	private Integer shellId; // 脚本ID
	private String shellName; // 脚本名称

	private List<Integer> devId; // 设备ID
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date addTime; // 任务新增时间
	private Integer user_id; // 用户ID
	private String user_name;// 账户名称

	private Integer devTotal; // 设备总数
	private Integer completedNum; // 已完成数
	private Integer status;  // 任务状态，0--已发起，1--已完成

	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
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

	public Integer getShellId() {
		return shellId;
	}

	public void setShellId(Integer shellId) {
		this.shellId = shellId;
	}

	public String getShellName() {
		return shellName;
	}

	public void setShellName(String shellName) {
		this.shellName = shellName;
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
		return "ShellTask [taskId=" + taskId + ", taskName=" + taskName + ", taskDesc=" + taskDesc + ", shellId="
				+ shellId + ", shellName=" + shellName + ", devId=" + devId + ", addTime=" + addTime + ", user_id="
				+ user_id + ", user_name=" + user_name + ", devTotal=" + devTotal + ", completedNum=" + completedNum
				+ ", status=" + status + "]";
	}

}
