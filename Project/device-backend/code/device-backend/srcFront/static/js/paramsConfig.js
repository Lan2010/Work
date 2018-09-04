/*!
 *	模块 - 设备 参数配置
 *	@Author Gang
 *	@Latest Update 2018-07-10
 *
 */

define(function(){

	'use strict';

	// Module export
	return {

		"newprobe.probe.enable": {
			"name": "探针模块",
			"fieldName": "probeEnable",
			"rw": 1,
            "type": 3,
            "options": "[{\"name\":\"开启\",\"value\":\"1\"},{\"name\":\"关闭\",\"value\":\"0\"}]",
            "required": true,
            "regExp": "",
            "regExpTip": "",
			"desc": ""
		},
		"newprobe.probe.server": {
			"name": "探针上报地址",
			"fieldName": "probeServer",
			"rw": 1,
            "type": 1,
            "options": "",
            "required": true,
            "regExp": "^(([0-9a-zA-Z-]{1,63}\\.)+[a-zA-Z]{2,4})$|^((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))$",
            "regExpTip": "域名 或 IP",
			"desc": "探针上报服务器地址，请输入域名 或 IP"
		},
		"newprobe.probe.port": {
			"name": "探针上报端口",
			"fieldName": "probePort",
			"rw": 1,
            "type": 1,
            "options": "",
            "required": true,
            "regExp": "^(102[5-9]{1}|10[3-9]\\d{1}|1[1-9]\\d{2}|[2-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$",
            "regExpTip": "1025 - 65535",
			"desc": "1025 - 65535"
		},
		"newprobe.probe.capcnt": {
			"name": "无线包上限",
			"fieldName": "probeCapcnt",
			"rw": 1,
            "type": 1,
            "options": "",
            "required": true,
            "regExp": "^([0-9]|[1-9]\\d{1}|[1-9]\\d{2}|[1-9]\\d{3}|[1-2]\\d{4}|31\\d{3}|32[0-6]\\d{2}|327[1-5]\\d{1}|3276[0-7]{1}|)$",
            "regExpTip": "0 - 32767",
			"desc": "单次扫描单个信道无线包总个数上限，建议500 ~ 3000"
		},
		"wireless.@wifi-iface[0].ssid": {
			"name": "设备Wifi名称",
			"fieldName": "wifi",
			"rw": 1,
            "type": 1,
            "options": "",
            "required": true,
            "regExp": "",
            "regExpTip": "",
            "rule": {byte:[1,40]},
			"desc": "1~40位（一个中文占3位）"
		}
	}

});