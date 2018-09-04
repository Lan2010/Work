package core.util;

import com.alibaba.fastjson.JSONObject;

/**
 * fastjson 工具类
 * @author dev-teng
 * @date 2018年6月11日
 */
public class FastjsonUtil {
	/**
	 * 获取字符串值类型值
	 * @param source
	 * @param key
	 * @return
	 */
	public static String getStringValue(String source, String key) {
		return parseObject(source).getString(key);
	}
	
	/**
	 * 获取整型值
	 * @param source
	 * @param key
	 * @return
	 */
	public static Integer getIntValue(String source, String key) {
		return  parseObject(source).getIntValue(key);
	}
	
	public static JSONObject parseObject(String source) {
		return JSONObject.parseObject(source);
	}
}
