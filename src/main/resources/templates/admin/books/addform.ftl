<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>添加图书</title>
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
                    <h5>图书信息表</h5>
                </div>
                <div class="ibox-content">
                    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/web/books/addBook">

                        <div class="form-group">
                            <label class="col-sm-3 control-label">书名：</label>
                            <div class="col-sm-8">
                                <input type="hidden" id="bookId" name="bookId" value="${book.bookId}"/>
                                <input class="form-control" type="text" id="bookName" name="bookName" value="${book.bookName}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">出版社：</label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" id="bookPress" name="bookPress" value="${book.bookPress}"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">作者：</label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" id="bookAuthor" name="bookAuthor" value="${book.bookAuthor}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">库存：</label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" id="bookInventory" name="bookInventory" value="${book.bookInventory}"/>
                                <input type="hidden" id="currentInventory" name="currentInventory" value="${book.currentInventory}"/>
                                <input type="hidden" id="cInventory" name="cInventory" value="${book.bookInventory}"/>
                            </div>
                        </div>

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

        $("#frm").validate({
            rules: {
                bookName: {
                    required: true,
                    minlength: 1,
                    maxlength: 50
                },
                bookPress: {
                    required: true,
                    minlength: 2,
                    maxlength: 50
                },
                bookAuthor: {
                    required: true,
                    minlength: 2,
                    maxlength: 50
                },
                bookInventory: {
                    required: true,
                    digits: "只能输入数字",
                }
            },
            messages: {},
            submitHandler:function(form){
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "${ctx!}/web/books/edit"+'?uCode='+'<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="userCode"/>'+'&cInventory='+$("#cInventory").val(),
                    data: $(form).serialize(),
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index);
                        });
                    }
                });
            }
        });
    });

    /**
     * 防止input框内显示数字时出现","
     */
    function changeStr() {

        var bookInventoryStr=$("#bookInventory").val();
        var str=bookInventoryStr.replace(",","");
        $("#bookInventory").val(str);
    };

    window.onload=function () {
        changeStr();
    }
</script>

</body>

</html>
