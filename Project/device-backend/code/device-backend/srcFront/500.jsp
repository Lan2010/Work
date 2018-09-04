<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum=1, user-scalable=no">
<meta name="format-detection" content="telephone=no,email=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="mobile-web-app-capable" content="yes">
<title>页面无法访问</title>
<script>
// Set FontSize
(function(){var n=function(){var n=document.documentElement.clientWidth;if(n>750){n=750}document.documentElement.style.fontSize=n/7.5+"px"};window.onresize=function(){n()};n()})();
console.log('500');
</script>
<link rel="stylesheet" href="<%=path%>/static/css/error.css">
</head>

<body>
<div class="page">
	<div class="pic1"></div>
	<div class="text">系统繁忙，请稍后再试~</div>
</div>
</body>
</html>
