package core.pojo;

/**
 * 刷选设备条件
 * 
 * @author dev-lan
 * @date: 2018年7月21日
 *
 */
public class Condition {
	private String model; // 设备型号
	private Integer belongUnitId;// 所属单位编码
	private String isBind; // 是否绑定
	private Integer onlineStatus; // 是否在线1:在线 0:不在线
	private Integer isOnlined;// 是否上过线，0未上过线，1上过线
	private Integer selectType; // 选择类型 （全选 1、反选0）
	private String devIds;// 设备ID集合,逗号隔开'12345,12346,12347...'

	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getDevIds() {
		return devIds;
	}

	public void setDevIds(String devIds) {
		this.devIds = devIds;
	}

	public Integer getSelectType() {
		return selectType;
	}

	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getBelongUnitId() {
		return belongUnitId;
	}

	public void setBelongUnitId(Integer belongUnitId) {
		this.belongUnitId = belongUnitId;
	}

	public String getIsBind() {
		return isBind;
	}

	public void setIsBind(String isBind) {
		this.isBind = isBind;
	}

	public Integer getIsOnlined() {
		return isOnlined;
	}

	public void setIsOnlined(Integer isOnlined) {
		this.isOnlined = isOnlined;
	}

	@Override
	public String toString() {
		return "Condition [model=" + model + ", belongUnitId=" + belongUnitId + ", isBind=" + isBind + ", onlineStatus="
				+ onlineStatus + ", isOnlined=" + isOnlined + ", selectType=" + selectType + ", devIds=" + devIds + "]";
	}

}
