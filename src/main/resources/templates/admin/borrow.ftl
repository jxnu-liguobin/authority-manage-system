<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的借阅</title>
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
 				<#if message?exists >
				<div class="alert alert-danger">${message!}</div>
				</#if>
                </div>

                <div class="ibox-content">
                    <div class="row row-lg">
                        <div class="col-sm-12">
                            <!-- Example Card View -->
                            <div class="example-wrap">
                                <div class="example">
                                    <table class="table table-bordered" id="table_list"></table>
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
    //预期的图书
    var delayBook = new Array();
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
            url: "${ctx!}/assets/borrowList?uCode="+'<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="userCode"/>',
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
                    title: "借阅日期",
                    field: "updateTime",
                    align: 'center',
                    formatter:function(value, row, index){
                    	var dt = new Date(row.updateTime);
                        dt = timestampToTime(dt.getTime());
                     	var operateHtml='<span class="label label-info">'+dt+'</span>';
                        return operateHtml;
                    }

                },{
                    title: "应归还日期",
                    field: "shouldReturn",
                    align: 'center',
                    formatter: function (value, row, index) {
                    	var operateHtml;
                    	//显示借书日期
                        var dt = new Date(row.updateTime);
                        dt = timestampToTime(dt.getTime());
                        //计算时间间隔
                        var date = new Date();
                        var s = date.getTime();//现在的时间
                        var days = GetDateDiff(row.updateTime,s);//得到间隔时间,这里updateTime是时间戳，而不是日期
                        if (days>30) {
                        	var delay = days-30
                        	delayBook.push(row.bookId);//记录已经预期的书籍
                        	operateHtml = '<span class="label label-danger">预期'+delay+'天，不可自主还书</span>';
                         } else {
                        	 var dt =new Date(new Date(row.updateTime).getTime()+30*24*60*60*1000);
                        	 dt = timestampToTime(dt.getTime());
                        	 operateHtml ='<span class="label label-warning">'+dt+'</span>';
                         }
                        return operateHtml;
                    }
                },{
                	 title: "操作",
                     field: "mOperate",
                     align: "center",
                     formatter: function (value, row, index) {
                    	  var isIn = isInArray(delayBook,row.bookId);
                    	  var operateHtml;
                    	  if (isIn) {
                    		  //console.info("存在这个");
                           	  operateHtml = '<button name="canNotClick"  disabled class="btn btn-xs btn-info" > <span class="glyphicon glyphicon-retweet" aria-hidden="true"></span> 还书</button>';
                    	  } else {
                           	  operateHtml = '<button name="returnBook" class="btn btn-xs btn-info" onclick="returnBook(\''+row.bookId+'\')"> <span class="glyphicon glyphicon-retweet" aria-hidden="true"></span> 还书</button>';
                    	  }
                    	  return  operateHtml;
                     }
                }]
        });
    });
    //判断是否存在某个值
    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    }
    
    //还书
    function returnBook(id){
    	
       var userId='<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="id"/>';
       var bookId = id;
       var data = new Array();
       data.push(userId);
       data.push(bookId);
       //转换为json格式
       var jsonDate=JSON.stringify(data);
   	   layer.confirm('确定归还这本图书吗?', {icon: 3, title:'提示'}, function(index){
              $.ajax({
                  type: "POST",
                  dataType: "json",
                  url: "${ctx!}/web/books/returnOneBook/" + jsonDate,
                  success: function(msg){
                      //每还一本书就动态将该行删除
                      var id="#"+bookId;
                      $(id).remove();
                      layer.msg(msg.message, {time: 2000},function(){
                          $('#table_list').bootstrapTable("refresh");
                          layer.close(index);
                      });
                  }
              });
          });
    }
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
     /**
      *此处增加一个将时间戳转化为正常格式的方法
      */
      function timestampToTime(timestamp) {
          var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
          Y = date.getFullYear() + '年';
          M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '月';
          D = date.getDate() + '日';
          h = date.getHours() + ':';
          m = date.getMinutes() + ':';
          s = date.getSeconds();
          return Y+M+D+h+m+s;
      }
      
      /**
       * 计算两个日期之间所差天数
       */

      function GetDateDiff(startDate,endDate)
      {
    	  //传来的值已经是时间戳了
          //var startTime = new Date(Date.parse(startDate.replace(/-/g,"/"))).getTime();
          //var endTime = new Date(Date.parse(endDate.replace(/-/g,"/"))).getTime();
          var dates = Math.abs((startDate - endDate))/(1000*60*60*24);
          // console.info("超期天数："+Math.floor(dates));
          return  Math.floor(dates);
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
