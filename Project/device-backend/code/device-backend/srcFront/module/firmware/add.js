/*!
 *	模块 - 添加固件
 *	@Author Gang
 *	@Latest Update 2018-07-31
 *
 */

define(['jquery','Global','Menu','Validate','DTP'],function($,G,Menu,Validate,DTP){

	'use strict';

	var $form, validate;

	var setSelects = function(){

		var uri = '/api/device/modelList';

		$('#model').setSelect({
			url: G.path + uri,
			id: 'code',
			name: 'name'
		});

		var uri2 = '/api/firmware/firmwareNamelist';

		$('#firmwareNameId').setSelect({
			url: G.path + uri2,
			id: 'firmwareNameId',
			name: 'nickName'
		});
	};

	// 设置上传
	var setUpload = function(){

		$form.find('div.fileupload').fileupload();
	};

	// 设置表单验证
	var setValidate = function(){

		validate = $form.validate();
	};

	var setForm = function(){

		// 设置下拉
		setSelects();

		// 设置上传
		setUpload();

		// 设置表单验证
		setValidate();

		// 表单提交
		$form.on('submit',function(){

			if (!validate.form()){ return; }

			G.$page.msg('loading',{text: '添加中'});

			var uri = '/api/firmware/upload';

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: new FormData($form.get(0)),
                processData: false,
                contentType: false,
                dataType: 'json',
                success: function(json){

                	if (!G.checkJson(json)){ return; }

                    G.$page.msg('success',{text: '添加成功。 可继续添加或返回',time: 3000});

                    // window.history.back();
                    $form.get(0).reset();
                },
                error: function(xhr, textStatus){
                	G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
                }
            });

			return false;
		});

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
			Menu.active('firmware/list');

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