/*!
 *	模块 - 脚本列表
 *	@Author Gang
 *	@Latest Update 2018-07-31
 *
 */

define(['jquery','Global','Menu','Route','TSearch','Grid','DTP','Modal'],function($,G,Menu,Route,TSearch,Grid,DTP,Modal){

	'use strict';

	var table, hashChange = true;

	var setTable = function(params){

		var $table = $('#mainTable'),
			uri = '/api/shell/list';

		// Table
		table = $table.grid({
			url: G.path + uri,
			data: params,
			columns: [
				{data: 'orderNo',class: 'td-sn',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'shellName'},
				{data: 'shellDesc'},
				{data: 'addTime', render: function(d,r,i){ return $.date(d).string }},
				{data: 'user_name'},
				{data: 'status', render: function(d,r,i){
					return d ? '<span class="font-green">正常</span>' : '<span class="font-gray">失效</span>'
				}},
				{data: 'opt', class: 'txt-c', render: function(d,r,i){

					var html = [
						'<button type="button" class="btn default btn-del" data-id="'+r.shellId+'">删除</button>'
					];

					return html.join('');
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

		// 删除
		$table.on('click','.btn-del',delData);

	};


	// 删除数据
	var delData = function(){

		var id = $(this).data('id');

		if (!id){ return; }
		if (!confirm('确定删除吗？')){ return; }

		G.$page.msg('loading',{text: '删除中'});

		var uri = '/api/shell/delete';

		$.ajax({
			url: G.path + uri,
			data: {shellId: id},
			cache: false,
			success:function(json){

				if (!G.checkJson(json)){ return; }

                G.$page.msg('success',{text: '删除成功',time: 2000});

				table.refresh();
			},
			error: function(xhr, textStatus){
				G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
			}
		});

	};

	// 设置 Toolbar
	var setToolbar = function(){

		// Datetime
		$('#addTime').datetimepicker({
			format: 'yyyy-mm-dd',
			minView: 2
		});
	};


	// Module export methods
	var moduleExport = {

		refresh: function(){

			table.refresh();
		},

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