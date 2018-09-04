/*!
 *	模块 - jquery extend
 *	@Author Gang
 *	@Latest Update 2018-02-02
 */

define(['jquery'],function($){

	'use strict';

	// 重写ajax，存入数组
	// var ajaxOrigin = $.ajax;

	// $.ajaxs = [];
	// $.extend({
	// 	ajax: [],
	// 	ajax: function(){
	// 		var args = Array.prototype.slice.call(arguments);
	// 	    var ajax = ajaxOrigin.apply(this, args);
	// 	    $.ajaxs.push(ajax);
	// 	    return ajax;
	// 	}
	// });

	// console.log($.ajaxs);

	$.extend({

		// Cookie
		cookie: function(key,value,options){

			if (typeof value === 'object' || typeof value === 'function'){ console.log('[Cookie] value can not be Object or Function'); return ''; }

			options = options ? options : {};

			// Write
			if (key && value != undefined){

				// Delete
				if (value === ''){
					options.expires = -1;
				}

				if (typeof options.expires === 'number') {
					var days = options.expires, t = options.expires = new Date();
					t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
				}

				document.cookie = [
					encodeURIComponent(key), '=', encodeURIComponent(value),
					options.expires ? '; expires=' + options.expires.toUTCString() : '',
					options.path    ? '; path=' + options.path : '',
					options.domain  ? '; domain=' + options.domain : '',
					options.secure  ? '; secure' : ''
				].join('');

				return '';
			}

			// Read
			if (key && value === undefined){

				var result = '',
					cookies = document.cookie ? document.cookie.split('; ') : [],
					len = cookies.length;

				for (var i=0; i<len; i++){
					var parts = cookies[i].split('=');

					if (key === decodeURIComponent(parts[0])){
						result = decodeURIComponent(parts[1]);
						break;
					}
				}
				return result;
			};

		},


		// Format date  @Gang 2016-06-18 10:00
		date: function(value,secs){
			var timeStamp = value || 0, oDate = value || {}, string = value || '', yy = '', mm = '', dd = '', h = '', m ='', s = '';

			if (value){
				if (typeof value === 'string'){ 						// String - YYYY-MM-DD HH:MM:SS
					oDate = new Date((value).replace(/-/g,"/"));
					timeStamp = oDate.getTime();
				}
				else if (typeof value === 'number'){					// TimeStamp
					oDate = new Date(value);
					timeStamp = value;
				}
				else if (typeof value === 'object'){					// Date Object
					oDate = value;
					timeStamp = oDate.getTime();
				}

				if (secs){
					timeStamp += secs * 1000;
					oDate = null;
					oDate = new Date(timeStamp);
				}

				yy = oDate.getFullYear();
				mm = oDate.getMonth()+1;
				dd = oDate.getDate();
				h = oDate.getHours();
				m = oDate.getMinutes();
				s = oDate.getSeconds();

				mm = (mm < 10)?'0'+mm : mm;
				dd = (dd < 10)?'0'+dd : dd;
				h = (h < 10)?'0'+h : h;
				m = (m < 10)?'0'+m : m;
				s = (s < 10)?'0'+s : s;

				string = yy+"-"+mm+"-"+dd+" "+h+":"+m+":"+s;
			}
			// console.log('oDate - '+oDate);
			// console.log('string - '+string);
			// console.log('timeStamp - '+timeStamp);

			return {timeStamp:timeStamp, stardard:oDate ,string:string, ymd: yy+'-'+mm+'-'+dd, year:yy, month:mm, day:dd, hour:h, minute:m, second:s};
		},

		// Transfer Duration (millisecond) to String @Gang 2016-05-16
		convertMS: function(ms,type){
			if (ms){
				if (typeof ms != 'number'){ ms = parseFloat(ms); }

				var days = Math.floor(ms/(86400000)),
					remainder1 = ms%(86400000),
					hours = Math.floor(remainder1/(3600000)),
					remainder2 = remainder1%(3600000),
					minutes = Math.floor(remainder2/(60000)),
					remainder3 = remainder2%(60000),
					seconds = Math.round(remainder3/1000);

				return (ms>=60000) ? (days===0?'':days+'天 ')+(hours===0?'':hours+'小时 ')+(minutes===0?'':minutes+'分钟 ') : (seconds===0?'':seconds+'秒');
			}
			else {
				return ms == 0 ? 0+'秒' : ms;
			};
		},

		// Encode text @Gang 2016-11-18 
		enCodeText: function(val){

			if (val && typeof val === 'string'){
				val = val.replace(/</ig,'&lt;');
				val = val.replace(/>/ig,'&gt;');
			}

			return val;
		},

		// unEncode text @Gang 2016-11-19 
		deCodeText: function(val){

			if (val && typeof val === 'string'){
				val = val.replace(/&lt;/ig,'<');
				val = val.replace(/&gt;/ig,'>');
			}
			
			return val;
		},

		// Check json string @Gang 2016-04-14
		isJson: function(str){
			return (str)? /^[\],:{}\s]*$/.test(str.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, "")) : false;
		},


		// Is Init
		isInit: function(str){

			return /^[0-9]+$/.test(str);
		},


		/*
		 *	[获取url参数]
		 */ 
		getAllParam: function(url){

			var url = url ? url : window.location.href;
			var p = url.split('?');
			p = p[1] ? p[1].split('#')[0] : '';
			p = p ? p.split('&') : [];
		    var len = p.length, kv, k, v, r = {};

		    if (len){
			    for (var i=0; i < len; i++) {
			    	if (!p[i]){ continue; }
			        kv = p[i].split("=");
			        k = kv[0];
			        v = kv[1];
			        if (kv.length > 2){
			        	kv.shift();
			        	v = kv.join('=');
			        }
			        r[k] = v ? decodeURIComponent(v) : '';
			    }

			    if (!url){ this.urlParameters = r; }	// 存入库属性，全局通用
		    }

		    return r;
		},


		/*
		 *	[获取url特定参数]
		 *
		 *	依赖 getAllParam()
		 */ 
		getParam: function(key){

			var params = this.urlParameters != undefined ? this.urlParameters : this.getAllParam();
			return key ? (params[key] ? params[key] : '') : '';
		}

	});


	/*
	 *	[Loading]
	 */ 
	$.fn.loading = function(isLoad){
		// console.log('page loading',isLoad);
		if (isLoad == false){
			$(this).find('>.page-load').remove();
			return;
		}

		var $load = $('<div class="page-load"><div class="page-mask-transparent"></div><div class="page-loading"><div class="icon-loading"></div></div></div>');
		$(this).append($load);
	};


	/*
	 *	[Show popup msg]
	 *
	 *	@ Author Gang 2018-03-10
	 *	@ type = 'loading', 'msg'(default), 'success', 'error', 'tip' , 'hide', 'page'
	 */
	$.fn.msg = function(type,options){

		if (Object.prototype.toString.call(type) === '[object Object]'){
			options = $.extend({},type);
			type = 'msg';	
		}

		var $that = $(this),
			type = type ? type : 'msg',
			icons = ['icon-loading','fa fa-warning','fa fa-times-circle','fa fa-check-circle'],
			defaults = {
				level: 'main',
				text: type == 'loading' && '数据加载中' || type,
				icon: type == 'loading' && icons[0] || type == 'tip' && icons[1] || type == 'error' && icons[2] || type == 'success' && icons[3] || '',
				mask: type == 'loading' ? true : false,
				time: 0,
				close: true		// Only for 'error' type
			},
			opts = options ? $.extend(defaults,options) : defaults;

		var clearMsg =  function(dt){
			var $t = $(this), dt = $t.data();
			if (dt.sto){ clearTimeout(dt.sto); }
			if (dt.msg){ dt.msg.remove(); }
			if (dt.mask){ dt.mask.remove(); }
			$t.data({sto: '', msg: '', mask: ''});
		};

		// Clear ------------------------------------
		clearMsg.call($that);
		if (type == 'hide'){ return; }

		// Add -------------------------------------
		var $msg = $([
				'<div class="msg msg-'+type+' '+(opts.level == 'top' ? 'msg-top' : '')+'">',
					'<div class="msg-cell">',
						'<div class="msg-main">',
							''+(opts.icon ? '<i class="msg-icon '+opts.icon+'"></i>' : '')+'',
							'<div class="msg-content"></div>',
						'</div>',
					'</div>',
				'</div>'
			].join('')),
			$msgCell = $msg.find('.msg-cell'),
			$msgContent = $msg.find('.msg-content'),
			$mask = opts.mask ? $('<div class="page-mask-msg '+(opts.level == 'top' ? 'top' : '')+'"></div>') : '',
			sto;

		$that.append($mask).append($msg).data({msg: $msg, mask: $mask});	// Add Dom, Save data
		$msgContent.html(opts.text);										// Set content
		$msgCell.css('top', -($msgCell.height()/2) + 'px');					// Set height
		$msg.show();														// Show

		// Timer close -----------------------------
		if (opts.time){ 
			sto = setTimeout(function(){ clearMsg.call($that); },opts.time);
			$that.data('sto',sto);
		}

		// Error close -----------------------------
		if (type == 'error' && opts.close){
			$msg.find('.msg-cell').on('click',function(e){
				clearMsg.call($that);
				e.preventDefault();
			});
		}

		// Page close ------------------------------
		if (type == 'page'){
			var $close = $('<div class="msg-close"><i class="fa fa-times"></i></div>');
			$msg.append($close);
			$close.on('click',function(e){
				clearMsg.call($that);
				e.preventDefault();
			});
		}

		return $that;
	};
	

	/*
	 *	[Window Resize]
	 *
	 *	@ Author Gang 2016-04-25
	 */
	$.fn.oResize = function(fn){
		var size = {
			width: $(window).width(),
			height: $(window).height()
		};

		var checkResize = function(){

			if (navigator.appVersion.match(/MSIE 7.0|MSIE 6.0|MSIE 8.0/i)){
				var w = $(window).width(), 
					h = $(window).height();

				if (w === size.width && h === size.height){
					return false;
				}
				else {
					size.width = w;
					size.height = h;
				}
			}
			else {
				size.width = $(window).width();
			}

			return true;
		};

		this.each(function(){

			if (this === window){
				$(this).resize(function(e){
					// console.log('Window resize');
					if (checkResize()){
						return fn.apply(this,[e,size.width]);
					}
				});
			}
			else {
				$(this).resize(fn);
			}
		});

		return this;
	};


	/*
	 *	[Build radios by ajax]
	 *
	 *	@ Author Gang 2018-03-19
	 */
	$.fn.setRadio = function(options){

		var that = this,
			$that = $(that),
			opts = $.extend({
				id: "id",
				name: "name",
				value: "",
				def: false
			},options);

		$.extend(opts,{
			dataType: 'json',
			cache: false,
			success: function(json){
				if (Object.prototype.toString.call(json) != '[object Object]'){ return; }
				if (Object.prototype.toString.call(json.data) != '[object Array]'){ return; }

				var html = '', data = json.data;

				for (var i=0, len=data.length; i<len; i++){
					html += '<label class="radio-inline"><input name="'+$that.attr('data-name')+'" type="radio" value="'+data[i][opts.id]+'" '+(opts.value == data[i][opts.id] ? 'checked' : '')+'><i></i><span>'+data[i][opts.name]+'</span></label>';
				}

				$that.html(html);

				if (opts.def){
					$that.find('input').first().prop('checked',true);
				}
			},
			error: function(){

			}
		});

		return $.ajax(opts);
	};


	/*
	 *	[Build select options by ajax]
	 *
	 *	@ Author Gang 2018-03-19
	 */
	$.fn.setSelect = function(options){

		var that = this,
			$that = $(this),
			dt = $that.data(),
			opts = $.extend({
				value: "",
				id: "id",
				name: "name",
				relate: [],
				text: dt.text ? dt.text : '',
				textNodata: dt.textnodata ? dt.textnodata : '无数据',
				def: true,
				group: false,
				onChangeAll: false,
				onChange: null
			},options);

		// Set onChange
		if (Object.prototype.toString.call(opts.onChange) === '[object Function]'){ 

		    $that.off('change').on('change',function(){

				var val = $(this).val() || '', data = val ? $(this).data(''+val+'') : '';

				// Set change
				if (val || (!val && opts.onChangeAll)){
					opts.onChange.call(that, val, data ? {id: data[opts.id], name: data[opts.name]} : {} );
				}

				// Set sub obj default
				var len = opts.relate.length;
				if (!val && len){
					for (var i=0; i<len; i++){
						if (opts.relate[i]){
							var $relate = $(opts.relate[i]);
							var text = $relate.data('emptytext');
							if (text != undefined){
								$relate.html('<option value="">'+text+'</option>');
							}
							else {
								$relate.html('');
							}
						}
					}
				}
			}); 
		};

		$.extend(opts,{
			dataType: "json",
			cache: false,
			success: function(json){

				if (Object.prototype.toString.call(json) != '[object Object]'){ return; }
				if (Object.prototype.toString.call(json.data) != '[object Array]'){ return; }

				var html, data = json.data, len = data.length, val, subData;

				// option default text
				html = opts.def ? '<option value="" selected>'+opts.text+'</option>' : '';

				// option data
				if (len){
					for (var i=0; i<len; i++){

						if (opts.group){
							html += '<optgroup label="'+data[i][opts.name]+'">';
							subData = data[i].data ? data[i].data : [];
							for (var j=0, subLen = subData.length; j<subLen; j++){
								html += '<option value="'+subData[j][opts.id]+'">'+subData[j][opts.name]+'</option>';
								$that.data(''+subData[j][opts.id]+'',subData[j]);
							}
							html += '</optgroup>';
						}
						else {
							html += '<option value="'+data[i][opts.id]+'">'+data[i][opts.name]+'</option>';
							$that.data(''+data[i][opts.id]+'',data[i]);
						}			
					};
				}
				else {
					html = '<option value="" selected>'+opts.textNodata+'</option>';
				}

				// Update dom
				$that.html(html);

				// Set default value
				if (opts.value != undefined){
					$that.val(opts.value);
				}

				// Value null && no default text, select first option
				if (!opts.value && !opts.def){
					$that.find('option').first().prop('selected',true);
				}

				// Trigger onChange
				$that.trigger('change');

				opts.onComplete && opts.onComplete(data);
			},
			error: function(xhr,stat,err){
				
			}
		});

		return $.ajax(opts);	
	};

	
	/*
	 *	[Fill data to elements]
	 *
	 *	e.g: <div name='filedName'></div> or <input name='filedName' /> or ......
	 *
	 *	@ Author Gang 2018-03-13
	 */
	$.fn.fillForm = function(data,options,fn){

		if (Object.prototype.toString.call(options) === '[object Function]'){
			fn = options;
			options = {};
		}

		var opts = $.extend(true,{
				fields: []
			},options),
			$form = $(this),
			fields = opts.fields;

		// Set fields
		if (!fields.length){
			for (var key in data){
				fields.push(key);
			}
		}

		for (var i=0, len = fields.length; i<len; i++){

			var field = fields[i];
			var val = data[field];

			if (Object.prototype.toString.call(field) === '[object Object]'){

				for (var key in field){
					val = field[key];
					field = key;
				}
			}

			var $field = $form.find('*[name="'+field+'"]');

			if (val === undefined){ continue; }
			if ($field.length){

				var tagName = $field.get(0).tagName;

				val = $.trim(val);
				val = val ? val : '';

				if (tagName == 'INPUT' || tagName == 'SELECT' || tagName == 'TEXTAREA'){

					var type = $field.attr('type');

					if (type == 'radio'){

						$field.filter('[value="'+val+'"]').prop('checked',true);
						continue;
					}

					if (type == 'checkbox'){

						var arr = val.split(',');
						for (var j=0, len2=arr.length; j<len2; j++){
							$field.filter('[value="'+$.trim(arr[j])+'"]').prop('checked',true);
						}
						continue;
					}

					$field.val(val);
				}

				else {

					// val = val.replace(/</ig,'&lt;').replace(/>/ig,'&gt;');

					// IMG
					if (tagName == 'IMG'){
						$field.attr('src',val);
						continue;
					}

					// Text
					$field.html(val);
				}
			}
		}

		fn && fn();

		return $(this);
	};


	/*
	 *	[Package form's data to json]
	 *
	 *	@ Author Gang 2016-07-01
	 */
	$.fn.packJson = function(options){
		var defaults = {
				type: 'object',			// string - json string, object - json object,
				checkbox: 'string',		// array - checkbox array, string - checkbox string
				sep: '.',				// Multi-Object split string
				nullVal: true,		    // Pack null value or not
				hidden: true,	        // Pack hidden element or not
				shown: true,		    // Pack not hidden element or not
				filter: []				// Filter elements ['element name']
			},
			opts = $.extend(defaults,options),
			form = $(this),
			json = {};

		// Set element value
		var setVal = function(val,dataType){	
			if (dataType === 'number'){
				val = (parseFloat(val)) ? parseFloat(val) : 0;
			}
			else if (dataType === 'array'){
				val = (val) ? val.split(',') : [];
			}
			else {}
			return val;
		};

		$(":input",form).each(function(i){

			var self = $(this),
				name = self.attr("name"), 
				type = self.attr("type"), 
				val = self.val(),
				checked = self.prop('checked');

			// Trim blank space
			if (type === 'text'){ val = $.trim(val); if (val === ''){ self.val(''); } }

			// Filter Button and Name is null and Filter list
			if (self.is('button') || !name || opts.filter.toString().indexOf(name)>=0){ return true; }

			// Filter Config for nullVal , hidden, shown , Data-ignore
			if ((!opts.shown && type != 'hidden') || (!opts.hidden && type === 'hidden') || (!opts.nullVal && (!val || ((type==='checkbox'||type==='radio')&&!checked)) ) || (self.attr('data-ignore')==='true'?true:false)){ return true; }

			// Set value data
			val = setVal(val,self.attr('data-type'));

			// Set field object and key;
			var obj = json, key = name;

			// Handle Multi-Object
			if (opts.sep){
				if (name.indexOf(opts.sep) > 0){
					var arr = name.split(opts.sep);
					for (var j=0, len=arr.length; j<len; j++){
						key = arr[j];
						if (j < (len-1)){
							if (!obj[key]){ obj[key] = {}; }
							obj = obj[key];
						}
					}
				}
			}

			// Push Value
			if (type === 'checkbox'){
				if (obj[key] === undefined){ obj[key] = opts.checkbox === 'array'? [] : ''; }
				if (checked){
					if (opts.checkbox === 'array'){
						obj[key].push(val);
					}
					else {
						var valSep = self.attr('data-sep');
						valSep = valSep === undefined ? ',' : valSep;
						obj[key] += (obj[key]!=''? valSep :'') + val;
					}
				}
			}
			else if (type === 'radio'){
				obj[key] = checked ? val : ( obj[key] === undefined ? '' : obj[key] );
			}
			else {
				obj[key] = val;
			}

		});

		var jsonStr = JSON.stringify(json);
		// console.log(jsonStr);
		// console.log(/^\{\}$/.test(jsonStr)? '' : (opts.type === 'string'? jsonStr : json));
		return /^\{\}$/.test(jsonStr)? '' : (opts.type === 'string'? jsonStr : json);
	};




	/* FILEUPLOAD PUBLIC CLASS DEFINITION
     * ================================= */

    var Fileupload = function(element, options) {
        this.$element = $(element);
        this.type = this.$element.data('uploadtype') || (this.$element.find('.thumbnail').length > 0 ? "image" : "file");
        this.$input = this.$element.find(':file');
        if (this.$input.length === 0) return;

        this.name = this.$input.attr('name') || options.name;

        this.$hidden = this.$element.find('input[type=hidden][name="' + this.name + '"]');
        if (this.$hidden.length === 0) {
            this.$hidden = $('<input type="hidden" />');
            this.$element.prepend(this.$hidden);
        };

        this.$preview = this.$element.find('.fileupload-preview');
        var height = this.$preview.css('height');
        if (this.$preview.css('display') != 'inline' && height != '0px' && height != 'none') this.$preview.css('line-height', height);

        this.$thumbnail = this.$element.find('.thumbnail');

        this.original = {
            'exists': this.$element.hasClass('fileupload-exists'),
            'preview': this.$preview.html(),
            'hiddenVal': this.$hidden.val()
        };

        this.$remove = this.$element.find('[data-dismiss="fileupload"]');

        this.$element.find('[data-trigger="fileupload"]').on('click.fileupload', $.proxy(this.trigger, this));

        this.listen();
    };

    Fileupload.prototype = {

        listen: function() {
            this.$input.on('change.fileupload', $.proxy(this.change, this));
            $(this.$input[0].form).on('reset.fileupload', $.proxy(this.reset, this));
            if (this.$remove) this.$remove.on('click.fileupload', $.proxy(this.clear, this));
        },

        change: function(e, invoked) {
            var file = e.target.files !== undefined ? e.target.files[0] : (e.target.value ? { name: e.target.value.replace(/^.+\\/, '') } : null);
            if (invoked === 'clear') return;

            if (!file) {
                this.clear();
                return;
            };

            this.$hidden.val('');
            this.$hidden.attr('name', '');
            this.$input.attr('name', this.name);

            // this.$preview.length > 0
            if (this.type === "image" && (typeof file.type !== "undefined" ? file.type.match('image.*') : file.name.match('\\.(gif|png|jpe?g)$')) && typeof FileReader !== "undefined") {
                var reader = new FileReader();
                var preview = this.$preview;
                var thumbnail = this.$thumbnail;
                var element = this.$element;

                reader.onload = function(e) {
                    // preview.html('<img src="' + e.target.result + '" ' + (preview.css('max-height') != 'none' ? 'style="max-height: ' + preview.css('max-height') + ';"' : '') + ' />');
                    // thumbnail.html('<img src="' + e.target.result + '" ' + (preview.css('max-height') != 'none' ? 'style="max-height: ' + preview.css('max-height') + ';"' : '') + ' />').show();
                    thumbnail.html('<img src="' + e.target.result + '" />').show();
                    preview.text(file.name);
                    element.addClass('fileupload-exists').removeClass('fileupload-new').addClass('fileupload-thumbnail');
                };

                reader.readAsDataURL(file);
            } else {
                this.$thumbnail.hide();
                this.$preview.text(file.name);
                this.$element.addClass('fileupload-exists').removeClass('fileupload-new').removeClass('fileupload-thumbnail');
            };

            this.$input.focus();
        },

        clear: function(e) {
            this.$hidden.val('');
            this.$hidden.attr('name', this.name);
            this.$input.attr('name', '');

            //ie8+ doesn't support changing the value of input with type=file so clone instead
            if (/msie/.test(navigator.userAgent.toLowerCase())) {
                var inputClone = this.$input.clone(true);
                this.$input.after(inputClone);
                this.$input.remove();
                this.$input = inputClone;
            } else {
                this.$input.val('');
            };

            this.$thumbnail.html('').hide();
            this.$preview.html('');
            this.$element.addClass('fileupload-new').removeClass('fileupload-exists').removeClass('fileupload-thumbnail');

            if (e) {
                this.$input.trigger('change', ['clear']);
                e.preventDefault();
            };
        },

        reset: function(e) {
            this.clear();

            this.$hidden.val(this.original.hiddenVal);
            this.$preview.html(this.original.preview);

            if (this.original.exists) { this.$element.addClass('fileupload-exists').removeClass('fileupload-new') } else { this.$element.addClass('fileupload-new').removeClass('fileupload-exists') };
        },

        trigger: function(e) {
            this.$input.trigger('click');
            e.preventDefault();
        }
    };


    /* FILEUPLOAD PLUGIN DEFINITION
     * =========================== */

    $.fn.fileupload = function(options) {
        return this.each(function() {
            var $this = $(this),
                data = $this.data('fileupload');
            if (!data) $this.data('fileupload', (data = new Fileupload(this, options)));
            if (typeof options == 'string') data[options]();
        });
    };

    $.fn.fileupload.Constructor = Fileupload;

});
