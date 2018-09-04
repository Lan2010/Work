/*!
 *	模块 - 设备详情
 *	@Author Gang
 *	@Latest Update 2018-07-09
 *
 */

define(['jquery','Global','Menu','Validate','DTP'],function($,G,Menu,Validate){

	'use strict';

	var opts = {}, 
		validate, 
		validateParam,
		validatePass,
		$form, 
		$formParam,
		$formPass,
		$infoBody,
		$paramsBody;

	// 获取详情
	var getDetail = function(fn){

		if (!opts.id){ return; }

		$infoBody.loading();

		var uri = '/api/device/list';

		$.ajax({
			url: G.path + uri,
			data: {id: opts.id},
			cache: false,
			success: function(json){
				
				if (!G.checkJson(json)){ return; }
				
				var data = json.data[0],
					online = data.onlineStatus;

				$form.fillForm(data,{
					fields: [
						'id',
						'number',
						'mac',
						'devPasswd',
						{'bindAccount': data.bindAccount ? data.bindAccount : '未绑定'},
						{'bindTime': data.bindTime ? $.date(data.bindTime).string : ''},
						'model',
						{'onlineStatus': online == 1 && '<span class="font-green">在线</span>' || online == 0 && '<span class="font-gray">离线</span>' || '<span class="font-gray">--</span>'},
						{'millisecond': data.millisecond ? $.convertMS(data.millisecond) : ''},
						{'belongUnitId': data.belongUnitId == 1 ? '天智星' : '未知'},
						{'addTime': data.addTime ? $.date(data.addTime).string : ''},
						{'productTtime': data.productTtime ? $.date(data.productTtime).ymd : ''},
						{'isOnlined': data.isOnlined ? '<span class="font-green">是</span>' : '<span class="font-gray">否</span>'}
					]
				});

				$('#devDurationLabel').text(data.onlineStatus ? '在线时长' : '离线时长')

				fn && fn(data);

				$infoBody.loading(false);	
			},
			error: function(xhr, textStatus){
				$infoBody.loading(false);
				G.pageError(uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus);
			}
		});
	};

	var setFormInfo = function(data){

		if (data.isOnlined || data.bindAccount || data.onlineStatus){ return; }

		// Open form
		$form.find('input').removeClass('input-read')
		$form.find('.eitable').removeAttr('readonly');
		$('#devBtn').removeClass('hide');

		// Datetime
		$('#productTtime2').datetimepicker({
			format: 'yyyy-mm-dd',
			endDate: $.date(new Date()).ymd,
			minView: 2
		});

		// Set validate
		validate = $form.validate();

		var uri = '/api/device/updateDevice';

		// Set form
		$form.on('submit',function(){

			if (!validate.form()){ return; }

			G.$page.msg('loading',{text: '修改中'});

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: $form.serialize(),
                success: function(json){

                	if (!G.checkJson(json)){ return; }

                    G.$page.msg('success',{text: '修改成功',time: 2000});

                },
                error: function(xhr, textStatus){
                	G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
                }
            });

			return false;
		});
	};



	// 获取设备参数
	var getParams = function(data){

		if (!data.number){ return; };

		if (!data.onlineStatus){

			$paramsBody.html('<div class="font-gray">只有在线设备才能获取设备参数</div>');

			return;
		};

		$paramsBody.loading();

		$('#devNo').val(data.number);

		var uri = '/api/device/getDeviceDetail';

		$.ajax({
			url: G.path + uri,
			data: {dev_num: data.number},
			cache: false,
			success: function(json){
				
				$paramsBody.loading(false);

				if (!G.checkJson(json)){ return; }
				
				var data = json.conf;

				setParams(data);
				setFormParams(data);
			},
			error: function(xhr, textStatus){
				$paramsBody.loading(false);
				G.pageError(uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus);
			}
		});
	};

	// Set params data
	var setParams = function(data){

		// Build dom eles
		var html = '',
			rules = {},
			messages = {};	

		for (var i=0, len=data.length; i<len; i++){

			var item = data[i];

			// 设置表单字段名 name
			var name = item.devParam.replace('@','');
			data[i].name = name;

			// 生成 Dom html
			var cell = [
				'<div class="cell">',
					'<div class="hd '+(item.required == 1? 'required' : '')+'"><label>'+(item.viewName ? item.viewName : item.devParam)+'</label></div>',
					'<div class="bd">',
						'<div class="control">',
							''+createEle(item)+'',
						'</div>',
						''+(item.describe ? '<span class="prompt">'+ item.describe +'</span>' : '')+'',
					'</div>',
				'</div>'
			].join('');

			html += cell;

			// 定义验证规则
			rules[name] = {};
			messages[name] = {};
			if (item.regExp){

				rules[name]['regExp'] = item.regExp;
				messages[name]['regExp'] = item.regExpTip;
			}
			if (item.rule){

				var rule = JSON.parse(item.rule);

				for (var key in rule){
					rules[name][key] = rule[key];
				}
			}
		}

		// Set dom html
		$paramsBody.html(html);

		// Set form validate
		validateParam = $formParam.validate({
			rules: rules,
			messages: messages
		});

		// console.log(rules);
	};

	var createEle = function(data){

		var html = '', 
			name = data.name,
			field = data.devParam,
			val = data.value || '',
			rw = data.rw,
			required = data.required,
			type = data.inputType,
			options = data.options ? data.options : '', 
			len = 0;

			// Set options to object
			if (options){
				if ($.isJson(options)){
					options = $.parseJSON(options);
					len = options.length;
				}
			}

			// Readonly
			if (rw === 'r'){ 

				if ( options && (type === 'select' || type === 'radio')){
					for (var i=0; i<len; i++){ if (options[i].value == val){ val = options[i].name; break; } }
				}

				return '<span class="input-read">'+$.enCodeText(val)+'</span>';
			}

			if (rw != 'rw'){ return ''; }

			// Input
			if (type === 'text'){ 
				html = '<input name="'+name+'" data-field="'+field+'" type="text" class="input" value="'+val+'" '+(required == 1?'required':'')+'>';
			}

			// Select
			else if (type === 'select' && len){

				html += '<select name="'+name+'" data-field="'+field+'" class="input" '+(required == 1?'required':'')+'>';
				html += '<option value="">请选择</option>';
				for (var i=0; i<len; i++){ 
					html += '<option value="'+options[i].value+'" '+(options[i].value==val?'selected':'')+'>'+options[i].name+'</option>'; 
				}
				html += '</select>';
			}

			// Radio
			else if (type === 'radio' && len){ 

				for (var i=0; i<len; i++){
					html += '<label class="radio-inline"><input name="'+name+'" data-field="'+field+'" type="radio" value="'+options[i].value+'" '+(options[i].value==val?'checked':'')+' '+(required == 1?'required':'')+'><i></i><span>'+options[i].name+'</span></label>';
				}
			}

			// Checkbox
			else if (type === 'checkbox' && len){ 

				for (var i=0; i<len; i++){
					html += '<label class="checkbox-inline"><input name="'+name+'" data-field="'+field+'" type="checkbox" value="'+options[i].value+'" '+(options[i].value==val?'checked':'')+' '+(required == 1?'required':'')+'><i></i><span>'+options[i].name+'</span></label>';
				}
			}

			// Error
			else {
				html = '<input name="'+name+'" data-field="'+field+'" type="text" class="input" value="'+val+'">';
			}

			return html;
	};

	// Pack params data
	var packParamData = function(){

		var devNo = $('#devNo').val();
		var data = {
			dev_num: devNo,
			conf: []
		};

		$formParam.find('input,select,textarea').each(function(i){

			var $self = $(this),
				type = $self.attr("type"),
				name = $self.attr('name'),
				o = $self.data('field'),
				v = $self.val(),
				checked = $self.prop('checked');

			var isValid = false;

			if (name){

				// Input radio
				if (type === 'radio' || type === 'checkbox'){

					var $inputs = $formParam.find('input[name="'+name+'"]');

					if ($inputs.index($self) === 0){

						var $checkeds = $inputs.filter(':checked');

						if (type === 'radio'){
							v = $checkeds.length ? $checkeds.val() : '';
						}

						if (type === 'checkbox'){

							var vv = [];
							$checkeds.each(function(){
								vv.push($(this).val());
							});

							v = vv.join(',');			
						}

						isValid = true;
					}
				}

				else {
					isValid = true;
				}
			}
			
			// Set
			if (isValid){

				v = v.replace(/\\/g,'\\\\').replace(/"/g,'\\"');

				data.conf.push({o: o,v: v});
			}
		});

		return JSON.stringify(data);
	};

	// Set form param
	var setFormParams = function(){

		// Open form
		$('#devBtnParam').removeClass('hide');

		var uri = '/api/device/setConfig';

		// Set form
		$formParam.on('submit',function(){

			if (!validateParam.form()){ return; }

			G.$page.msg('loading',{text: '提交中'});

			var data = packParamData();

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: data,
                contentType: 'application/json',
                success: function(json){

                	if (!G.checkJson(json)){ return; }

                    G.$page.msg('success',{text: '修改成功',time: 2000});

                },
                error: function(xhr, textStatus){
                	G.$page.msg('error',{text: uri + ' - ' + xhr.status + ' - ' + xhr.statusText + ' - ' + textStatus});
                }
            });

			return false;
		});
	};

	// Set form
	var setForm = function(data){

		setFormInfo(data);
		
	};


	var setFormPass = function(data){

		if (!data.id){ return; }

		$('#devNum').val(data.id);

		// Set validate
		validatePass = $formPass.validate();

		var uri = '/api/device/setPasswd';

		// Set form
		$formPass.on('submit',function(){

			if (!validatePass.form()){ return; }

			G.$page.msg('loading',{text: '修改中'});

			$.ajax({
                url: G.path + uri,
                type: 'post',
                data: $formPass.serialize(),
                success: function(json){

                	if (!G.checkJson(json)){ return; }

                    G.$page.msg('success',{text: '修改成功',time: 2000});

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
			// console.log('[Module][oaMgt/modify] onSearch - params',params);
		},

		// Module onLoad
		onLoad: function(path,params){
			// console.log('[Module][oaMgt/modify] onLoad - params',params);

			// 激活菜单
			Menu.active('device/list');

			// 改变底色		
			G.$page.addClass('page-gray');

			// 检查参数			
			if (!params || !$.isInit(params.id)){ G.pageError('参数错误'); return; }

			// 设置参数
			opts = params;
			$form = $('#formMod');
			$formParam = $('#formParam');
			$formPass = $('#formPass');
			$infoBody = $('#infoBody');
			$paramsBody = $('#paramsBody');

			// 获取详情，设置表单
			getDetail(function(data){ 
				setForm(data); 
				getParams(data);
				setFormPass(data);
			});
		}
	}

	return moduleExport;

});