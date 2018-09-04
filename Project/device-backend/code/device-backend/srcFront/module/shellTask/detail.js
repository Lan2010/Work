/*!
 *	模块 - 脚本任务详情
 *	@Author Gang
 *	@Latest Update 2018-08-01
 *
 */

define(['jquery','Global','Menu','Grid'],function($,G,Menu,Grid){

	'use strict';

	var opts = {}, 
		$detlBody,
		$form,
		table;

	// 获取任务详情
	var getDetail = function(fn){

		if (!opts.id){ return; }

		$detlBody.loading();

		var uri = '/api/shell/taskDetailById';

		$.ajax({
			url: G.path + uri,
			data: {taskId: opts.id},
			cache: false,
			success: function(json){
				
				if (!G.checkJson(json)){ return; }
				
				var data = json.data;

				var rate = parseInt(data.completedNum/data.devTotal*100);

				$form.fillForm(data,{
					fields: [
						'taskName',
						'user_name',
						{'addTime': data.addTime ? $.date(data.addTime).string : ''},
						'shellName',
						'taskDesc',
						{'status': data.status == 1 ? '已完成' : '已发起'},
						{'devTotal': '<span class="'+(rate>=100 ? 'font-green' : 'font-orange')+' font-16">'+rate+'%</span>' + '　（已完成：'+data.completedNum+' / 总数：'+data.devTotal+'）' }
					]
				});

				$('#divAppend').toggleClass('hide',data.operateType == 1);

				setTable();

				fn && fn(data);

				$detlBody.loading(false);	
			},
			error: function(xhr, textStatus){
				$detlBody.loading(false);
				G.pageError(uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus);
			}
		});
	};


	var setTable = function(){

		var $table = $('#devTable'),
			uri = '/api/shell/devDetailByTask';

		// Table
		table = $table.grid({
			url: G.path + uri,
			data: {taskId: opts.id},
			pageSize: 10,
			columns: [
				{data: 'orderNo',class: 'td-sn',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'number'},
				{data: 'model'},
				{data: 'onlineStatus', class: 'txt-c', render: function(d,r,i){
					return d == undefined ? '<span class="font-thingray">--</span>' :  '<i class="fa fa-wifi '+(d ? 'font-green' : 'font-thingray')+'" title="'+(d ? '在线' : '离线')+'"></i>';
				}},
				{data: 'status', render: function(d,r,i){

					var res = '';

					switch (d){
						case 0: res = '成功'; break;
						case 1: res = '未发现下载资源的url'; break;
						case 2: res = '下载资源失败'; break;
						case 3: res = '资源MD5值校验不正确'; break;
						case 4: res = '安装插件失败'; break;
					}

					return '<span class="'+(d==0?'font-green': 'font-red')+'">'+res+'</span>';
				}}
			],
			onSuccess: function(dataHash,json){

				if (!G.checkJson(json)){ return; }

			},
			onError: function(xhr, textStatus){
				$('body').msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
			}
		});

	};


	// Module export methods
	var moduleExport = {

		// Module path parameter onChange
		onSearch: function(path,params){
			// console.log('[Module][oaMgt/modify] onSearch - params',params);
		},

		// Module onLoad
		onLoad: function(path,params){
			// console.log('[Module][oaMgt/modify] onLoad - params',params);

			// 激活菜单
			Menu.active('shellTask/list');

			// 改变底色		
			G.$page.addClass('page-gray');

			// 设置参数
			opts = params;
			$detlBody = $('#detlBody');
			$form = $('#formDetl');

			// 获取详情
			getDetail();
		}
	}

	return moduleExport;

});