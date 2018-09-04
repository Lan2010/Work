/*!
 *	[App 入口]
 *	@Author Gang
 *	@Latest Update 2018-07-03
 */


/*
 *	[定义模块 - Global]
 *
 *	Depend: jquery, $.fn.msg, 
 *	Export: 1.参数 2.全局方法
 */
define('Global',['jquery','Extend'],function($){

	'use strict';

	var $page = $('#pageMain'),
		$side = $('#pageSide'),
		$content = $('#pageContent'),
		path = window.path != undefined ? window.path : '/stardev',
		apiPath = window.apiPath != undefined ? window.apiPath : '/stardev/api',
		error = false,
		nickName = $.cookie('nickName');

	var checkBrowser = function(){

		var res = navigator.appVersion.match(/MSIE 7.0|MSIE 6.0|MSIE 8.0|MSIE 9.0|MSIE 10.0/i);
		if (res){
			$('body').msg('tip',{text: '尊敬的用户，你好！ 本系统不支持IE10及以下的浏览器，请升级你的浏览器。推荐谷歌、火狐浏览器！', mask: true, close: false, level: 'top'});
			$side.remove();
			$page.remove();
		}
		return !res
	};

	var checkJson = function(json,opts){

		var res = true,
			showMsg = true,
			options = {},
			msg = '';

		if (Object.prototype.toString.call(opts) === '[object Object]'){
			$.extend(options,opts);
		}

		if (Object.prototype.toString.call(opts) === '[object Boolean]'){
			showMsg = opts;
		}
		
		var overtime = json.code == 10101 || json.code == 10302;

		// Set msg ---------------------------------------------------
		if (Object.prototype.toString.call(json) != '[object Object]'){

			msg = '数据非对象格式';
			res = false;
		}

		// Set msg ---------------------------------------------------
		if (json.code != 0){

			msg = json.msg ? json.msg : json.code;
			if (overtime){ msg = '登录超时，2秒后请重新登录'; }

			res = false;
		}


		// 弹出消息提示
		if (!res && showMsg){
			var $wrap = options.level == 'top' ? $('body') : $page;
			$wrap.msg('tip',$.extend({text: msg, mask: overtime?true:false, time: overtime?0:3500},options));
		}

		// 2秒重新登陆
		if (overtime){
			setTimeout(function(){
				window.location.replace(path+'/');
			},2000);
		}

		return res;
	};

	var pageError = function(msg){
		error = true;
		$page.msg('error',{text: msg, mask: true, close: false});
		return this;
	};

	var pageReset = function(){
		$page.removeClass('page-gray');
		$('div.datetimepicker,span.select2-hidden-accessible,div.select2-drop').remove();
		error && $page.msg('hide');
		error = false;
		return this;
	};

	// Global ajax setup
	$.ajaxSetup({
        dataType: 'json',
	    error: function(event,xhr,options,err){
	    	
	    	console.log('=======================================');
			console.log("[ Ajax Error ("+xhr.status+" / "+err+") ]");
			console.log("url:         \""+options.url+"\"");
			console.log("data:        \""+options.data+"\"");
			console.log("type:        \""+options.type+"\"");
			console.log("contentType: \""+options.contentType+"\"");
			console.log("dataType:    \""+options.dataType+"\"");
			console.log('=======================================');

	    }
	});

	return {
		$side: $side,
		$page: $page,
		$content: $content,
		path: path,
		api: {path: apiPath},
		nickName: nickName,
		checkBrowser: checkBrowser,
		checkJson: checkJson,
		pageReset: pageReset,
		pageError: pageError
	}

});


/*
 *	[定义模块 - Modal Window]
 *	
 *	1. 说明：模态框模块，结合 requirejs 调用模块(html,js) 显示页面内容
 *	1. 依赖：jquery, $.loading, $.oResize
 *	2. 调用：
 *		Modal.show({
 *			width: 720,			// 宽度
 *			minHeight: 200,		// 最小高度 （加载模块前）
 *			title: '标题',		// 标题
 *			path: '',			// require 路径 （基于require配置）
 *			js: true,			// 是否加载Js模块
 *			data: {}			// 传递参数：data 将在模块加载完默认执行 onLoad 方法时作为输出参数 - onLoad($modal,data)
 *		})
 */
define('Modal',['jquery','Extend'],function($){

	'use strict';

	var $modal, $win = $(window), $body = $('body');

	var initTpl = function(opts){

		var top = ($win.height() - opts.minHeight)/2 - opts.initMove;

		var $tpl = $([
			'<div class="modal">',
				'<div class="page-mask-modal"></div>',
				'<div class="modal-cell">',
					'<div class="modal-main transform modal-default" style="width: '+opts.width+'px; top: '+(top > 0 ? top : '0')+'px;">',
						'<div class="modal-header">',
							'<div class="title">'+opts.title+'</div>',
							'<div class="close" title="关闭"><i class="fa fa-times"></i></div>',
						'</div>',
						'<div class="modal-body" style="min-height: '+(opts.minHeight-48)+'px;"></div>',
					'</div>',
				'</div>',
			'</div>'
		].join(''));

		// Append tpl
		$body.append($tpl);

		return $tpl;
	};

	// Set modal events
	var setEvents = function(){

		var $this = this;

		$this.on('click','.close',function(){
			hide();
		});

	};

	// Set modal postion
	var setPostion = function(){

		var $this = this,
			top = $win.height() - $this.main.outerHeight(),
			ww = $win.width(),
			w = ww <= $this.options.width ? ww : $this.options.width;

		top = top >= 0 ? top/2 : 20;
		$this.main.css({top: top + 'px',width: w + 'px',opacity: 1});
	};

	// Hide modal
	var hide = function(){

		$modal.remove();
		$body.removeClass('body-modal');
		$win.off('resize');
		$modal = null;
	};

	// New modal and show
	var show = function(options){

		var opts = $.extend({
			width: 720,
			minHeight: 200,		// 初始化最小高度
			initMove: 200,		// 默认居中后上移的距离
			title: '标题',
			path: '',
			js: true,
			data: {}
		},options);

		// Body Overflow hidden
		$body.addClass('body-modal');

		// Init modal
		$modal = initTpl(opts);

		// Set modal prop
		$.extend(true,$modal,{options: opts, body: $modal.find('.modal-body'), main: $modal.find('.modal-main')});

		// Set modal events
		setEvents.call($modal);

		// Set postion
		setPostion.call($modal);

		// Loading
		$modal.body.loading();

		// Require html, js
		if (opts.path){

			// Require html
			require(['text!module/' + opts.path + '.html'],function(html){			

				// Dom append html
				$modal.body.html(html);

				// Reset pos
				setPostion.call($modal);

				// Set window resize
				$win.oResize(function(){ setPostion.call($modal); });

				// When no js
				if (!opts.js){ 
					// Reset pos
					setTimeout(function(){ setPostion.call($modal); },50);
					return; 
				}

				// Require js
				require(['module/'+opts.path],function(page){

					// Module onLoad
					page.onLoad && page.onLoad(opts.path,opts.data);

					// Reset pos
					setTimeout(function(){ setPostion.call($modal); },50);
				});

			},function(err){												// 404
				console.log('[Modal] require html error',err.xhr.status);
				$modal.body.loading(false);
			});

		}
	};

	return {
		show: show,
		hide: hide
	}
});


/*
 *	[定义模块 - Header]
 *
 *	Depend: jquery, Global
 *	Export: logout
 */
define('Header',['jquery','Global','Modal'],function($,G,Modal){

	'use strict';


	// 获取用户信息
	// var getuser = function(){

	// 	$.ajax({
	// 		url: G.path + '/api/user',
	// 		cache: false,
	// 		success: function(json){

	// 			if (!G.checkJson(json)){ return; }

	// 			$('#headerNickName').text(json.data.user_name ? json.data.user_name : '欢迎');

	// 			G.nickName = json.data.user_name;
	// 		},
	// 		error: function(xhr){
	//         	$('body').msg('error',{text: xhr.status + ' - ' + xhr.statusText,level: 'top'});
	//         }
	// 	});
	// };

	// 退出
	var logout = function(){

		$.ajax({
			url: G.path + '/api/logout',
			cache: false,
			success: function(json){

				if (!G.checkJson(json)){ return; }

				window.location.replace(G.path+'/');
			},
			error: function(xhr){
            	$('body').msg('error',{text: xhr.status + ' - ' + xhr.statusText,level: 'top'});
            }
		});

	};

	// Logo
	$('#logo').on('click',function(){ window.location.href = './'; });

	// 退出
	$('#logout').on('click',logout);

	// 修改密码
	$('#btnModPass').on('click',function(){

		Modal.show({
			width: 600,
			title: '你的登录密码',
			path: 'user/pass'
		});
	});

	$('#headerNickName').text(G.nickName ? G.nickName : '欢迎');

	// 获取用户信息
	// getuser();

	// Gulp publish version
	// $('head').attr('data-publish-version',window.version);

	return {
		logout: logout
	}
});


/*
 *	[定义模块 - Menu]
 *
 *	Depend: jquery
 *	Export: active
 */
define('Menu',['jquery','Global'],function($,G){

	'use strict';

	var $menu = $('#pageMenu');

	// 激活菜单
	var active = function(path){

		$menu.find('.active').removeClass('active');

		if (path){
			$menu.find('a[href^="#'+path.replace(/\//g,'\\/')+'"]').addClass('active').parent().parent().parent().addClass('open active');
		}
	};

	// 菜单交互
	var setMenu = function(){

		// 获取菜单数据
		// $.ajax({
		// 	url: G.path + '/api/login/menu',
		// 	cache: false,
		// 	success: function(json){

		// 		if (!G.checkJson(json)){ return; }

		// 		// 渲染菜单
		// 		var data = json.data, html = '';

		// 		if (!data){ return; }

		// 		for (var i=0, len=data.length; i<len; i++){
					 

		// 			html += '<li>';
		// 			html += 	'<div class="node"><span class="icon"><i class="fa fa-'+data[i].icon+'"></i></span>'+data[i].name+'<span class="arrow fa"></span></div>';
		// 			html += 	'<ul>';

		// 			if (data[i].sub){

		// 				for (var j=0, len2=data[i].sub.length; j<len2; j++){
		// 						html += '<li><a href="#'+data[i].sub[j].url+'">'+data[i].sub[j].name+'</a></li>';
		// 				}
		// 			}
					
		// 			html += 	'</ul>';
		// 			html += '</li>';
		// 		}

		// 		$menu.find('ul').html(html);

		// 	},
		// 	error: function(xhr){
  //           	$('body').msg('error',{text: xhr.status + ' - ' + xhr.statusText,level: 'top'});
  //           }
		// })
		
		$menu.on('click','.node',function(){

			var $li = $(this).parent(),
				$opend = $menu.find('.open');

			// Close
			// $opend.find('ul').slideUp('fast',function(){
			// 	$opend.removeClass('open');
			// });

			if ($li.hasClass('open')){ 

				$li.find('ul').slideUp('fast',function(){
					$li.removeClass('open');
				});
				return; 
			}

			// Open
			$li.find('ul').slideDown('fast',function(){
				$li.addClass('open');
			});
		});

	};

	// 初始化
	setMenu();

	return {
		active: active
	}
});


/*
 *	[定义模块 - Main Toolbar search]
 */
define('TSearch',['jquery'],function($){

	'use strict';

	// 过滤显示切换
	var toggleFilter = function(isOpen){

		$('#tFilter').css('display', isOpen ? 'block' : 'none');
		$('#tFormSearch').find('.adv').toggleClass('open',isOpen ? true : false);
	}

	// 初始化交互
	var init = function(options){

		var $form1 = $('#tFormSearch'),
			$filter = $('#tFilter'),
			$form2 = $('#tFormFilter'),
			hasFilter = $form2.length;

		// 点击搜索按钮
		$form1.on('submit',function(){

			if (hasFilter){ $form2.get(0).reset(); }
			var data = $form1.packJson({nullVal:false});
			options.onSearch && options.onSearch(data);
			// console.log(data);
			$form1.find('.clear').toggleClass('hide',!data);
			return false;
		});

		// Clear
		$form1.find('.clear').on('click',function(){

			$(this).addClass('hide');

			$form1.get(0).reset();
			var data = hasFilter ? $form2.packJson({nullVal:false}) : {};
			options.onClear && options.onClear(data);
		});

		// Keyword
		$form1.find('input[data-name=keyword]').on('focus',function(){
			$(this).parent().addClass('keyword-focus');
		}).on('blur',function(){
			$(this).parent().removeClass('keyword-focus');
		});

		// 过滤显示切换
		$form1.find('span[data-toggle=filter]').on('click',function(){

			toggleFilter($filter.css('display') == 'none' ? true : false);
		});

		if (hasFilter){
			// 点击过滤
			$form2.on('submit',function(){

				options.onFilter && options.onFilter($.extend($form1.packJson({nullVal:false}),$form2.packJson({nullVal:false})));
				return false;
			});

			// 点击过滤清除
			$form2.find('button[data-btn=clear]').on('click',function(){

				$form2.get(0).reset();
				options.onFiltClear && options.onFiltClear($form1.packJson({nullVal:false}));
			});
		}
	};

	// 填充数据
	var fillData = function(params){

		var $form1 = $('#tFormSearch'), $form2 = $('#tFormFilter');

		$form1.get(0).reset();
		$form1.fillForm(params);

		if ($form2.length){
			$form2.get(0).reset();
			$form2.fillForm(params);
		}

		// Clear
		$form1.find('.clear').toggleClass('hide',!$form1.packJson({nullVal:false}));

		// 有数据打开
		if ($form2.length){
			toggleFilter($form2.packJson({nullVal:false}) ? true : false);
		}
	};

	return {
		init: init,
		fillData: fillData
	}
});


/*
 *	[配置 - Require]
 */
require.config({
	baseUrl: (window.path != undefined ? window.path : '/auth') + '/static/js',
	paths: {
		'module':   	'../../module',
		'async': 		'lib/require.async',
		'text':   		'lib/require.text',
		'jquery': 		'lib/jquery.min',
		'Route':  		'lib/route',
		'Extend':   	'jqueryPlugin/jquery.extend',
		'Grid': 		'jqueryPlugin/jquery.grid',
		'DTP': 			'jqueryPlugin/jquery.datetimepicker',
		'Validate': 	'jqueryPlugin/jquery.validate',
		'Select2':  	'jqueryPlugin/jquery.select2.v3.5.4',
		'BMap': 		'https://api.map.baidu.com/api?v=3.0&ak=Lelvz6yh1CSHGNNhOmHVPfkp',
		'kindeditor': 	'kindeditor/kindeditor-all',
		'ParamsConfig': 'paramsConfig'
	},

	shim: {
		'BMap': {
			deps: [],
            exports: 'BMap'
        },
        'kindeditor': {
        	deps: ['jquery']
        }
	},
	urlArgs: 'v=' + window.version
});


/*
 *	[初始化 - App]
 */
require(['Route','jquery','Extend','Global','Header','Menu'],function(Route,$,Extend,G,Header,Menu){

	'use strict';

	// Check Browser
	if (!G.checkBrowser()){ return; }

	// 重置页面
	G.pageReset();

	// 配置路由
	Route.extend({

		// 路由规则
		routes: {									// 规则优先级		- 按 routes 配置顺序，越后优先级越高，'*' 规则除外。
			'': 				'routeHome',		// 无Url 			- 调用 routeHome
			':module/:action': 	'routePage',		// 页面默认规则		- 调用 routePage
			'*': 				'routeError'		// 不匹配以上规则	- 调用 routeError
		},

		// 路由首页模块
		routeHome: function(paths){

			var params = paths[0] ? $.getAllParam('?' + paths[0]) : null;	// 参数

			// Loading
			G.$page.loading();

			Menu.active('');

			// Require Home html
			require(['text!module/index.html'],function(html){

				G.pageReset();
				G.$content.html(html);

				// Require Home js
				require(['module/index'],function(page){

					page.onLoad && page.onLoad(null,params);
					G.$page.loading(false);
				});
			});
		},

		// 路由页面模块
		routePage: function(paths,newPath,oldPath){

			var params = paths[2] ? $.getAllParam('?' + paths[2]) : null;	// 参数

			// 路径相同，调用模块 onSearch 方法
			if (newPath == oldPath){
				require(['module/'+newPath],function(page){

					page.onSearch && page.onSearch(newPath,params);
				});
				return;
			}

			// Loading
			G.$page.loading();

   			// Require html
			require(['text!module/'+newPath+'.html'],function(html){			

				// Dom update html
				G.pageReset();
				G.$content.html(html);

				// Require js
				require(['module/'+newPath],function(page){

					page.onLoad && page.onLoad(newPath,params);
					G.$page.loading(false);
				});

			},function(err){												// 404

				console.log('[App routePage] require html error');
				console.log(err);

				Menu.active(newPath);
				require(['text!module/404.html'],function(html){ 
					
					G.pageReset();
					G.$content.html(html);	
					G.$page.loading(false);
				});
			});

		},

		// 403 - 规则以外的访问路径
		routeError: function(){

			console.log('[App routeError] path rule error');

			Menu.active('');
			require(['text!module/403.html'],function(html){ 

				G.pageReset();
				G.$content.html(html);
				G.$page.loading(false);
			});
		}

	}).start();

});