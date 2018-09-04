package com.tianzhixing.devicecomm.pojo;

import com.alibaba.fastjson.JSONObject;

/**
 * 返回给接口调用方的提示消息
 * 
 * @author Seamar Luo
 *
 */
public class ResultMessage {
	private Integer code;
	private String msg;
	private Integer total;
	private JSONObject result;
	private Object object;
	private String key;

	public ResultMessage() {
	}

	public ResultMessage(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ResultMessage(Integer code, String msg, Object object) {
		this.code = code;
		this.msg = msg;
		this.object = object;
	}

	public ResultMessage(Integer code, String msg, Integer total, Object object) {
		this.code = code;
		this.msg = msg;
		this.object = object;
		this.total = total;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public ResultMessage put(String key, Object object) {
		this.key = key;
		this.object = object;
		return this;
	}

	public String toString() {
		result = new JSONObject();
		result.put("code", this.code);
		result.put("msg", this.msg);

		if (key != null) {
			result.put(key, object);
		} else if (object != null) {
			result.put("data", object);
		}

		if (total != null) {
			result.put("total", total);
		}
		return result.toString();
	}

}
