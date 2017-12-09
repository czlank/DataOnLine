<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.dataonline.config.Database"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
        <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    
        <title>DATA ON LINE</title>
        <!-- BOOTSTRAP STYLES-->
        <link href="../assets/css/bootstrap.css" rel="stylesheet"/>
        <!-- FONTAWESOME STYLES-->
        <link href="../assets/css/font-awesome.css" rel="stylesheet"/>
        <!-- CUSTOM STYLES-->
        <link href="../assets/css/custom.css" rel="stylesheet"/>
        <!-- GOOGLE FONTS-->
    </head>
    
    <body>
        <%
            String dbUserName       = new String();
            String dbPassword       = new String();
            String dbName           = new String();
            String btnText          = new String("数据库配置");

            Database xml            = new Database("config.xml");
            boolean isDatabaseReady = xml.getFlag();

            if (isDatabaseReady) {
                dbUserName = xml.getUserName();
                dbPassword = xml.getPassword();
                dbName     = xml.getDatabaseName();
                btnText    = "数据库重置";
            }
        %>

    <div id="page-inner">
        <div id="step0">
            <div class="row">
                <div class="col-md-12">
                    <h2>数据库配置信息</h2>
                </div>
            </div>

            <hr/>

            <div class="row">
                <div class="col-md-4 col-sm-4" style="height: 40%; width: 99%">
                    <div class="panel panel-default" style="height: 100%; width: 50%; margin-right: auto; margin-left: auto; margin-bottom: auto; margin-top: auto; margin-right: auto; margin-left: auto; margin-bottom: auto; margin-top: auto">
                        <div class="panel-heading" style="color:Black; text-align:left">
                            <%
                                if (isDatabaseReady) {
                            %>
                                                                    数据库配置信息
                            <%
                                } else {
                            %>
                                                                    数据库未配置，请先配置数据库
                            <%
                                }
                            %>
                        </div>

                        <div class="panel-body" style="height: 80%; width: 100%;">
                            <!-- 数据库表信息表开始  -->
                            <div class="row clearfix">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th>用户名</th>
                                                <th>密码</th>
                                                <th>数据库名</th>
                                            </tr>
                                        </thead>
                                        <tbody style="text-align:center;">
                                            <tr>
                                                <td></td>
                                                <td><%=dbUserName%></td>
                                                <td><%=dbPassword%></td>
                                                <td><%=dbName%></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <!-- 数据库表信息表结束 -->
                        </div>
                    </div>
                </div>

            </div>

            <hr/>

            <div class="row">
                <div align="right" style="width:auto; height:auto; padding-right:auto; margin-right: 26.5%;">
                    <input type="button" value="<%=btnText%>" class="btn btn-danger" onclick="MSGConfirm('重置数据库将丢失原来数据，是否继续？', submit_resetdb)"/>
                </div>
            </div>
        </div>
        
        <div id="step1" style="display: none">
            <div class="row">
                <div class="col-md-12">
                    <h2>数据库部署</h2>
                </div>
            </div>

            <hr/>
            
            <center>
                <div class="row ext-center">
                    <div class="col-md-4 col-sm-4" style="height: 60%; width: 60%; left: 20%; top: 25%;">
                        <div class="panel panel-default" style="width: 572px; height: 346px;">
                            <div class="panel-heading" style="color:Black; text-align:left">
                                <big class="step1"><strong>请输入数据库用户名和密码</strong></big>
                                <big class="step2" style="display: none"><strong>请输入需要创建的数据库名</strong></big>
                            </div>

                            <div class="panel-body">
                                <!-- form class="form-horizontal" role="form" id="dbForm" action="DatabaseDeploy.html" method="post" style="height: 180px; width: 322px" -->
                                <form class="form-horizontal" style="height: 180px; width: 322px">
                                    <br/><br/><br/>

                                    <div class="form-group step1">
                                        <label for="dbUserName" class="col-sm-2 control-label" style="width: 87px;">用户名</label>
                                        <input type="text" name="dbUserName" id="dbUserName" class="form-control" style="width: 151px;"/>
                                    </div>

                                    <div class="form-group step1">
                                        <label for="dbPassword" class="col-sm-2 control-label" style="width: 88px;">密 码</label>
                                        <input type="password" name="dbPassword" id="dbPassword" class="form-control" style="width: 151px;"/>
                                    </div>
                                    
                                    <div class="form-group step2"  style="display: none">
                                            <label for="dbName" class="col-sm-2 control-label" style="height: 30px; width: 111px">数据库名</label>
                                            <input type="text" name="dbName" id="dbName" class="form-control" style="width: 163px;"/>
                                    </div>
                                </form>

                                <br/><br/>

                                <div align="right" class="step1" id="step1But">
                                    <input type="button" name="nextStep" value="下一步" class="btn btn-danger" style="width: 104px; margin-right: 1px; margin-bottom:-1px;" onclick="step1_next()"/>
                                </div>
                                <div align="right" class="step2"  style="display: none" id="step2But">
                                    <input type="button" name= "prevStep" value="上一步" class="btn btn-danger" style="width: 83px; margin-bottom: 0px;" onclick="step2_pre()"/>
                                    <input type="button" name= "nextStep" value="配置" class="btn btn-danger" style="width: 76px; margin-right: 2px; margin-bottom: 0px;" onclick="step2_submit()"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </center>
        </div>

    </div>

        <!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
        <!-- JQUERY SCRIPTS -->
        <script src="../assets/js/jquery-3.2.1.min.js"></script>
        <!-- BOOTSTRAP SCRIPTS -->
        <script src="../assets/js/bootstrap.min.js"></script>
        <!-- METISMENU SCRIPTS -->
        <script src="../assets/js/jquery.metisMenu.js"></script>
        <!-- CUSTOM SCRIPTS -->
        <script src="../assets/js/custom.js"></script>
        <!-- msgbox -->
        <script src="../assets/msgbox/msg.js" language="JavaScript" type="text/javascript"></script>

        <script>
            document.body.onkeypress= function CheckEnter() {
                if (event.keyCode == 13 && $('#step1').css('display') != 'none') {
                    if($('#step1But').css('display') == 'block') {
                        step1_next();
                    } else if ($('#step2But').css('display') == 'block') {
                        step2_submit();
                    }
                    return false;
                }       
            };
            
            function submit_resetdb(action) {
                if (action == 'ok') {
                    $('#step0').css('display','none');
                    $('#step1').css('display','block');
                    $('#dbUserName').focus();
                }
            }
            
            function step1_next() {
                var dbusername = document.getElementById("dbUserName").value;
                var dbPassword = document.getElementById("dbPassword").value;
                
                if ("" == dbusername) {
                    MSGAlert("用户名不能为空！",function(){ $("#dbUserName").focus();});
                    return;
                }
                
                if ("" == dbPassword) {
                    MSGAlert("密码不能为空！",function(){ $("#dbPassword").focus();});
                    return;
                }
                
                $('.step1').css('display','none');
                $('.step2').css('display','block');
                $('#dbName').focus();
            }
            
            function step2_pre() {
                $('.step2').css('display','none');
                $('.step1').css('display','block');
                $('#dbUserName').focus();
            }
            
            function step2_submit() {
                var dbName = document.getElementById("dbName").value;
                
                if ("" == dbName) {
                    MSGAlert("数据库名不能为空！",function(){ $("#dbName").focus();});
                    return;
                }
                
                parent.showLoading();
                $.ajax({  
                    type:   "POST",
                    url:    "../manager/DatabaseDeploy.html",
                    data: {
                        dbUserName : $("#dbUserName").val(),
                        dbPassword : $("#dbPassword").val(),
                        dbName     : $("#dbName").val(),
                    },
                    async: false,
                    error: function(request) { 
                        parent.hideLoading();
                        MSGAlert("Connection error");  
                    },  
                    success: function(data) {
                        onAjaxSuccess(data, "../login.jsp");
                    }  
                });
            }
            
            function onAjaxSuccess(msg, href, confirmHandler) {
                var Info = eval('(' + msg + ')');
                
                if ('ok' == Info.result) {
                    if("" != href){
                        window.location.href = href;
                    } else {
                        parent.hideLoading();
                    }                   
                } else if("logout" == Info.result){
                    parent.hideLoading();
                    MSGAlert("登陆超时，请重新登录！",function(){
                        window.location.href = "../login.jsp";
                    });                                
                } else if ('error' == Info.result) {
                    parent.hideLoading();
                    MSGAlert(Info.tipMsg); 
                } else if ('confirm' == Info.result) {
                    parent.hideLoading();
                    if($.isFunction(confirmHandler)){
                        MSGConfirm(Info.tipMsg, confirmHandler);
                    }                    
                }
            }
        </script>
    </body>
</html>
