package core.common;

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
	/** 登录失败 */
	public static final Integer LOGIN_FAILURE = 10305;// 登录失败;
	/** invalid credential, token is invalid or not latest */
	public static final Integer INVALID_TOKEN = 10309;

	/** 密码不能为空 */
	public static final Integer PASSWORD_IS_NULL = 40107;//

	/** 账户不能为空 */
	public static final Integer ACCOUNT_IS_NULL = 40108;//

	/** 验证码不正确 */
	public static final Integer ERROR_VALIDATION_CODE = 40104;// 请输入正确的验证码

	/** 用户名或者密码错误 */
	public static final Integer ACCOUNT_OR_PWD_ERROR = 40109;//
	
	/** 没有该设备 */
	public static final Integer DEVICE_IS_NULL = 40110;//

}
