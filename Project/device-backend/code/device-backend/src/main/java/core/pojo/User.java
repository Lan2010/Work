package core.pojo;

/**
 * 用户表
 * @author dev-teng
 * @date 2018年6月12日
 */
public class User {
	private Integer userId;// 自增主键
	private String cellPhone;// 手机号码
	
	public User() {}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
}
