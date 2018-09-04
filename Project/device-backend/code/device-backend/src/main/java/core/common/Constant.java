package core.common;

import java.text.SimpleDateFormat;

import core.util.CommonUtils;

/**
 * 全局静态变量
 * @author dev-teng
 * @date 2018年6月14日
 */
public class Constant {
	
	public static final String APPLICATION_CONFIG_PATH="/conf/properties/application.properties";
	
	/** cookie 名称 */
	public static final String COOKIE_USER = "AdminUser";
	
	public final static String SESSION_USER = "AdminUser";
	/** cookie生命周期 */
	public static final Integer COOKIE_MAX_AGE = 10*24*60*60;
	/** 登录密码加密的key*/
	public static final String LOGIN_SECRETKEY="XX#$%()(#*!()!KL<><20180205fdf>?N<:{LWPW";
	
	public static final String COOKIE_SECRETKEY="XX#$%()(#*!()!KL<cookie>?N<:{LWPW";
	
	/**所属单位编码*/
	public static final Integer SUBORDINATE_UNITS_TZX=1;//天智星
	
	public static final Integer SUBORDINATE_UNITS_404=0;//未知
	


	public static String SISCHAIN_URL;
			
	static {		
		SISCHAIN_URL = CommonUtils.getPropertiesValue(APPLICATION_CONFIG_PATH, "cn.sischain.comm.url");
	}
	
	public final static String SESSION_USERMENU = "jsonmu";
	
	
	/**
	 * 日期格式
	 */
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 平台名称
	 */
	public static final String PLATFORM_NAME = "DEVICEMANAGE";

}
