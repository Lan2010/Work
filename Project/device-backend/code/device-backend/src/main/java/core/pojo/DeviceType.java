package core.pojo;


/**
 * 设备型号表
 * @author dev-jin
 * @date 2018年6月16日
 */
public class DeviceType {
	private String code;   // 编号
	private String name;   // 型号
	private Integer tag;	   // 1:正常 0:删除
	
	public DeviceType() {
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTag() {
		return tag;
	}
	public void setTag(Integer tag) {
		this.tag = tag;
	}

	
}
