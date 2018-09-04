/*!
 *	模块 - 任务 - 固件更新
 *	@Author Gang
 *	@Latest Update 2018-08-01
 *
 */

define(['jquery','Global','Menu','Route','TSearch','Grid','DTP','Modal'],function($,G,Menu,Route,TSearch,Grid,DTP,Modal){

	'use strict';

	var table, hashChange = true, checkedIDs = '', $btnReboot;

	var setTable = function(params){

		var $table = $('#mainTable'),
			uri = '/api/firmware/tasklist';

		// Table
		table = $table.grid({
			url: G.path + uri,
			data: params,
			type: 'post',
			// mulSelect: true,
			columns: [
				{data: 'orderNo',class: 'td-sn',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'taskName',render: function(d,r){
					return '<a href="#fmwTask/detail?id='+r.firmwareTaskId+'" class="font-mac">'+d+'</a>';
				}},
				{data: 'nickName'},
				{data: 'version'},
				{data: 'addTime', class: 'txt-c', render: function(d,r,i){ return $.date(d).ymd; }},
				{data: 'devTotal', class: 'txt-c', render: function(d,r,i){ return r.completedNum + ' / ' + d }},
				{data: 'progress', class: 'td-progress', render: function(d,r,i){

					var rate = parseInt(r.completedNum / r.devTotal * 100);

					var html = [
						'<div class="progress">',
							'<div class="progress-bar progress-bar-success progress-bar-striped '+(rate != 100 ? 'active' : '')+'" style="width: '+rate+'%;"></div>',
							'<span class="rate">'+rate+' %</span>',
						'</div>'
					];

					return html.join('');
				}},
				{data: 'user_name'}
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
				onFilter: function(data){
					table.load(data ? data : {});
				},
				onClear: function(data){
					table.load(data ? data : {});
				},
				onFiltClear: function(data){
					table.load(data ? data : {});
				}
			});	
		}
	};

	return moduleExport;

});