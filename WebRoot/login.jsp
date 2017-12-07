<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

        <title>SaneChips - FOTA</title>
        <link rel="shortcut icon" type="image/x-icon" href="favicon.ico">
        <!-- BOOTSTRAP STYLES-->
        <link href="assets/css/bootstrap.css" rel="stylesheet"/>
        <!-- FONTAWESOME STYLES-->
        <link href="assets/css/font-awesome.css" rel="stylesheet"/>
        <!-- CUSTOM STYLES-->
        <link href="assets/css/custom.css" rel="stylesheet"/>
        <link type="text/css" rel="stylesheet" href= "assets/msgbox/msg.box.css" />

    </head>
    <body>
        <div class="container">
            <div class="row text-center">
                <div class="col-md-12">
                    <br/><br/>
                    <h2>FOTA</h2>

                    <h5>&nbsp;</h5>
                    <br/>
                </div>
            </div>

            <div class="row">
                <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <strong>   请输入账号 </strong>
                        </div>

                        <div class="panel-body">
                            <form id="frmLogin" action="login.html" method="post">
                                <div class="form-group input-group">
                                    <span class="input-group-addon"><i class="fa fa-tag"></i></span>
                                    <input type="text" id="username" name="username" value="${username}" class="form-control" placeholder="账号" maxlength="32"/>
                                </div>
                                <div class="form-group input-group">
                                    <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                                    <input type="password" id="password" name="password" class="form-control" placeholder="密码" maxlength="64"/>
                                </div>
                                <input type="hidden" name="action" value="login">
                                <input type="hidden" id="errorResult" value="<%=request.getAttribute("error")%>" >
                                <div align="center">
                                    <input type="submit" value="登录" onclick="login()" class="btn btn-primary"/>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div id='loading' style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.7);z-index: 15000; display: none" >
            <img id="loadingImg" style="position: absolute; top: 50%; left: 50%; width: 80px; height: 80px; margin-top: -15px; margin-left: -15px;" src="assets/msgbox/images/loading.gif"/>
        </div>
        <!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
        <!-- JQUERY SCRIPTS -->
        <script src="assets/js/jquery-3.2.1.min.js"></script>
        <!-- BOOTSTRAP SCRIPTS -->
        <script src="assets/js/bootstrap.min.js"></script>  
        <script src="assets/msgbox/msg.box.js"></script>      
        <script src="assets/msgbox/fota.msg.js" language="JavaScript" type="text/javascript"></script>
        
        <script type="text/javascript">
            history.go(1);
            window.onload = function(){
                loadTopWindow();
                $("#username").focus();
                var errInfo = document.getElementById("errorResult").value;
                if(errInfo != "null"){
                    FotaAlert(errInfo,function(){ $("#username").focus(); });
                }
            };
            function login(){
                if($("#username").val() == "" || $("#username").val() == null){
                    FotaAlert("请输入账号");
                    return;
                } 
                showLoading();
                $("frmLogin").submit();
            }

            function showLoading() {
                $('#loading').show();
            }
            function hideLoading() {
                $('#loading').hide();
            }   
            //判断当前窗口是否有顶级窗口，如果有就让当前的窗口的地址栏发生变化，    
            //这样就可以让登陆窗口显示在整个窗口了    
            function loadTopWindow(){    
                if (window.top!=null && window.top.document.URL!=document.URL){
                    window.top.location= document.URL;
                }    
            }
         </script>
        
    </body>
</html>

