<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="idx" uri="/index-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta content="text/html;charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
        <meta http-equiv="X-UA-Compatible" content="IE=9"/>

        <title>DATA ON LINE</title>
        <link rel="shortcut icon" type="image/ico" href="favicon.ico">
        <!-- BOOTSTRAP STYLES-->
        <link href="assets/css/bootstrap.css" rel="stylesheet"/>
        <!-- FONTAWESOME STYLES-->
        <link href="assets/css/font-awesome.css" rel="stylesheet"/>
        <!-- CUSTOM STYLES-->
        <link href="assets/css/custom.css" rel="stylesheet"/>
    </head>

    <body>
        <div id="wrapper">
            <nav class="navbar navbar-default navbar-cls-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="index.jsp">DATA ON LINE</a>
                </div>

                <div class="navbar-right">
                    <div>
                        ${username}&nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="login.html?action=logout">注销</a>
                    </div>
                </div>
            </nav>

            <nav class="navbar-default navbar-side" role="navigation">
                <div class="sidebar-collapse">
                    <ul class="nav" id="main-menu">
                        <li class="text-center">
                            <img src="assets/img/find_user.png" class="user-image img-responsive"/>
                        </li>

                        <idx:menu/>
                        
                    </ul>
                </div>
            </nav>

            <div id="page-wrapper">
                <div id="page-inner">
                    <idx:main/>
                </div>                 
                <div class="footer">
                    <idx:copyright/>
                </div>
            </div>
        </div>
        
        <div id='loading' style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.7);z-index: 15000; display: none" >
            <img id="loadingImg" style="position: absolute; top: 50%; left: 50%; width: 80px; height: 80px; margin-top: -15px; margin-left: -15px;" src="assets/msgbox/images/loading.gif"/>
        </div>

        <!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
        <script src="assets/js/jquery-3.2.1.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <!-- METISMENU SCRIPTS -->
        <script src="assets/js/jquery.metisMenu.js"></script>
        <!-- CUSTOM SCRIPTS -->
        <script src="assets/js/custom.js"></script>
        
        <script type="text/javascript">
            history.go(1);
            showLoading();
            window.onload=function() {
                $("#main-menu").find("li").each(function () {
                    $(this).click(function () {
                        showLoading();
                        if($(this).children('ul').length != 0){
                            $(this).next('ul').slideToggle();
                        }
                        var href = $(this).find("a").attr('href');
                        if (href != null && "Default.html" == href) {
                            href = "manager/Default.jsp";
                        } else if (href != null && "User.html" == href) {
                            href = "manager/UserManager.jsp";
                        } else if (href != null && "Database.html" == href) {
                            href = "maintenance/DatabaseManager.jsp";
                        } else {
                            href = "manager/Default.jsp";
                        }
                        
                        document.getElementById('pageContent').src = href;
                         
                        // 修改样式 
                        $("#main-menu").find("a").removeClass('active-menu');
                        if ($(this).children('ul').length != 0) {
                            $(this).children('ul').children("li:first").find("a").addClass('active-menu');
                        } else {
                        	$(this).find("a").addClass('active-menu');
                        }
                        
                        // 阻止跳转
                        return false;
                    });
                });
                setSidebarActive();
            };
            
            function setSidebarActive() {
                var iframeSrc = document.getElementById('pageContent').src;
                
                $("#main-menu").find("a").removeClass('active-menu');
                $("#main-menu").find("li").each(function () {
                    var href = $(this).find("a").attr('href');
                    if(href && href.indexOf('.') != -1) {
                        href = href.substr(0, href.indexOf('.'));
                    }
                   
                    if(iframeSrc.indexOf(href) != -1) {
                        if ($(this).children('ul').length != 0) {
                            $(this).children('ul').children("li:first").find("a").addClass('active-menu');
                        } else {
                            $(this).find("a").addClass('active-menu');
                        }
                    }                        
                });
            }
            
            function setIframeHeight() {
                window.setTimeout(function () {
                    var iframe = document.getElementById("pageContent");
                    iframe.height = 0; 
                    try {
                        var bHeight = iframe.contentWindow.document.body.scrollHeight;
                        var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
                        var height = Math.max(bHeight, dHeight);
                        iframe.height = height;
                        hideLoading();
                     } catch (ex) {}
                }, 200);
                
            }
            
            function iframeRefresh(){
                document.getElementById('pageContent').contentWindow.location.reload(true);
            }
            
            function showLoading() {
                $('#loading').show();
            }
            function hideLoading() {
                $('#loading').hide();
            }
            
            //JSON           
            function jsonDecode(string){
                return string.replace(/<br>/g, "\r\n").replace(/&quot;/g, "\"");
            }
            function JSONLength(obj) {
                var size = 0, key;
                for (key in obj) {
                  if (obj.hasOwnProperty(key)) size++;
                }
                return size;
            }
        </script>
    </body>
</html>
