/*!
 *	模块 - 用户列表
 *	@Author Gang
 *	@Latest Update 2018-07-16
 *
 */

define(['jquery','Global','Menu','Route','TSearch','Grid','DTP','Modal'],function($,G,Menu,Route,TSearch,Grid,DTP,Modal){

	'use strict';

	var table, hashChange = true;

	var setTable = function(params){

		var $table = $('#mainTable'),
			uri = '/api/user/list';

		// Table
		table = $table.grid({
			url: G.path + uri,
			data: params,
			// mulSelect: true,
			columns: [
				{data: 'orderNo',class: 'td-sn',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'user_name'},
				{data: 'nickname'},
				{data: 'cell_phone'},
				{data: 'addTime', render: function(d,r,i){ return $.date(d).string; }},
				{data: 'role_id', render: function(d,r,i){
					return d == 0 && '管理员' || d == 1 && '普通用户' || '';
				}},
				{data: 'status', render: function(d,r,i){
					return d ? '<span class="font-green">启用</span>' : '<span class="font-red">禁用</span>';
				}},
				{data: 'opt', class: 'txt-c', render: function(d,r,i){

					var html = [
						'<button type="button" class="btn default btn-status" data-status="'+r.status+'" data-username="'+r.user_name+'">'+(r.status? '禁用' : '启用')+'</button>　',
						'<button type="button" class="btn default btn-pass" data-status="'+r.status+'" data-username="'+r.user_name+'" data-id="'+r.user_id+'">修改密码</button>'
					];

					return html.join('');
				}}
			],
			// onCheck: function(data){

			// 	console.log(data);
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

		// 启用、禁用
		$table.on('click','.btn-status',setStatus);

		// 修改用户密码
		$table.on('click','.btn-pass',function(){

			Modal.show({
				width: 600,
				title: '修改密码',
				path: 'user/userPass',
				data: $(this).data()
			});
		});

	};

	// 设置 Toolbar
	var setToolbar = function(){

	};

	// 设置 启用、禁用
	var setStatus = function(){

		var data = $(this).data(),
			username = data.username,
			status = data.status;

		if (!confirm('确定'+(status == '1' ? '禁用' : '启用')+'该用户吗？')){ return; }

		G.$page.msg('loading',{text: '提交中'});

		var uri = '/api/user/disable';

		$.ajax({
			url: G.path + uri,
			data: {user_name: username,status: status ? 0 : 1},
			cache: false,
			success:function(json){

				if (!G.checkJson(json)){ return; }

                G.$page.msg('success',{text: '操作成功',time: 2000});

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

		tableRefresh: function(){

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