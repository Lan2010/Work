/*!
 *	模块 - 型号列表
 *	@Author Gang
 *	@Latest Update 2018-07-06
 *
 */

define(['jquery','Global','Menu','Route','TSearch','Grid','DTP','Modal'],function($,G,Menu,Route,TSearch,Grid,DTP,Modal){

	'use strict';

	var table, hashChange = true;

	var setTable = function(params){

		var $table = $('#mainTable'),
			uri = '/api/device/getDeviceType';

		// Table
		table = $table.grid({
			url: G.path + uri,
			data: params,
			// multiCheck: true,
			// multiCheckName: 'checkboxName',
			columns: [

				{data: 'orderNo',class: 'txt-c',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'name'},
				{data: 'opt', class: 'txt-c', render: function(d,r,i){

					var canDel = !r.isOnlined && !r.onlineStatus && !r.bindAccount ? true : false;
					var html = [
						'<button type="button" class="btn default btn-del" '+(canDel ? 'data-id="'+r.code+'"' : 'disabled')+' >删除</button>'
					];

					return html.join('');
				}}
			],
			// onMultiCheck: function(ids){

			// 	$tBtnDelete.toggleClass('disabled',ids ? false : true);
			// },
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
		$table.on('click','.btn-del',function(){

			var id = $(this).data('id');

			if (id){
				delData(id);
			}
		});

	};

	// 设置 Toolbar
	var setToolbar = function(){

		// 添加型号按钮
		$('#btnAdd').on('click',function(){

			Modal.show({
				width: 600,
				title: '添加型号',
				path: 'module/model/add'
			})
		});
	};

	// 删除数据
	var delData = function(id){

		if (!confirm('确定删除所选的数据吗？')){ return; }

		G.$page.msg('loading',{text: '删除中'});

		var uri = '/api/device/deleteDeviceType';

		$.ajax({
			url: G.path + uri,
			data: {code: id},
			cache: false,
			success:function(json){

				if (!G.checkJson(json)){ return; }

                G.$page.msg('success',{text: '删除成功',time: 2000});

				// $('#tBtnDelete').addClass('disabled');

				table.refresh();
			},
			error: function(xhr, textStatus){
				G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
			}
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
				onClear: function(data){
					table.load(data ? data : {});
				}
			});	
		}
	};

	return moduleExport;

});