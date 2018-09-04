package core.pojo;

/**
 * 插件名称
 * 
 * @author dev-lan
 * @date: 2018年7月21日
 *
 */
public class FirmwareName {

	private Integer firmwareNameId; // 自增主键
	private String firmwareName; // 插件名称
	private String nickName; // 插件中文名称

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

}
