package com.tianzhixing.devicecomm.util;

import org.apache.commons.lang3.RandomStringUtils;

public class CommonUtil {
	/**
	 * 生成随机ID
	 * @param count 随机字符串位数
	 * @return 
	 */
	public static String randomID(Integer count) {
		String randomString = RandomStringUtils.randomAlphanumeric(count);
		long now = System.currentTimeMillis();
		return randomString+now;
	}
}
