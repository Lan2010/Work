/*!
 *	模块 - 用户管理 - 修改用户密码 （弹窗）
 *	@Author Gang
 *	@Latest Update 2018-07-06
 *
 */

define(['jquery','Extend','Global','Validate','Modal'],function($,Extend,G,Validate,Modal){

	'use strict';

	var opts = {}, validate, $form;


	var setForm = function(){

		$('#username').text(opts.username);

		// 设置验证表单
		validate = $form.validate();

		var uri = '/api/user/changePasswd';

		// 表单提交
		$form.on('submit',function(){

			if (!validate.form()){ return; }

			$('body').msg('loading',{text: '提交中',level: 'top'});

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: {
                	user_name: opts.username,
                	password: $('#password').val()
                },
                success: function(json,status,xhr){

                	if (!G.checkJson(json,{level:'top'})){ return; }

                	Modal.hide();
                    $('body').msg('success',{text: '修改成功',level: 'top',time: 2000});

                },
                error: function(xhr, textStatus){
                	$('body').msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus,level: 'top'});
                }
            });

			return false;
		});

	};

	// Module export methods
	var moduleExport = {

		// Module onLoad
		onLoad: function(path,params){
			// console.log('[Module][osConfig/finishPage] onLoad - params',params);

			// 检查参数			
			// if (!params || !params.appid){ $('body').msg('error',{text: '参数错误',level: 'top'}); return; }

			// 设置参数
			opts = params;
			$form = $('#formModPass');

			// 设置表单
			setForm();
		}
	};

	return moduleExport;

});