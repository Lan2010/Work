package core.pojo;

/**
 * 设备可配置参数约定
 * @author dev-teng
 * @date 2018年7月17日
 */
public class ParamsAppoint {
	private Integer id;//自增编号
	private String viewName;//参数中文释义，页面展示用名称
	private String devParam;//对应设备中的参数名
	private String value;//对应设备参数的值
	private String describe;//参数描述
	private String rw;//r：可读，rw：可读写
	private String inputType;//输入类型
	private String options;//输入类型的分类集
	private String group;//所属模块的分组
	private String required;//是否必填
	private String rule;//验证规则，例如mac表示是一个mac地址类型的字符串，phone表示是一个手机类型
	private String regExp;//正则表达式
	private String regExpTip;//正则验证提示，输入提示作用
	private String devAlias;//设备参数名的别名
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getDevParam() {
		return devParam;
	}
	public void setDevParam(String devParam) {
		this.devParam = devParam;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getRw() {
		return rw;
	}
	public void setRw(String rw) {
		this.rw = rw;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public String getRegExpTip() {
		return regExpTip;
	}
	public void setRegExpTip(String regExpTip) {
		this.regExpTip = regExpTip;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDevAlias() {
		return devAlias;
	}
	public void setDevAlias(String devAlias) {
		this.devAlias = devAlias;
	}
	
}
