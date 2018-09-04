package com.tianzhixing.devicecomm.common;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	/**
	 * Token名
	 */
	public static final String TOKEN = "token";
	
	public static final Map<String,String> SECRET_STORE = new HashMap<String,String>();
	static {
		SECRET_STORE.put("comm13a61d4d0fd6", "deadb2e78c35db589ac0cb9a50ce6293");//设备管控平台
	}
	
//	public static void main(String[] args) throws Exception {
//		String plat = "设备管控平台";
//		String md5_prefix=EncryptUtil.encodeByMD5(plat).substring(0,12);
//		String md5_suffix=EncryptUtil.encodeByMD5(plat).substring(12);
//		System.out.println("appid=comm"+md5_prefix);
//		System.out.println(md5_suffix);
//		System.out.println("secret="+EncryptUtil.encodeByMD5(md5_suffix+"tianzhixing"));
//		//7ec9b4cf51733c1646837ac4c7fadca2
//	}
	/**
	 * mac的正则表达式（无分隔符）
	 */
	public static final String pattern = "([0-9a-z]{14})";
	
	/**
	 * taskId的正则表达式
	 */
	public static final String taskId = "[0-9a-zA-Z]{32}";
	
	public static final String REPLY_URL = "http://192.168.11.102:8000/dev/reply/";
	
	/**
	 * 平台名称
	 */
	public static final String PLATFORM_NAME = "DEVICEMANAGE";
}
