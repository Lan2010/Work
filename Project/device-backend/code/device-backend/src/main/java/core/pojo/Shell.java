package core.pojo;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @Description:脚本信息
 * @author: dev-lan
 * @date: 2018年7月18日
 */
public class Shell {
	private Integer shellId; // 自增主键
	private String shellName; // 脚本名称
	private String shellDesc; // 插件描述
	private String shellPath; // 插件文件保存路径
	private Integer user_id; // 用户ID
	private String user_name; // 用户名称
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date addTime; // 入库时间
	private Integer status; // 插件状态 1:正常 0:失效
	private String shell_md5; // MD5值

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

	

	public String getShellPath() {
		return shellPath;
	}

	public void setShellPath(String shellPath) {
		this.shellPath = shellPath;
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

	public String getShell_md5() {
		return shell_md5;
	}

	public void setShell_md5(String shell_md5) {
		this.shell_md5 = shell_md5;
	}

	public String getShellDesc() {
		return shellDesc;
	}

	public void setShellDesc(String shellDesc) {
		this.shellDesc = shellDesc;
	}

	@Override
	public String toString() {
		return "Shell [shellId=" + shellId + ", shellName=" + shellName + ", shellDesc=" + shellDesc + ", shellPath="
				+ shellPath + ", user_id=" + user_id + ", user_name=" + user_name + ", addTime=" + addTime + ", status="
				+ status + ", shell_md5=" + shell_md5 + "]";
	}

	
}
