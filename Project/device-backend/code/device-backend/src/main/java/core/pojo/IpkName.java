package core.pojo;

/**
 * 插件名称
 * 
 * @author dev-lan
 * @date: 2018年7月21日
 *
 */
public class IpkName {

	private Integer ipkNameId; // 自增主键
	private String ipkName; // 插件名称
	private String ipkNickName; // 插件中文名称

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

}
