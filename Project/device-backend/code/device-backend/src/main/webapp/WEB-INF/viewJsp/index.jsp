<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<title>天智星 - 设备管控平台</title>
<link rel="stylesheet" href="<%=path%>/static/css/font.4.7.0.css">
<link rel="stylesheet" href="<%=path%>/static/css/iconfont.css">
<link rel="stylesheet" href="<%=path%>/static/css/main.css">
<link rel="stylesheet" href="<%=path%>/static/css/datetimepicker.css">
<link rel="stylesheet" href="<%=path%>/static/js/jqueryPlugin/jquery.select2.v3.5.4.css">
<script>
var version = '1.0.0.88';
var path = window.path = '<%=path%>';
var apiPath = window.apiPath = '<%=path%>/api';
</script>
</head>

<body>

<div class="page-side" id="pageSide">
	<div class="page-menu" id="pageMenu">
		<ul>
			<li class="open">
				<div class="node"><span class="icon"><i class="fa fa-globe"></i></span>概览<span class="arrow fa"></span></div>
				<ul>
					<li><a href="#stat/overview"><span class="icon"><i class="fa fa-pie-chart"></i></span>设备统计</a></li>
				</ul>
			</li>
			<li class="open">
				<div class="node"><span class="icon"><i class="fa fa-hdd-o"></i></span>设备管控<span class="arrow fa"></span></div>
				<ul>
					<li><a href="#device/list"><span class="icon"><i class="fa fa-hdd-o"></i></span>设备管理</a></li>
					<li><a href="#plugin/list"><span class="icon"><i class="fa fa-plug"></i></span>插件上传</a></li>
					<li><a href="#pluginTask/list"><span class="icon"><i class="fa fa-tasks"></i></span>插件更新</a></li>
					<li><a href="#shell/list"><span class="icon"><i class="fa fa-code"></i></span>脚本上传</a></li>
					<li><a href="#shellTask/list"><span class="icon"><i class="fa fa-tasks"></i></span>脚本更新</a></li>
					<li><a href="#firmware/list"><span class="icon"><i class="fa fa-cube"></i></span>固件上传</a></li>
					<li><a href="#fmwTask/list"><span class="icon"><i class="fa fa-tasks"></i></span>固件更新</a></li>
					<!-- <li><a href="#model/list">型号列表</a></li> -->
				</ul>
			</li>
			<li class="open">
				<div class="node"><span class="icon"><i class="fa fa-sun-o"></i></span>系统管理<span class="arrow fa"></span></div>
				<ul>
					<li><a href="#user/list"><span class="icon"><i class="fa fa-user"></i></span>用户列表</a></li>
					<li><a href="#log/login""><span class="icon"><i class="fa fa-file-text-o"></i></span>登录日志</a></li>
					<li><a href="#log/operation"><span class="icon"><i class="fa fa-file-text-o"></i></span>操作日志</a></li>
				</ul>
			</li>
		</ul>
	</div>
</div>

<div class="page-wrap" id="pageMain">
	
	<div class="header" id="pageHeader">
		<div class="logo" id="logo"><i></i>天智星 - 设备管控平台</div>
		<div class="user-region">
			<div class="opt logout" title="退出" id="logout"><i class="fa fa-sign-out"></i></div>
			<div class="opt" id="btnModPass"><i class="fa fa-pencil"></i>修改密码</div>
			<div class="name"><i class="fa fa-user-circle-o"></i><span id="headerNickName"></span></div>
			<div class="com" id="headerComp"></div>
		</div>
	</div>

	<div class="header-area"></div>
	<div class="main">
		<div class="page-content" id="pageContent">
		</div>
	</div>

</div>

<script src="<%=path%>/static/js/lib/require.min.js" data-main="static/js/main.js"></script>
</body>
</html>
