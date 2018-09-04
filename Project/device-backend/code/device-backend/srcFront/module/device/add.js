/*!
 *	模块 - 添加设备
 *	@Author Gang
 *	@Latest Update 2018-07-06
 *
 */

define(['jquery','Global','Menu','Validate','DTP'],function($,G,Menu,Validate,DTP){

	'use strict';

	var $form, $formImport, validate, validateImport, addType = 1;

	var setAddType = function(){

		var $addType = $('#addType'),
			$divType1 = $('#divType1'),
			$divType2 = $('#divType2');
			// $inputs = $form.find('input'),
			// $inputs1 = $form.find('.input-type1'),		// 单个录入 必填 inputs
			// $inputs2 = $form.find('.input-type2');		// 批量录入 必填 inputs

		$addType.on('change','input',function(){
			
			var val = $(this).val();

			// Update data
			addType = val;

			// Reset
			// $inputs.removeAttr("aria-required required");
			$divType1.removeClass('hide');
			$divType2.removeClass('hide');

			// 单个录入
			if (val == 1){
				$divType2.addClass('hide');
				// $inputs1.attr('required',true);
			}

			if (val == 2){
				$divType1.addClass('hide');
				// $inputs2.attr('required',true);
			}

		});
	};

	// 设置型号
	var setModel = function(){

		var uri = '/api/device/modelList';

		$('#model').setSelect({
			url: G.path + uri,
			id: 'code',
			name: 'name'
		});

		$('#model2').setSelect({
			url: G.path + uri,
			id: 'code',
			name: 'name'
		});
	};

	// 设置上传
	var setUpload = function(){

		$formImport.find('div.fileupload').fileupload();
	};

	// 设置表单验证
	var setValidate = function(){

		validate = $form.validate({
			rules: {
				pn_appsecret: {regExp: ''}
			},
			messages: {
				pn_appsecret: {regExp: ''}
			}
		});

		validateImport = $formImport.validate();

		// Datetime
		$('#productTtime').datetimepicker({
			format: 'yyyy-mm-dd',
			endDate: $.date(new Date()).ymd,
			minView: 2
		});
	};

	var setForm = function(){

		// 设置型号
		setModel();

		// 设置上传
		setUpload();

		// 录入方式
		setAddType();

		// 设置表单验证
		setValidate();

		// 表单提交
		$('#btnSubmit').on('click',function(){

			if (addType == 1){

				// 设置设备编号值
				// $('#devNumber').val($('#devMac').val().split(':').join(''));

				if (!validate.form()){ return; }
			}

			if (addType == 2){
				if (!validateImport.form()){ return; }

			}

			// if (!validate.form()){ return; }

			G.$page.msg('loading',{text: '添加中'});


			var uri = addType == 2 ? '/api/device/importDevice' : '/api/device/addDevice';
			var data = addType == 2 ? new FormData($formImport.get(0)) : $form.serialize();

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: data,
                processData: addType == 2 ? false : true,
                contentType: addType == 2 ? false : 'application/x-www-form-urlencoded',
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
		$formImport.on('submit',function(){ return false; });

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
			Menu.active('device/list');

			// 改变底色			
			G.$page.addClass('page-gray');

			// 设置参数
			$form = $('#formAdd');
			$formImport = $('#formImport');

			// 设置表单
			setForm();
		}
	}

	return moduleExport;

});