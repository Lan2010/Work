/*!
 *	模块 - 概览
 *	@Author Gang
 *	@Latest Update 2018-08-01
 *
 */

define(['jquery','Global','Menu','Route'],function($,G,Menu,Route){

	'use strict';

	var $overview;

	var getStat = function(){

		$overview.loading();

		var uri = '/api/statistics/device';

		$.ajax({
			url: G.path + uri,
			cache: false,
			success: function(json){
				
				if (!G.checkJson(json)){ return; }
				
				var data = json.data;

				$('#all_total').text(data.all.total);
				$('#all_onlined').text(data.all.onlined);
				$('#all_unonlined').text(data.all.unonlined);

				$('#unbound_total').text(data.unbound.total);
				$('#unbound_online').text(data.unbound.online);
				$('#unbound_today').text(data.unbound.today);
				$('#unbound_unonlined').text(data.unbound.unonlined);

				$('#bind_total').text(data.bind.total);
				$('#bind_online').text(data.bind.online);
				$('#bind_today').text(data.bind.today);

				$overview.loading(false);	
			},
			error: function(xhr, textStatus){
				$overview.loading(false);
				G.pageError(uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus);
			}
		});
	};

	// Module export methods
	var moduleExport = {

		// Module path parameter onChange
		onSearch: function(path,params){
			// console.log('[Module][oaMgt/list] onSearch - params',params);

			hashChange = false;

		},

		// Module onLoad
		onLoad: function(path,params){
			// console.log('[Module][oaMgt/list] onLoad - params',params);

			// 激活菜单
			Menu.active(path);

			// 改变底色			
			G.$page.addClass('page-gray');

			$overview = $('#overview');

			getStat();
		}
	};

	return moduleExport;

});