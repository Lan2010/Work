package core.pojo;



public class SetConfig {
	private Integer probeEnable;//开关选项 "0"代表关闭，"1"代表开启
	private String probeServer;//探针上报服务器地址选项
	private Integer probePort;//探针上报服务器端口选项
	private Integer probeCapcnt;//单次扫描单个信道无线包
	private String wifi;//设备wifi名称选项
	
	public SetConfig() {
	}

	public Integer getProbeEnable() {
		return probeEnable;
	}

	public void setProbeEnable(Integer probeEnable) {
		this.probeEnable = probeEnable;
	}

	public String getProbeServer() {
		return probeServer;
	}

	public void setProbeServer(String probeServer) {
		this.probeServer = probeServer;
	}

	public Integer getProbePort() {
		return probePort;
	}

	public void setProbePort(Integer probePort) {
		this.probePort = probePort;
	}

	public Integer getProbeCapcnt() {
		return probeCapcnt;
	}

	public void setProbeCapcnt(Integer probeCapcnt) {
		this.probeCapcnt = probeCapcnt;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	
	
}
