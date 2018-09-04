package com.tianzhixing.devicecomm.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Task {

	private Integer id; // 自增编号
	private String taskId; // 任务ID
	private String devNum; // 设备ID
	private String operate; // 任务操作类型
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime; // 任务发起时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date replayTime; // 任务回复时间
	private Integer status; // 任务完成状态 -1:发送 0:回复成功 1:其他
	private Integer ipkTaskId; // 插件任务ID

	public Task() {
		super();
	}

	public Task(String taskId, String devNum) {
		super();
		this.taskId = taskId;
		this.devNum = devNum;
	}

	public Task(String taskId, String devNum, Integer status) {
		super();
		this.taskId = taskId;
		this.devNum = devNum;
		this.status = status;
	}

	public Task(String taskId, String devNum, String operate, Integer status) {
		super();
		this.taskId = taskId;
		this.devNum = devNum;
		this.operate = operate;
		this.status = status;
	}

	public Task(String taskId, String devNum, String operate, Date startTime, Integer status, Integer ipkTaskId) {
		super();
		this.taskId = taskId;
		this.devNum = devNum;
		this.operate = operate;
		this.startTime = startTime;
		this.status = status;
		this.ipkTaskId = ipkTaskId;
	}

	public Integer getIpkTaskId() {
		return ipkTaskId;
	}

	public void setIpkTaskId(Integer ipkTaskId) {
		this.ipkTaskId = ipkTaskId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getDevNum() {
		return devNum;
	}

	public void setDevNum(String devNum) {
		this.devNum = devNum;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getReplayTime() {
		return replayTime;
	}

	public void setReplayTime(Date replayTime) {
		this.replayTime = replayTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", taskId=" + taskId + ", devNum=" + devNum + ", operate=" + operate + ", startTime="
				+ startTime + ", replayTime=" + replayTime + ", status=" + status + ", ipkTaskId=" + ipkTaskId + "]";
	}

}