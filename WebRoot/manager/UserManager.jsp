<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="user" uri="/user-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
        <meta http-equiv="X-UA-Compatible" content="IE=9"/>

        <title></title>
        <!-- BOOTSTRAP STYLES-->
        <link href="../assets/css/bootstrap.css" rel="stylesheet"/>
        <!-- FONTAWESOME STYLES-->
        <link href="../assets/css/font-awesome.css" rel="stylesheet"/>

        <!-- CUSTOM STYLES-->
        <link href="../assets/css/custom.css" rel="stylesheet"/>

        <!-- TABLE STYLES-->
        <link href="../assets/js/dataTables/dataTables.bootstrap.min.css" rel="stylesheet"/>
    </head>

    <body>
        <user:collect/>

        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <h2>账户管理</h2>
                    <div>
                        <div align="right">
                            <a href="#" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#editUser" onclick="showUser('userAdd', -1)">添加账户</a>
                        </div>

                        <jsp:include page="UserMaintenance.jsp"/>
                    </div>
                </div>
            </div>

            <hr/>

            <div class="row">
                <div class="col-md-12">
                    <!-- 账号表开始 -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <font face=宋体 color=#23ADCF size=5>账户</font>
                        </div>

                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="user-table" style="word-break:break-all; word-wrap:break-all;">
                                    <thead>
                                        <user:header/>
                                    </thead>

                                    <tbody>
                                        <user:table index="idx" itemName="userItem" items="${users}">
                                            <user:show index="${idx}" user="${userItem}"/>
                                        </user:table>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- 账号表结束 -->
                </div>
            </div>
        </div>

        <!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
        <!-- JQUERY SCRIPTS -->
        <script src="../assets/js/jquery-3.2.1.min.js"></script>
        <!-- BOOTSTRAP SCRIPTS -->
        <script src="../assets/js/bootstrap.min.js"></script>
        <!-- METISMENU SCRIPTS -->
        <script src="../assets/js/jquery.metisMenu.js"></script>
        <!-- DATA TABLE SCRIPTS -->
        <script src="../assets/js/dataTables/jquery.dataTables.min.js"></script>
        <script src="../assets/js/dataTables/dataTables.bootstrap.min.js"></script>

        <script>
            $(document).ready(function () {
                $('#user-table').dataTable();
            });
            
            window.onload=function() {
                $("select[name='user-table_length']") .change(function () {
                    parent.setIframeHeight();
                });
            };
           
            var usrID = -1;
            var usrName = '';
            function showUser(action, idx) {
                if ("userAdd" == action) {
                    document.getElementById("editUserTitle").innerHTML = "<font face=宋体 color=#23ADCF size=4>添加账户</font>";
                    
                    $("#passwordGroup").css("display", "block");
                    $("#nodesGroup").css("display", "block");
                    
                    document.getElementById("userName").removeAttribute("readOnly");
                    document.getElementById("userName").style.backgroundColor = "#FFF";
                    document.getElementById("userName").value = "";
                    
                    document.getElementById("userPassword").value = "";
                    document.getElementById("userManufacturer").value = "";
                    
                    document.getElementById("actionUser").value = "addUser";
                } else if ("userEdit" == action) {
                	document.getElementById("editUserTitle").innerHTML = "<font face=宋体 color=#23ADCF size=4>修改账户信息</font>";
                	
                	$("#passwordGroup").css("display", "none");
                    $("#nodesGroup").css("display", "block");
                	
                	var nameObj = document.getElementById("userName");
                    nameObj.setAttribute("readOnly", true);
                    nameObj.style.backgroundColor = "#d2d2d2";
                	
                    var jsUser = '${jsonUser}';
                    var jsonVector = eval("(" + jsUser + ")");                    

                    nameObj.value = parent.jsonDecode(jsonVector[idx]["userName"]);
                    document.getElementById("userNodes").value = parent.jsonDecode(jsonVector[idx]["nodes"]);
                    
                    document.getElementById("actionUser").value = "editUser";
                    document.getElementById("userId").value = jsonVector[idx]["id"];
                } else if ("userResetPassword" == action){
                	document.getElementById("editUserTitle").innerHTML = "<font face=宋体 color=#23ADCF size=4>重置密码</font>";
                    
                	$("#passwordGroup").css("display", "block");
                    $("#nodesGroup").css("display", "none");
                	
                	var nameObj = document.getElementById("userName");
                    nameObj.setAttribute("readOnly", true);
                    nameObj.style.backgroundColor = "#d2d2d2";
                    
                    var jsUser = '${jsonUser}';
                    var jsonVector = eval("(" + jsUser + ")");                    

                    nameObj.value = parent.jsonDecode(jsonVector[idx]["userName"]);
                    document.getElementById("userPassword").value = "";
                    
                    document.getElementById("actionUser").value = "userResetPassword";
                    document.getElementById("userId").value    = jsonVector[idx]["id"];
                }
            }
            
            function deleteUser(userId, userName) {
                usrID = userId;
                usrName = userName;
                MSGAlert("删除账号操作将删除该账号下所有数据且不可恢复!", confirmDelUser);                
            }
            
            function confirmDelUser(tp) {
               if (tp != 'ok') {
                   return;
                }
                
                MSGConfirm("是否删除账号？", function (tp) {
                    if (tp == 'ok'){
                        deleteAction();
                    }
                });
            }
            
            function deleteAction() {
                parent.showLoading();
                $.ajax({
                    type: "POST",
                    url:"../manager/UserEdit.html",
                    data:{
                        actionUser : "delUser",
                        userId : usrID,
                        userName : usrName,
                    },
                    async: false,
                    error: function(request) {
                        parent.hideLoading();
                        FotaAlert("Connection error");
                    },
                    success: function(data) {
                        onAjaxSuccess(data, "../manager/UserManager.jsp");
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

        <!-- CUSTOM SCRIPTS -->
        <script src="../assets/js/custom.js"></script>
    </body>
</html>
