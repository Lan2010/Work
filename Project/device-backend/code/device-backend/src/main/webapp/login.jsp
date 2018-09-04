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
<link rel="stylesheet" href="<%=path%>/static/css/font.4.7.0.css?v=1b1c43088a">
<link rel="stylesheet" href="<%=path%>/static/css/iconfont.css?v=1906d12dad">
<link rel="stylesheet" href="<%=path%>/static/css/login.css?v=c24943e5c6">
<script>
var version = '1.0.0.88';
var path = window.path = '<%=path%>';
var apiPath = window.apiPath = '<%=path%>/api';
</script>
</head>

<body>
<div class="wrapper">
    <div class="login-wrap">
    	<div class="title">设备管控平台</div>
    	<div class="login">
            <form id="loginForm">
        		<div class="cells cells-form">
        			<div class="cell">
        				<div class="hd"><i class="fa fa-user"></i></div>
        				<div class="bd"><input id="username" name="user_name" type="text" class="input" placeholder="用户名"></div>
        			</div>
        			<div class="cell">
        				<div class="hd"><i class="fa fa-lock"></i></div>
        				<div class="bd"><input id="password" name="password" type="password" class="input" placeholder="密码"></div>
        			</div>
                    <!-- <div class="cell code">
                        <div class="hd"><i class="fa fa-check-circle-o"></i></div>   
                        <div class="bd"><input id="code" name="validateCode"  type="text" class="input" placeholder="验证码"></div>
                        <div class="ft">
                            <img id="imgCode" src="static/images/login_code.gif" class="img">
                            <a href="javascript:void(0);" id="btnCode">换一张</a>
                        </div>
                    </div> -->
        		</div>
                <div class="btn-area">
                    <div class="error" id="error"></div>
                    <button type="submit" class="btn" id="btnSubmit">登 录</button>
                </div>
            </form>
    	</div>
        <div class="copyright">Copyright&nbsp;&copy;&nbsp;2018&nbsp;天智星</div>
    </div>
</div>

<script src="<%=path%>/static/js/lib/require.min.js?v=5b08692433" data-main="static/js/login.js?v=e859bb8850"></script>
</body>
</html>
