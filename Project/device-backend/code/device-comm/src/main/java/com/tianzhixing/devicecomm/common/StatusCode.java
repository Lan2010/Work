package com.tianzhixing.devicecomm.common;

/**
 * 全局返回码
 * 
 * @author dev-teng
 * @date 2018年6月7日
 */
public class StatusCode {
	/** 请求成功 */
	public static final Integer OK = 0;// 请求成功;
	/** 失败 */
	public static final Integer ERROR = -1;
	/** 请求超时 */
	public static final Integer REQ_TIMEOUT = 10101;//
	/** 网络异常 */
	public static final Integer UNUSUAL_NETWORK = 10102;//
	/** 系统繁忙，请稍后再试 */
	public static final Integer SYSTEM_IS_BUSY = 10103;//
	/** SQL异常 */
	public static final Integer SQL_EXCEPTION = 10104;//
	/** IO异常 */
	public static final Integer IO_EXCEPTION = 10105;//
	/** 数字格式错误 */
	public static final Integer NUMBER_FORMAT_EXCEPTION = 10106;//
	/** 请求参数错误 */
	public static final Integer ERROR_REQ_PARAM = 10201;//
	/** 请求方法错误，例如POST请求，填写成GET */
	public static final Integer ERROR_REQ_METHOD = 10202;//
	/** 未授权 */
	public static final Integer NOT_AUTH = 10301;// 未授权;
	/** 未登录 */
	public static final Integer NOT_LOGIN = 10302;// 未登录;
	/** 登录超时 */
	public static final Integer LOGIN_TIMEOUT = 10303;// 登录超时;
	/** 用户被禁用 */
	public static final Integer DISABLE_USER = 10304;// 用户被禁用
	/** 没有小程序登录凭证 */
	public static final Integer NO_CODE = 10305;// 
	/** openid为空 */
	public static final Integer NO_OPENID = 10306;
	/** token接口请求参数secret错误 */
	public static final Integer ERROR_SECRET = 10307;
	/** token接口请求参数appid错误 */
	public static final Integer ERROR_APPID = 10308;
	/** invalid credential, token is invalid or not latest */
	public static final Integer INVALID_TOKEN = 10309;
	/** 参数中缺少token */
	public static final Integer NO_TOKEN = 10310;
	/** 重复领取绿卡 */
	public static final Integer REGET = 10401;// 已领取过，领取失败;
	/** 设备获取配置失败 */
	public static final Integer NO_CONFIG = 10402;
	/** JSON解析错误 */
	public static final Integer ERROR_JSON = 10403;
	/** 修改设备系统用户密码失败 */
	public static final Integer CHANGE_PASSWD_FAILED = 10501;

}
