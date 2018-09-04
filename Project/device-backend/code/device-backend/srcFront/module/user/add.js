/*!
 *	模块 - 添加用户
 *	@Author Gang
 *	@Latest Update 2018-07-18
 *
 */

define(['jquery','Global','Menu','Validate','DTP'],function($,G,Menu,Validate,DTP){

	'use strict';

	var $form, validate;

	// 设置表单验证
	var setValidate = function(){

		validate = $form.validate();

	};

	var setForm = function(){

		// 设置表单验证
		setValidate();

		// 表单提交
		$('#btnSubmit').on('click',function(){

			if (!validate.form()){ return; }

			G.$page.msg('loading',{text: '添加中'});


			var uri = '/api/user/add';
			var data = $form.serialize();

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: data,
                dataType: 'json',
                success: function(json){

                	if (!G.checkJson(json)){ return; }

                    G.$page.msg('success',{text: '添加成功',time: 2000});

                    window.history.back();
                },
                error: function(xhr, textStatus){
                	G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
                }
            });

			// return false;
		});

		$form.on('submit',function(){ return false; });

	};

	// Module export methods
	var moduleExport = {

		// Module path parameter onChange
		onSearch: function(path,params){
			// console.log('[Module][oaMgt/add] onSearch - params',params);
		},

		// Module onLoad
		onLoad: function(path,params){
			// console.log('[Module][oaMgt/add] onLoad - params',params);

			// 激活菜单
			Menu.active('user/list');

			// 改变底色			
			G.$page.addClass('page-gray');

			// 设置参数
			$form = $('#formAdd');

			// 设置表单
			setForm();
		}
	}

	return moduleExport;

});