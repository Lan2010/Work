package core.pojo;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户登录日志
 * @author dev-jin
 * @date 2018年6月20日
 */
public class LoginLog {
	private Integer id;           // 自增编号
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date loginTime;//登录时间
	private String nickName;  // 昵称
	private String userName;  //账户名
	private String ip;  //登录ip
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date loginOutTime; //注销系统时间
	
	public LoginLog() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public java.util.Date getLoginOutTime() {
		return loginOutTime;
	}

	public void setLoginOutTime(java.util.Date loginOutTime) {
		this.loginOutTime = loginOutTime;
	}
	
	
}
