package com.tianzhixing.devicecomm.controller;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tianzhixing.devicecomm.common.Constant;
import com.tianzhixing.devicecomm.common.StatusCode;
import com.tianzhixing.devicecomm.pojo.ResultMessage;
import com.tianzhixing.devicecomm.redis.RedisCache;
import com.tianzhixing.devicecomm.util.EncryptUtil;

@RestController
public class TokenController {
	@Autowired
	private RedisCache redisCache;
	
	/**
	 * 获取接口调用临时凭证：token
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/token", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String setConfig(@RequestParam("appid")String appid,@RequestParam("randomString") String randomString,@RequestParam("now") String now ,
			@RequestParam("signature")String signature) {
		Iterator<Map.Entry<String, String>> entries = Constant.SECRET_STORE.entrySet().iterator();
		Map.Entry<String, String> entry = null;
		JSONObject json = new JSONObject();
		String token = "";
		ResultMessage rm = null;
		int code = StatusCode.ERROR;
		String message = "";
		String source ="";
		String sha1 = null;
		while (entries.hasNext()) {
			entry = entries.next();
			if (appid.equals(entry.getKey())) {// 找到APPID
				//根据appid+随机数+时间戳+秘钥（服务器与客户端约定的appsecret）的顺序生成的签名与参数中的签名比对，如果一致，则认为客户端合法
				System.out.println(entry.getValue());
				sha1=EncryptUtil.encodeBySHA1(appid+randomString+now+entry.getValue());
				if(signature.equals(sha1)) {
					code = StatusCode.OK;
					message = "request is success.";
				} else {// secret错误
					code = StatusCode.ERROR_SECRET;
					message = "secret is error.";
				}
				break;
			} else {
				code = StatusCode.ERROR_APPID;
				message = "appid is error.";
			}
		}
		if (code == StatusCode.OK) {
			try {
				//加密源
				source = RandomStringUtils.randomAlphanumeric(8) +System.currentTimeMillis();
				//生成token
				token = EncryptUtil.encodeBySHA1( source + "tianzhixing.com");
				token = token.replaceAll("\r\n", "");
				json.put("token", token);
				redisCache.set(token, appid, 7200);//将token放入缓存当中
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rm = new ResultMessage(code, message, json);
		} else {
			rm = new ResultMessage(code, message);
		}
		return rm.toString();
	}
	
	
	
	
//	public static void main(String[] args) throws Exception {
//		String appid="";
//		System.out.println(EncryptUtil.encryptBASE64("comm13a61d4d0fd6".getBytes()));
//		System.out.println(new String("comm13a61d4d0fd6".getBytes()));
//		System.out.println(new String(EncryptUtil.decryptBASE64("Y29tbTEzYTYxZDRkMGZkNg==")));
//		System.out.println(new String(EncryptUtil.encryptBASE64("comm13a61d4d0fd6".getBytes())));
//		String token= EncryptUtil.encodeBySHA1(RandomStringUtils.randomAlphanumeric(8) +System.currentTimeMillis() + "tianzhixing.com")+"_"+new String(EncryptUtil.encryptBASE64("comm13a61d4d0fd6".getBytes()));
//		System.out.println(token);
//		appid=new String(EncryptUtil.decryptBASE64(token.substring(token.indexOf("_")+1)));
//		System.out.println(appid);
//	}
}
