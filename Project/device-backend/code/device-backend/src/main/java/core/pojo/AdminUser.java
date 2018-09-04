package core.pojo;

import org.springframework.format.annotation.DateTimeFormat;



/**
 * 后台管理用户信息
 * @author dev-jin
 * @date 2018年6月19日
 */
public class AdminUser {
	private Integer user_id;//自增主键
	private String user_name;//账户名称
	private String nickname;//用户中文名字
	private String password;//登录密码
	private String cell_phone;//用户手机号
	private Short status;//用户生效状态，1表示生效，0表示失效
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date create_time;//创建时间，时间戳形式
	private Short role_id;//角色ID，对应角色表主键
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	private java.util.Date modify_time;//用户信息更新时间
	private String remark;//备注
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
		this.user_name = user_name.trim();
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCell_phone() {
		return cell_phone;
	}
	public void setCell_phone(String user_phone) {
		this.cell_phone = user_phone;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Short getRole_id() {
		return role_id;
	}
	public void setRole_id(Short role_id) {
		this.role_id = role_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public java.util.Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(java.util.Date create_time) {
		this.create_time = create_time;
	}
	public java.util.Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(java.util.Date modify_time) {
		this.modify_time = modify_time;
	}
}