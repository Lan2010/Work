package com.tianzhixing.devicecomm;

import com.tianzhixing.devicecomm.util.EncryptUtil;

public class DeviceCommApplicationTests {

	public static void main(String[] args) throws Exception {
		//验签
		String appid="comm13a61d4d0fd6";
		String appsecret = "deadb2e78c35db589ac0cb9a50ce6293";
		String signature= EncryptUtil.encodeBySHA1(appid +"sadfsadfasd"+"43221345345"+ appsecret);
		System.out.println(signature);
	}
	
}
