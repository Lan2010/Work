/*!
 *	[Login 入口]
 *	@Author Gang
 *	@Latest Update 2018-04-19
 */


/*
 *	[定义模块 - 全局参数配置]
 */
define('Global',['jquery','Extend'],function($){

	'use strict';

	var checkBrowser = function(){

		var res = navigator.appVersion.match(/MSIE 7.0|MSIE 6.0|MSIE 8.0|MSIE 9.0|MSIE 10.0/i);
		if (res){
			$('body').msg('tip',{text: '尊敬的用户，你好！ 本系统不支持IE10及以下的浏览器，请升级你的浏览器。推荐谷歌、火狐浏览器！', mask: true, close: false, level: 'top'});
			$('body').find('.login-wrap').remove();
		}
		return !res
	};
	
	return {
		path: window.path != undefined ? window.path : '/stardev',
		api: {
			path: window.apiPath != undefined ? window.apiPath : '/stardev/api'
		},
		checkBrowser: checkBrowser
	}

});


/*
 *	[配置 - Require]
 */
require.config({
	baseUrl: (window.path != undefined ? window.path : '/auth') + '/static/js',
	paths: {
		'jquery': 	'lib/jquery.min',
		'Extend':   'jqueryPlugin/jquery.extend',
		'MD5': 		'lib/md5'
	},
	urlArgs: 'v=' + version
});


/*
 *	[初始化 登录]
 */
require(['jquery','Extend','Global'],function($,Extend,G){

	'use strict';

	// Check Browser
	if (!G.checkBrowser()){ return; }

	// Var $ojbect
	var $form = $('#loginForm'),
		$btnSubmit = $('#btnSubmit'),
		$inputs = $('input'),
		$username = $('#username'),
		$password = $('#password'),
		$code = $('#code'),
		$imgCode = $('#imgCode'),
		$btnCode = $('#btnCode'),
		$error = $('#error');

	// Set api
	var api = {
		login: {path: G.path,uri: '/api/login'},
		code: {path: G.path,uri: '/api/validateCode'}
	};

	// 检查表单
	var checkForm = function(){

		var r = true;

		$error.text('');
		$inputs.each(function(){
			var $self = $(this), v = $self.val();
			if (!v){
				$error.text('请输入' + $self.attr('placeholder'));
				r = false;
				return false;
			}

			// if ($self.attr('id') == 'code' && !/^[a-zA-Z0-9]{4}$/.test(v)){
			// 	$error.text('请输入4位验证码');
			// 	r = false;
			// 	return false;
			// }
		});

		return r;
	};

	// 更新验证码
	var updateCode = function(){

		$imgCode.attr('src',api.code.apiPath+api.code.uri + '?' + Math.random());
		$code.val('');
	};

	// 重置按钮
	var resetBtn = function(){
		$btnSubmit.removeAttr('disabled').text('登　录');
		// updateCode();
	};

	// 设置表单
	var setForm = function(){

		// 换一张
		// $btnCode.on('click',updateCode);

		// 输入
		$inputs.on('focus',function(){
			// $error.text('');
			$(this).parent().parent().addClass('focus');
		}).on('blur',function(){
			$(this).parent().parent().removeClass('focus');
		});

		$('#username,#password').on('input keyup',function(e){

			if (e.keyCode === 13){ return; }
			
			if ($(this).val()){
				$error.text('');
			}
		});

		// $code.on('input keyup',function(){

		// 	var v = $(this).val(), l = v.length;

		// 	// if (l >= 1){
		// 	// 	$error.text('');
		// 	// }
		// 	if (l >= 4){
		// 		$error.text( !/^[a-zA-Z0-9]{4}$/.test(v) ? '请输入4位验证码' : '');
		// 	}
			
		// });

		// 登录
		$form.on('submit',function(){	

			if (!checkForm()){ return false; }

			var name = $username.val(),
				pass = $password.val();
				// code = $code.val();

			// pass = MD5.hex_md5(MD5.hex_md5(pass)+''+MD5.hex_md5(name));

			$.ajax({
				url: api.login.path + api.login.uri,
				type: 'post',
				data: {
					user_name: name,
					password: pass
					// ,validateCode: code
				},
				dataType: 'json',
	    		beforeSend: function(){
	    			$btnSubmit.attr('disabled',true).text('登录中');
	    		},
	    		success: function(json){
	    			if (json.code != 0){
	    				$error.text(json.msg);
	    				resetBtn();
	    				return;
	    			}

	    			window.location.replace(G.path+'/');
	    		},
	    		error: function(xhr){
	    			resetBtn();
	    		}
			})

			return false;
		});
	};

	var init = function(){

		setForm();
	};

	init();
});