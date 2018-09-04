package core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import core.common.Constant;
import core.common.StatusCode;
import core.pojo.ResultMessages;

public class CommonUtils {
	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	/**
	 * 获取配置文件内容
	 * 
	 * @param path
	 *            相对classpath路径
	 * @param key
	 *            需要取的key
	 * @return
	 */
	public static String getPropertiesValue(String path, String key) {
		if (getProperties(path) != null) {
			return getProperties(path).getProperty(key);
		}
		return null;
	}

	/**
	 * 设置Properties
	 * 
	 * @param path
	 *            相对classpath路径
	 */
	public static Properties getProperties(String path) {
		InputStream inputStream = null;
		Properties properties = new Properties();// 属性集合对象
		try {
			inputStream = CommonUtils.class.getResourceAsStream(path);
			properties.load(inputStream);
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream)
					inputStream.close();
			} catch (IOException e) {
				logger.error("InputStream关闭失败");
			}
		}
		return properties;
	}

	/**
	 * 获取Token
	 * 
	 * @return
	 */
	public static String getToken() {
		try {
			String appid = "comm13a61d4d0fd6";
			String randomString = RandomStringUtils.randomAlphanumeric(8);
			long now = System.currentTimeMillis();
			String appsecret = "deadb2e78c35db589ac0cb9a50ce6293";
			String signature = EncryptUtil.encodeBySHA1(appid + randomString + now + appsecret);
			// String url = "http://127.0.0.1:8080/device-backend/getJson";
			String url = Constant.SISCHAIN_URL + "/token?appid=" + appid + "&randomString=" + randomString + "&now="
					+ now + "&signature=" + signature + "";
			String result = HttpClientUtil.doGet(url);
			if (result == null || result.length() == 0) {
				return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "获取token失败").toString();
			}
			JSONObject json = JSONObject.parseObject(result);
			Integer code = (Integer) json.get("code");
			if (code == 0) {
				JSONObject json2 = (JSONObject) json.get("data");
				Object obj = json2.get("token");
				return obj.toString();
			} else {
				return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "获取token失败").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessages(StatusCode.SYSTEM_IS_BUSY, "系统繁忙").toString();
		}
	}

	/**
	 * 生成随机ID
	 * 
	 * @param count
	 *            随机字符串位数
	 * @return
	 */
	public static String randomID(Integer count) {
		String randomString = RandomStringUtils.randomAlphanumeric(count);
		long now = System.currentTimeMillis();
		return randomString + now;
	}

	/**
	 *  获取用户的ip地址
	 * @param request
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getIpAddr(HttpServletRequest request) throws UnknownHostException {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}

		if (ip == null || ip.length() == 0 || "unkonown".equals(ip)) {
			ip = request.getHeader("X-Real-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip))
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException unknownhostexception) {

			}
		if (ip.indexOf(",") > 0) {
			ip = ip.substring(0, ip.indexOf(","));
		}
		return ip;
	}

}
