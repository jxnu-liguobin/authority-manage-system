<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户注册</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>用户信息</h5>
                </div>
                <div class="ibox-content">
                    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/regist">
                        <input type="hidden" id="id" name="id" value="${user.id}">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">学生卡号:</label>
                            <div class="col-sm-8">
                            <!--onblur 离开输入时验证  -->
                                <input data-exist="userCode" id="userCode" name="userCode" class="form-control" placeholder="请输入您的学生卡号。" type="text" value="${user.userCode}" <#if user?exists> readonly="readonly"</#if> >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">姓名:</label>
                            <div class="col-sm-8">
                                <input id="userName" name="userName" class="form-control" type="text" placeholder="请输入您的真实姓名。" value="${user.userName}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">电话:</label>
                            <div class="col-sm-8">
                                <input id="telephone" name="telephone" class="form-control" placeholder="请输入您的电话号码。" value="${user.telephone}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">密码:</label>
                            <div class="col-sm-8">
                                <input id="password1" name="password1" type="password" class="form-control" placeholder="请设置您的登录密码。">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">密码:</label>
                            <div class="col-sm-8">
                                <input id="password" name="password" type="password" class="form-control" placeholder="请再次输入您的登录密码。">
                            </div>
                        </div>
                        <input type="hidden" id="locked" name="locked" value="0">
                        <div class="form-group">
                            <div class="col-sm-8 col-sm-offset-3">
                                <button class="btn btn-primary" type="submit">提交</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>


<!-- 全局js -->
<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>

<!-- 自定义js -->
<script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>
<script src="${ctx!}/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx!}/assets/js/plugins/validate/messages_zh.min.js"></script>
<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/assets/js/plugins/layer/laydate/laydate.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
    	/**添加手机号码验证*/
    	jQuery.validator.addMethod("checkPhone", function(value, element) {
    		var tel = /^1[3,5,7,8]\d{9}$/;
    		return this.optional(element) || (tel.test(value));
    	}, "电话号码格式错误");
    	
        $("#frm").validate({
            rules: {
                userName: {
                    required: true,
                    minlength: 2,
                    maxlength: 10,
                },
                userCode: {
                    required: true,
                    minlength: 5,
                    maxlength: 10,
                    remote:{
                    	 url:"${ctx!}/assets/isAvailable",
                    	 type: 'GET',//请求方式
                    	 delay :  2000,
                    	 //默认提交当前input value
                    }
                }
                ,
                telephone: {
                    required: true,
                    checkPhone:true,  
                }
                ,
                locked: {
                    required: true
                }
                ,
                password1:
                {
                    required:true,
                    minlength:6,
                    maxlength:20
                }
                ,
                password:{
                    equalTo:"#password1"
                }
            },
            messages: {
            	userCode:{
            		remote:"学号已经被注册",
            		minlength:"学号最少5位",
            		maxlength:"学号最多10位"
            	},
            	userName:{
            		minlength:"姓名最少2位",
            		maxlength:"姓名最多10位"
            	},
            	password1:{
            		minlength:"密码最少5位",
            		maxlength:"密码最多20位"
            	}
            		
            },
            submitHandler:function(form){
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "${ctx!}/assets/edit",
                    data: $(form).serialize(),//序列化表单的值，把输入的内容用&符号连接起来name=xxx&paw=xxx，用于ajax向后台发送数据
                    success: function(msg){
                        layer.msg(msg.message, {time: 3000},function(){
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index);
                        });
                    }
                });
            }
        });
    });
</script>

</body>

</html>
