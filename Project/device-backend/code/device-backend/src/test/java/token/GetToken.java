package token;

import core.util.EncryptUtil;

public class GetToken {
	public static void main(String[] args) throws Exception {
		//验签
		String appid="comm13a61d4d0fd6";
		String appsecret = "deadb2e78c35db589ac0cb9a50ce6293";
		String randomString="sadfsadfasdf";
		String now="43221345345";
		String signature= EncryptUtil.encodeBySHA1(appid +randomString+now+ appsecret);
		System.out.println(signature);
	}
}
