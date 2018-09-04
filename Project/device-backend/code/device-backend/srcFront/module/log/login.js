/*!
 *	模块 - 日志 - 登录
 *	@Author Gang
 *	@Latest Update 2018-07-06
 *
 */

define(['jquery','Global','Menu','Route','TSearch','Grid'],function($,G,Menu,Route,TSearch,Grid){

	'use strict';

	var table, hashChange = true;

	var setTable = function(params){

		var $table = $('#mainTable'),
			uri = '/api/loginLog';

		// Table
		table = $table.grid({
			url: G.path + uri,
			data: params,
			columns: [

				{data: 'orderNo',class: 'td-sn',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'userName'},
				{data: 'loginTime', render: function(d,r,i){
					return $.date(d).string;
				}},
				{data: 'loginOutTime', render: function(d,r,i){
					return $.date(d).string;
				}}
			],
			onSuccess: function(dataHash,json){

				if (!G.checkJson(json)){ return; }

				hashChange && Route.updateHash(dataHash);
				hashChange = true;
			},
			onError: function(xhr, textStatus){
				$('body').msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
			}
		});

	};

	// 设置 Toolbar
	var setToolbar = function(){


	};


	// Module export methods
	var moduleExport = {

		// Module path parameter onChange
		onSearch: function(path,params){
			// console.log('[Module][oaMgt/list] onSearch - params',params);

			hashChange = false;

			// 填充搜索数据
			TSearch.fillData(params);

			// 更新表格数据
			table.load(params ? params : {});
		},

		// Module onLoad
		onLoad: function(path,params){
			// console.log('[Module][oaMgt/list] onLoad - params',params);

			// 激活菜单
			Menu.active(path);

			// 初始化表格
			setTable(params);

			// 初始化搜索字段
			setToolbar();

			// 填充搜索数据
			TSearch.fillData(params);

			// 设置通用的搜索交互
			TSearch.init({
				onSearch: function(data){
					table.load(data ? data : {});
				},
				onClear: function(data){
					table.load(data ? data : {});
				}
			});	
		}
	};

	return moduleExport;

});