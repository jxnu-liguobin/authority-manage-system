<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>图书查询</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/assets/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">

        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">

                    <form class="form-inline" id="search_from" name="search_from">
                        <label class="control-label" for="inputBookName">书名：</label>
                        <input class="form-control" type="text" id="inputBookName" name="inputBookName"/>
                        <label class="control-label" for="inputAuthor">作者：</label>
                        <input class="form-control" type="text" id="inputAuthor" name="inputAuthor"/>
                        <label class="control-label" for="inputPublication">出版社：</label>
                        <input class="form-control" type="text" id="inputPublication" name="inputPublication"/>
                        <button style="margin-top: 5px;"  type="button" onclick="findBook()" id="search" class="btn btn-warning">
                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span> 查询</button>
                    </form>

                </div>

                <div class="ibox-content">
                    <div class="row row-lg">
                        <div class="col-sm-12">
                            <!-- Example Card View -->
                            <div class="example-wrap">
                                <div class="example">
                                    <table class="table table-bordered" id="table_list"></table>
                                </div>
                                <!--提交按钮-->
                                <div class="example">
                                    <button id = "showlist" name = "showlist" type="button" class="btn btn-sm btn-success" data-toggle="modal" data-target="#myModal">
                                        <span class="glyphicon glyphicon-list" aria-hidden="true"></span>
                                        查看我的预定书单
                                    </button>
                                </div>
                            </div>
                            <!-- End Example Card View -->
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<!-- 请确认您要借阅的书籍模态框 -->
<div class="modal fade" id="myModal" name = "myModal" style="display: none;" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel" name = "myModalLabel">
                    请确认您要借阅的书籍
                </h4>
            </div>
            <div class="modal-body">
                <table id = "confirmTable" name = "confirmTable" class="table table-bordered">
                    <tr>
                        <th style="text-align:center">书名</th>
                        <th style="text-align:center">作者</th>
                        <th style="text-align:center">出版社</th>
                        <th style="text-align:center">操作</th>
                    </tr>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
                <button id="finish" name="finish" type="button" onclick="reserveBook()" class="btn btn-primary">
                    提交
                </button>
            </div>
        </div>
    </div>
</div>


<!-- 全局js -->
<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>

<!-- Bootstrap table -->
<script src="${ctx!}/assets/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx!}/assets/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx!}/assets/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

<!-- Peity -->
<script src="${ctx!}/assets/js/plugins/peity/jquery.peity.min.js"></script>

<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>
<script type="text/javascript">

    //预定图书列表
    var booklist = new Array();

    //预定图书对象
    var borrowlist= {
        //获取用户id
        id:'<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="id"/>',
        booklist:booklist,
        //添加图书
        addbooks : function(id)
        {
            booklist.push(id);
        },
        //删除图书
        removebooks : function(id)
        {
            var index=0;
            var newlist=new Array();
            for(var i=0;i<booklist.length;i++)
            {

                if(booklist[i]==id)
                {
                    index=i;
                }
            }
            for(var j=0;j<booklist.length;j++)
            {
                if(j!=index)
                {
                    newlist.push(booklist[j]);
                }
            }
            booklist=newlist;
            borrowlist.booklist=newlist;

        }
    };
    /*
     * 初始化BootstrapTable,动态从服务器加载数据
     * */
    $(document).ready(function () {

        $("#table_list").bootstrapTable({
            //使用get请求到服务器获取数据
            method: "POST",
            //必须设置，不然request.getParameter获取不到请求参数
            contentType: "application/x-www-form-urlencoded",
            //获取数据的Servlet地址
            url: "${ctx!}/web/books/findlist",
            //表格显示条纹
            striped: true,
            //启动分页
            pagination: true,
            //每页显示的记录数
            pageSize: 10,
            //当前第几页
            pageNumber: 1,
            //记录数可选列表
            pageList: [5, 10, 15, 20, 25],
            //是否启用查询
            search: false,
            //表示服务端请求
            sidePagination: "server",
            //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
            //设置为limit可以获取limit, offset, search, sort, order
            queryParamsType: "undefined",
            queryParams:queryParams,
            //json数据解析
            responseHandler: function(res) {
                return {
                    "rows": res.content,
                    "total": res.totalElements
                };
            },
            //数据列
            columns: [
                {
                    title: "书名",
                    field: "bookName",
                    align: 'center',
                },{
                    title: "作者",
                    field: "bookAuthor",
                    align: 'center'
                },{
                    title: "出版社",
                    field: "bookPress",
                    align: 'center'
                },{
                    title: "现有库存",
                    field: "currentInventory",
                    align: 'center'

                },{
                    title: "借阅书籍",
                    field: "reserve",
                    align: 'center',
                    formatter: function (value, row, index) {
                        var operateHtml = '<button type="button" class="btn btn-primary btn-sm addbtn" onclick="addReserve(\''+row.bookId+'\',\''+row.bookName+'\',\''+row.bookAuthor+'\',\''+row.bookPress+'\')"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 加入预定书单</button>';
                        return operateHtml;
                    }
                }]
        });

        //模态框删除按钮响应事件
        $("#confirmTable").on("click","#delete",function(){

            var bookID = $(this).parent().parent().attr("id");
            $(this).parent().parent().remove();
            borrowlist.removebooks(bookID);

        });
    });
     /**
      * 设置额外BootstrapTable请求参数
      **/
     function queryParams(params) {
         return {
             //表格显示条纹
             striped: true,
             //启动分页
             pagination: true,
             pageSize: params.pageSize,
             //当前第几页
             pageNumber: params.pageNumber,  //页码
             //记录数可选列表
             pageList: [5, 10, 15, 20, 25],
             inputBookName : $('#search_from input[name=\'inputBookName\']').val(),	// 请求时向服务端传递的参数
             inputAuthor : $('#search_from input[name=\'inputAuthor\']').val(),		// 请求时向服务端传递的参数
             inputPublication : $('#search_from input[name=\'inputPublication\']').val(),// 请求时向服务端传递的参数
         }
     }
     /**
      * 查询图书
      *
      */
     function findBook() {

         $('#table_list').bootstrapTable(('refresh'));	// 很重要的一步，刷新url！
         $('#search_from input[name=\'inputBookName\']').val('');
         $('#search_from input[name=\'inputAuthor\']').val('');
         $('#search_from input[name=\'inputPublication\']').val('');
     }
    /*
     * 添加预定图书
     * */
    function addReserve(bookid,bookName,bookAuthor,bookPress) {

        //判断是否已经添加过该书标记，规定一个人同一本书只能借一次
        var isInArry=0;
        for(var i=0;i<borrowlist.booklist.length;i++)
        {
            if(bookid==borrowlist.booklist[i])
            {
                isInArry=1;
            }
        }

        if(isInArry==0)
        {
            borrowlist.addbooks(bookid);
            var newLine = "<tr id="+bookid+">";
            newLine += "<td style='text-align:center'>"+bookName+"</td>";
            newLine += "<td style='text-align:center'>"+bookAuthor+"</td>";
            newLine += "<td style='text-align:center'>"+bookPress+"</td>";
            newLine += "<td style='text-align:center'><button id=delete class='btn btn-xs btn-danger'><span class='glyphicon glyphicon-remove' aria-hidden='true'><span> 删除</td>";
            newLine += "</tr>";
            $("#confirmTable").append(newLine);
            /** 添加成功弹出提示*/
         	layer.msg('添加成功', 
			{
					icon: 1,
					time: 1000,
			});
        }else
        {
            isInArry=0;
            layer.msg("同一本书只可以借阅一本！")
        }
    }

    /*
     * 预定图书
     * */
    function reserveBook(){
        var mborrowlist=JSON.stringify(borrowlist);
        layer.confirm('确定预定这些图书吗?', {icon: 3, title:'提示'}, function(index){
            $.ajax({
                type: "POST",
                dataType: "json",
                url: "${ctx!}/web/books/borrowlist/"+ mborrowlist ,
                success: function(msg){
                    layer.msg(msg.message, {time: 2000},function(){
                        //要清空booklist中对应在table中的书籍数据
                        cleanArray(borrowlist.booklist);
                        //清空数组
                        booklist.splice(0,booklist.length);
                        borrowlist.booklist=booklist;
                        $('#table_list').bootstrapTable("refresh");
                        layer.close(index);
                    });
                }
            });
        });
    }

    /*
     * 清空table项
     * */
    function cleanArray(arr) {
        for(var i=0;i<arr.length;i++)
        {
            var id="#"+arr[i];
            $(id).remove();
        }
    }
</script>

</body>

</html>
