/*!
 *	模块 - 新建脚本更新任务
 *	@Author Gang
 *	@Latest Update 2018-08-01
 *
 */

define(['jquery','Global','Menu','Validate','Grid','DTP'],function($,G,Menu,Validate,Grid){

	'use strict';

	var opts = {}, 
		validate, 
		$form,
		$formSearch,
		$taskError,
		table,
		selectDev = {},
		selectType = 0;


	// 设置当前选择结果
	var setSelectRes = function(res){

		if (res){
			// Save table select data
			selectDev = res;
			selectDev.checkTotal = res.checkAll ? res.total - res.unchecked.length : res.checked.length;
		}
		else {
			selectDev.checkTotal = 0;
		}

		// Set total
		$('#selectedTotal').text(selectDev.checkTotal);

		console.log(selectDev);
	};

	var setTable = function(params){

		var $table = $('#devTable'),
			uri = '/api/device/list';

		// Table
		table = $table.grid({
			autoLoad: false,
			url: G.path + uri,
			data: params,
			pageSize: 10,
			checkbox: true,
			checkAll: true,
			checkRange: 'all',
			columns: [
				{data: 'orderNo',class: 'td-sn',render: function(d,r,i,ss){ return ss.pageSize*(ss.page-1)+i; }},
				{data: 'number'},
				{data: 'model'},
				{data: 'bind', render: function(d,r,i){

					return r.bindAccount ? '<span title="绑定时间：'+$.date(r.bindTime).ymd+'">' + r.bindAccount + '</span>' : '未绑定';
				}},
				{data: 'isOnlined', class: 'txt-c', render: function(d,r,i){
					return d ? '是' : '否';
				}},
				{data: 'belongUnitId', render: function(d,r,i){
					return d === 1 && '天智星' || d === 0 && '未知' || '';
				}}
			],
			onCheck: function(res){

				// 设置当前选择结果
				setSelectRes(res);
			},
			onSuccess: function(dataHash,json){

				if (!G.checkJson(json)){ return; }

				// 设置当前选择结果
				setSelectRes(table.getCheckData());

				$('#tableCheckType').removeAttr('disabled');
			},
			onError: function(xhr, textStatus){
				$('body').msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
			}
		});

	};

	var resetTable = function(){

		$('#tableCheckType').prop('disabled',true);

		// Table clear
		table.clear();

		// Data reset
		selectDev = {};

		// 设置当前选择结果
		setSelectRes(0);
	};


	var setTableSelectMode = function(){

		$('#tableCheckType').on('click', function(){

			// 设置表格选择模式 - 全选/反选
			table.setSelectMode($(this).prop('checked'));

			// 设置当前选择结果
			setSelectRes(table.getCheckData());
		});
	}

	// 表格、搜索
	var setSearch = function(){


		// 设置表格
		setTable();

		// 设置数据全选/反选
		setTableSelectMode();

		// 设置搜索
		$formSearch.on('submit',function(){

			var data = $formSearch.packJson({nullVal:false});
			
			table.load(data);

			return false;
		});

		$formSearch.on('click','button[data-btn=clear]',function(){

			$formSearch.get(0).reset();

			// Table clear
			resetTable();
		});
	};


	//
	var setOptions = function(){

		// 任务配置 - 脚本
		var uri_p = G.path + '/api/shell/shellNamelist';

		$("#shellId").setSelect({
			url: uri_p, 
			id: 'shellId', 
			name: 'shellName'
		});


		// 搜索 - 型号
		var uri_m = G.path + '/api/device/modelList';

		$('#model').setSelect({
			url: uri_m,
			id: 'code',
			name: 'name'
		})
	};

	// 选择切换
	var setToggle = function(){

		// 升级方式切换
		$('#upgradeType').on('change',function(){

			var val = $(this).val();

			$('#append').toggleClass('hide', val);

			if (val == 0){
				$form.find('input[name=append]').prop('checked',true).eq(2).prop('checked', false);
			}
		});

		// 选择设备切换
		$('#selectType').on('change','input',function(){

			var val = $(this).val();

			selectType = val;

			$('#taskDevInput').toggleClass('hide',val == 1);
			$('#taskDev').toggleClass('hide',val == 0);
		});
	};

	// 表单验证
	var setValidate = function(){

		validate = $form.validate();
	};

	// 检查、封装数据
	var getData = function(){

		var res = null;

		// 输入方式
		if (selectType == 0){

			var $devs = $('#devs'), val = $devs.val();

			// 为空
			if (!val){

				G.$page.msg({text: '请输入设备编号',time: 3000});
				return res;
			}

			// 格式不对
			var reg = /^([\r\n\s ,，]*[0-9a-z]{14}[\r\n\s ,，]*)([\r\n\s ,，]+[0-9a-z]{14}[\r\n\s ,，]*)*$/;
			if (!reg.test(val)){

				G.$page.msg({text: '设备编号格式有误',time: 3000});
				return res;
			}

			// 封装数据
			var formData = $form.packJson();

			val = val.replace(/[\r\n\s ,，]+/g,'\n').replace(/(^[\r\n\s ,，]+)|([\r\n\s ,，]+$)/,'');
			formData['devNums'] = val.replace(/[\r\n\s ,，]+/g,',');
			
			return formData;
		}

		// 搜索方式
		if (selectType == 1){

			if (!selectDev.checkTotal){

				G.$page.msg({text: '至少选择一台设备',time: 3000});
				return res;
			}

			// 封装数据
			var formData = $.extend({}, $form.packJson(), $formSearch.packJson({nullVal: false})),
				devIds = [],
				devArr = selectDev.checkAll ? selectDev.unchecked : selectDev.checked;

			for (var i=0,len=devArr.length; i<len; i++){
				devIds.push(devArr[i].id);
			}

			formData['selectType'] = selectDev.checkAll ? 1 : 0;
			formData['devIds'] = devIds.join(',');
			
			return formData;
		}

		return res;
	};

	// Set form
	var setForm = function(data){

		setOptions();
		setToggle();
		setSearch();
		setValidate();
		setError();

		// 表单提交
		$form.on('submit',function(){

			if (!validate.form()){ return false; }

			var data = getData();

			if (!data){ return false; }

			if (!confirm('确定提交任务？')){ return false; }

			G.$page.msg('loading',{text: '添加中'});

			var uri = selectType == 1 ? '/api/shell/addTaskBySearch' : '/api/shell/addTaskByDevids';

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: data,
                dataType: 'json',
                success: function(json){

                	if (json.code == 10201 && json.data){
                		G.$page.msg('hide');
                		showError(json.data);
                		return;
                	}

                	if (!G.checkJson(json)){ return; }

                    G.$page.msg('success',{text: '新建成功',time: 3000});

                    window.history.back();
                },
                error: function(xhr, textStatus){
                	G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
                }
            });

			return false;
		});

		$('#btnSubmit').on('click',function(){ $form.submit(); })
		
	};

	// Set error
	var setError = function(){

		$taskError.on('click','.close',function(){

			$(this).parent().addClass('hide').find('#notexist,#notmatch').addClass('hide').find('info').text('');
		});
	};

	// Show error
	var showError = function(data){

		var d1 = data.error_devnums,
			d2 = data.NoPath_devnums;

		d1 = d1 ? d1 : [];
		d2 = d2 ? d2 : [];

		$taskError.removeClass('hide');
		$taskError.find('#notexist').toggleClass('hide',!d1.length).find('.info').text(d1.join(', '));
		$taskError.find('#notmatch').toggleClass('hide',!d2.length).find('.info').text(d2.join(', '));
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
			$form = $('#formAdd');
			$formSearch = $('#formSearch');
			$taskError = $('#taskError');

			// 设置表单
			setForm();
		}
	}

	return moduleExport;

});