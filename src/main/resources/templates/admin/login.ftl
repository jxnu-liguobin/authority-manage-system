<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

<title>图书管理系统登录页面</title>
<meta name="keywords" content="">
<meta name="description" content="">
<link href="${ctx!}/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0"
	rel="stylesheet">
<link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
<link href="${ctx!}/assets/css/style.css" rel="stylesheet">
<link href="${ctx!}/assets/css/login.css" rel="stylesheet">
<style>
button {
	display: table width: auto margin-left: auto margin-right: auto
}
</style>
<!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
<script>
	if (window.top !== window.self) {
		window.top.location = window.location;
	}
</script>
</head>

<body class="signin">
	<div class="signinpanel">
		<div class="row">
			<div class="col-sm-12">
				<#if message?exists >
				<div class="alert alert-danger">${message!}</div>
				</#if>
				<form method="post" action="${ctx!}/admin/login" id="frm">
					<h3 class="no-margins">图书管理系统</h3>
					<h4 class="no-margins">登录：</h4>
					<input type="text" class="form-control uname" name="usercode"
						id="usercode" placeholder="用户码" /> <input type="password"
						class="form-control pword m-b" name="password" id="password"
						placeholder="密码" /><br>
					<div class="form-group col-xs-6" style="padding-left: 0px;">
						<img id="vcode" src="/getGifCode" onclick="changeImg()">
					</div>
					<div class="form-group col-xs-6" style="padding-right: 0px;">
						<input type="text" class="form-control" 
							placeholder="验证码" data-rule="验证码:required" id="vcode" name="vcode" style="margin-top:0px;">
					</div>
					<div class="form-group" style="text-align: left">
						<label><input type="checkbox" name="rememberMe"
							id="rememberMe"
							style="width: 12px; height: 12px; margin-right: 5px;">记住我</label>
					</div>
					<button class="btn btn-success block full-width">登录</button>

				</form>
			</div>
		</div>
		<div class="signup-footer">
			<div class="pull-left">&copy;梦境迷离</div>
		</div>
	</div>




	<!-- 全局js -->
	<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
	<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>
	<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>

	<!-- 自定义js -->
	<script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>

	<!-- jQuery Validation plugin javascript-->
	<script src="${ctx!}/assets/js/plugins/validate/jquery.validate.min.js"></script>
	<script src="${ctx!}/assets/js/plugins/validate/messages_zh.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			// 在键盘按下并释放及提交后验证提交表单
			$("#frm").validate({
				rules : {
					usercode : {
						required : true,
						minlength : 5
					},
					password : {
						required : true,
						minlength : 6
					},
					vcode : {
					   required : true,
					   remote:{
                   	 		url:"/isTrue",//该url没有被拦截
                   	 		type: 'GET',//请求方式
                   	 		delay:2000,
                   	 		//默认提交当前input value
                  		 } 
					}
				},
				messages : {
					usercode : {
						required : "请输入用户码",
						minlength : "用户名必需由五个以上字符组成"
					},
					password : {
						required : "请输入密码",
						minlength : "密码长度不能小于6个字符"
					},
					vcode: {
						required : "请输入验证码",
						remote:"验证码错误，请刷新验证码",
					}
				},
				submitHandler : function(form) {
					form.submit();
				}
			});
		});
		$("#closeRegist").click(function() {
			$("#userName").val("");
			$("#nickName").val("");
			$("#telephone").val("");
		});
		//首页注册
		function register() {
			layer.open({
				type : 2,
				title : "用户注册",
				area : [ '893px', '600px' ],
				shadeClose : true,
				shade : false,
				content : '${ctx!}/assets/regist',
			});
		}
		function index() {
			window.location.href = '${ctx!}/admin/login';
		}
		
		 // 刷新图片  
	    function changeImg() {  
	        var imgSrc = $("#vcode");  
	        var src = '/getGifCode'
	        imgSrc.attr("src", changeUrl(src)); 
	        location.reload();
	    }  
	    //为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳  
	    function changeUrl(url) {  
	        var timestamp = (new Date()).valueOf();  
	        var index = url.indexOf("?",url);  
	        if (index > 0) {  
	            url = url.substring(0, url.indexOf(url, "?"));  
	        }  
	        if ((url.indexOf("&") >= 0)) {  
	            url = url + "×tamp=" + timestamp;  
	        } else {  
	            url = url + "?timestamp=" + timestamp;  
	        }  
	        return url;  
	    }  
	</script>
</body>

</html>
