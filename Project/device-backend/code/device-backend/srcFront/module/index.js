/*!
 *	模块 - 默认主页
 *	@Author Gang
 *	@Latest Update 2018-02-02
 *
 */

define(['jquery','Global'],function($,G){

	'use strict';

	var setMain = function(){

		var nickName = G.nickName ? '，' + G.nickName : '';

		$('#nickName').text(nickName);
	};

	// Module export methods
	var moduleExport = {

		// Module path parameter onChange
		onSearch: function(path,params){

			console.log('[Module][/index] onSearch - params',params);
		},

		// Module onLoad
		onLoad: function(path,params){

			console.log('[Module][/index] onLoad - params',params);

			setMain();

		}
	}

	return moduleExport;

});