/*!
 *	模块 - 添加型号 （弹窗）
 *	@Author Gang
 *	@Latest Update 2018-07-06
 *
 */

define(['jquery','Extend','Global','Validate'],function($,Extend,G,Validate){

	'use strict';

	var ModelList = require('module/model/list');


	var opts = {}, validate, $form, $modal;


	var setForm = function(){

		// 设置验证表单
		validate = $form.validate();

		var uri = '/api/device/addDeviceType';

		// 表单提交
		$form.on('submit',function(){

			if (!validate.form()){ return; }

			$('body').msg('loading',{text: '提交中',level: 'top'});

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: $form.serialize(),
                success: function(json,status,xhr){

                	if (!G.checkJson(json,{level:'top'})){ return; }

                	$modal.hide();
                    $('body').msg('success',{text: '添加成功',level: 'top',time: 2000});

                    // 刷新表格
                    ModelList.refresh();

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
		onLoad: function($modelObj,params){
			// console.log('[Module][osConfig/finishPage] onLoad - params',params);

			// 检查参数			
			// if (!params || !params.appid){ $('body').msg('error',{text: '参数错误',level: 'top'}); return; }

			// 设置参数
			$modal = $modelObj;
			opts = params;
			$form = $('#formAddModel');

			// 设置表单
			setForm();
		}
	};

	return moduleExport;

});