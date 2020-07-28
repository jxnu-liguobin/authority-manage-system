<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">

    <title>图书管理系统</title>

    <meta name="keywords" content="">
    <meta name="description" content="">

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="nav-close"><i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                    <span class="block m-t-xs" style="font-size:20px;">
                                        <strong class="font-bold">图书管理系统</strong>
                                    </span>
                                </span>
                        </a>
                    </div>
                    <div class="logo-element">图书管理系统
                    </div>
                </li>
                <li class="hidden-folded padder m-t m-b-sm text-muted text-xs">
                    <span class="ng-scope">操作</span>
                </li>

                <li>
                    <a class="J_menuItem" href="${ctx!}/admin/welcome">
                        <i class="fa fa-search"></i>
                        <span class="nav-label">图书查询&借阅</span>
                    </a>
                </li>
                 <li>
                    <a class="J_menuItem" href="${ctx!}/admin/info?id=<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="id"/>">
                       <i class="fa fa-info" aria-hidden="true"></i>
                        <span class="nav-label">&nbsp;个人信息</span>
                    </a>
                </li>
                 <li>
                    <a class="J_menuItem" href="${ctx!}/admin/borrow?id=<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="id"/>">
                     <i class="fa fa-bookmark-o" aria-hidden="true"></i>
                        <span class="nav-label">我的借阅</span>
                    </a>
                </li>
                <!-- 添加一个现实个人信息的模块，不需要授权访问 -->
                <li>
                    <a href="#">
                        <i class="fa fa fa-cog"></i>
                        <span class="nav-label">系统管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <@shiro.hasPermission name="system:user:index">
                        <li>
                            <a class="J_menuItem" href="${ctx!}/admin/user/index"><i class="fa fa-user" aria-hidden="true"></i>&nbsp;用户管理</a>
                        </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="system:role:index">
                <li>
                    <a class="J_menuItem" href="${ctx!}/admin/role/index"><i class="fa fa-user-plus" aria-hidden="true"></i>角色管理</a>
                </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="system:books:book_management">
            <li>
                <a class="J_menuItem" href="${ctx!}/web/books/index"><i class="fa fa-book" aria-hidden="true"></i>&nbsp;图书管理</a>
            </li>
        </@shiro.hasPermission>
        <@shiro.hasPermission name="system:resource:index">
        <li>
            <a class="J_menuItem" href="${ctx!}/admin/resource/index"><i class="fa fa-check-square" aria-hidden="true"></i>&nbsp;资源管理</a>
        </li>
    </@shiro.hasPermission>
    <!-- 当有资源管理权限的时候-system:resource:druid -->
      <@shiro.hasPermission name="system:resource:druid">
        <li>
            <a class="J_menuItem" href="${ctx!}/druid"><i class="fa fa-cogs" aria-hidden="true"></i>SQL监控</a>
        </li>
    </@shiro.hasPermission>
   <!-- 粗略控制 -->
      <@shiro.hasPermission name="system:memorandum:memorandum">
        <li>
            <a class="J_menuItem" href="${ctx!}/admin/memorandum/index"><i class="fa fa-sticky-note" aria-hidden="true"></i>备忘记录</a>
        </li>
    </@shiro.hasPermission>
    </ul>
    </li>
    <li class="line dk"></li>
    </ul>
</div>
</nav>
<!--左侧导航结束-->
<!--右侧部分开始-->
<div id="page-wrapper" class="gray-bg dashbard-1">
    <div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header"><a class="navbar-minimalize minimalize-styl-2 btn btn-info " href="#"><i class="fa fa-bars"></i> </a>
            </div>
            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-user" aria-hidden="true"></span> <span class="label label-primary"></span>【<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="userCode"/>】
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href='${ctx!}/admin/logout/<@shiro.principal type="cn.edu.jxnu.base.entity.User" property="userCode"/>'>
                                <div>
                                    <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> 注销
                                    <span class="pull-right text-muted small"><@shiro.principal type="cn.edu.jxnu.base.entity.User" property="userName"/></span>
                                </div>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
    <div class="row J_mainContent" id="content-main">
        <iframe id="J_iframe" width="100%" height="100%" src="${ctx!}/admin/welcome" frameborder="0" data-id="index_v1.html" seamless></iframe>
    </div>
</div>
<!--右侧部分结束-->
</div>

<!-- 全局js -->
<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${ctx!}/assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/assets/js/hAdmin.js?v=4.1.0"></script>
<script type="text/javascript" src="${ctx!}/assets/js/index.js"></script>
</body>

</html>
