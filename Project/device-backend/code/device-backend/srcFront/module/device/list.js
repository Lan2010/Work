/*!
 *	模块 - 设备列表
 *	@Author Gang
 *	@Latest Update 2018-07-04
 *
 */

define(['jquery','Global','Menu','Route','TSearch','Grid','DTP','Modal'],function($,G,Menu,Route,TSearch,Grid,DTP,Modal){

	'use strict';

	var table, hashChange = true, checkedIDs = '', $btnReboot;

	var setTable = function(params){

		var $table = $('#mainTable'),
			uri = '/api/device/list';

		// Table
		table = $table.grid({
			url: G.path + uri,
			data: params,
			checkbox: true,
			checkboxField: 'number',
			columns: [
				{data: 'orderNo',class: 'td-sn',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'number',render: function(d,r){
					return '<a href="#device/detail?id='+r.id+'" class="font-mac">'+d+'</a>';
				}},
				{data: 'model'},
				{data: 'bind', render: function(d,r,i){

					return r.bindAccount ? '<span title="绑定时间：'+$.date(r.bindTime).ymd+'">' + r.bindAccount + '</span>' : '未绑定';
				}},
				{data: 'onlineStatus', class: 'txt-c', render: function(d,r,i){
					return d == undefined ? '<span class="font-thingray">--</span>' :  '<i class="fa fa-wifi '+(d ? 'font-green' : 'font-thingray')+'" title="'+(d ? '在线' : '离线')+'"></i>';
				}},
				{data: 'millisecond', render: function(d,r,i){
					return d ? '<span title="'+(r.onlineStatus ? '在线时长' : '离线时长')+'">'+$.convertMS(d)+'</span>' : '';
				}},
				{data: 'isOnlined', class: 'txt-c', render: function(d,r,i){
					return d ? '是' : '否';
				}},
				// {data: 'addTime', class: 'txt-c', render: function(d,r,i){

				// 	return $.date(d).ymd;
				// }},
				{data: 'belongUnitId', render: function(d,r,i){
					return d === 1 && '天智星' || d === 0 && '未知' || '';
				}},
				{data: 'opt', class: 'txt-c', render: function(d,r,i){

					var canDel = !r.isOnlined && !r.onlineStatus && !r.bindAccount ? true : false;
					var html = [
						'<button type="button" class="btn default btn-del" '+(canDel ? 'data-id="'+r.id+'"' : 'disabled title="已绑定、在线、上过线的设备不允许删除"')+' >删除</button>'
					];

					return html.join('');
				}}
			],
			onCheck: function(res){

				toggleActions(res.checked);
			},
			onSuccess: function(dataHash,json){

				if (!G.checkJson(json)){ return; }

				Route.updateHash(dataHash);
				// hashChange && Route.updateHash(dataHash);
				// hashChange = true;
			},
			onError: function(xhr, textStatus){
				$('body').msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
			}
		});

		// 删除
		$table.on('click','.btn-del',delData);

	};

	// 可操作按钮
	var toggleActions = function(data){

		var len = data.length, ids = [], offLine = 0;

		for (var i=0;i<len;i++){

			ids.push(data[i].number);

			if (!data[i].onlineStatus){ offLine++; }
		}

		// Update data
		checkedIDs = ids;

		// Toggle btn
		$btnReboot.toggleClass('dim',len && !offLine ? false : true);
	};

	// 设置 Toolbar
	var setToolbar = function(){


		// 批量删除
		// $('#tBtnDelete').on('click',function(){

		// 	if ($(this).hasClass('disabled')){ return; }

		// 	var ids = table.getSelectedIDS();
		// 	if (ids){
		// 		delData(ids);
		// 	}
		// });

		// 批量重启
		$btnReboot.on('click',reboot);

	};

	// 重启
	var reboot = function(){

		if ($(this).hasClass('dim')){ return; }

		if (!checkedIDs.length){ return; }

		if (!confirm('确定重启所选的设备吗？')){ return; }

		G.$page.msg('loading',{text: '重启命令提交中'});

		var uri = '/api/device/reboot';

		$.ajax({
			url: G.path + uri,
			data: {reboot: 1, dev_num: checkedIDs.join(',')},
			cache: false,
			success:function(json){

				if (!G.checkJson(json)){ return; }

                G.$page.msg('success',{text: '操作成功',time: 2000});

				// $('#tBtnDelete').addClass('disabled');

				table.refresh();
			},
			error: function(xhr, textStatus){
				G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
			},
			complete: function(){
				$btnReboot.addClass('dim');
			}
		});
	};

	// 删除数据
	var delData = function(){

		var id = $(this).data('id');

		if (!id){ return; }
		if (!confirm('确定删除所选的设备吗？')){ return; }

		G.$page.msg('loading',{text: '删除中'});

		var uri = '/api/device/deleteDevice';

		$.ajax({
			url: G.path + uri,
			data: {id: id},
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

			// Set obj
			$btnReboot = $('#btnReboot');

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