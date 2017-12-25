﻿<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta content="text/html;charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
        <meta http-equiv="X-UA-Compatible" content="IE=9"/>

        <title></title>
        <!-- BOOTSTRAP STYLES-->
        <link href="../assets/css/bootstrap.css" rel="stylesheet"/>
        <!-- FONTAWESOME STYLES-->
        <link href="../assets/css/font-awesome. rel="stylesheet"/>

        <!-- CUSTOM STYLES-->        
        <link href="../assets/css/custom.css" rel="stylesheet"/>
        <link href="../assets/js/dataTables/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="../assets/css/jquery.datetimepicker.css" rel="stylesheet"/>
        
        <link href="../assets/css/chosen.css" rel="stylesheet"/>
    </head>

    <body>
        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <h2>数据统计</h2>
                </div>
            </div>

            <hr/>

            <div class="row">
	            <div class="col-md-12" align="right" style="margin-top:0px;">
	                <%
	                String userType = (String)session.getAttribute("usertype");
	                if ("administrator" == userType) {
	                %>
	                <div class="stainput-group input-group">
	                    <label>选择账号：&nbsp;</label>
	                    <div class="input-group" style="display:inline-block; vertical-align: middle;">
	                        <input type="text" name="selectUserName" id="selectUserName" class="form-control" placeholder="账号" maxlength="32"/>
	                    </div>
                    </div>
	                
	                <div class="stainput-group input-group">
	                    <input type="button" value="查询" class="btn btn-primary btn-sm" onclick="summary()"/>
	                </div>
	                <%
                    }
                    %>
	            </div>
            </div>
            
            <div class="row" style="margin-top:20px;">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        
                    </div>
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
        <script src="../assets/js/jquery.datetimepicker.full.js"></script>
        <script src="../assets/js/echarts.js"></script>
        <script src="../assets/js/macarons.js"></script>
        <script src="../assets/msgbox/msg.js" language="JavaScript" type="text/javascript"></script>        
        <script src="../assets/js/chosen.jquery.js"></script>
        
        <script type="text/javascript">
            var queryDate;
            window.onload=function() {
            	parent.showLoading();
            	summary();
                parent.hideLoading();
            };
            
            $('#selectUserName').bind('keypress', function(event) { 
                if(13 == event.keyCode) {  
                    summary();
                }
            });
            
            function summary() {
            	var userName;

            	if (document.getElementById("selectUserName")) {
            		userName = document.getElementById("selectUserName").value;
            	} else {
            		userName = '${username}';
            	}
            	
            	parent.showLoading();
                $.ajax({
                    type: "POST",
                    url:"../manager/ValueQuery.html",
                    data: {
                    	actionValue : "summary",
                        UserName : userName,
                    },
                    async: false,
                    error: function(request) {
                        parent.hideLoading();
                        MSGAlert("Connection error");
                    },
                    success: function(data) {
                        onAjaxSuccess(data);
                    }
                });
            }
            
            function onAjaxSuccess(msg) {
                var Info = eval('(' + msg + ')');
                
                if ('ok' == Info.result) {
                    parent.hideLoading();
                    resetTabElement();
                    showData(Info.tipMsg);
                } else if ("logout" == Info.result) {
                    parent.hideLoading();
                    MSGAlert("登陆超时，请重新登录！",function() {
                        window.location.href = "../login.jsp";
                    });
                } else if ('error' == Info.result) {
                    parent.hideLoading();
                    MSGAlert(Info.tipMsg);
                } else {
                	parent.hideLoading();
                }
            }
            
            function resetTabElement() {
            	var tabHeaderElement = document.getElementById("header-statistics");
            	var tabElement = document.getElementById("tab-statistics");
            	
            	if (null == tabHeaderElement) {
            		return;
            	}
            	
            	var parentNode = tabHeaderElement.parentNode;
            	
            	if (parentNode) {
            		parentNode.removeChild(tabHeaderElement);
            		parentNode.removeChild(tabElement);
            	}
            }
            
            function showData(data) {
            	var jsonValue = eval('(' + data + ')');
            	var jsonTypeArray = jsonValue.type;
            	
            	var len = parent.JSONLength(jsonTypeArray);
            	for (var i = 0; i < len; i++) {
            		showTab(jsonTypeArray[i]);
            	}
            }
            
            function showTab(data) {
            	var type = data.t;
            	
            	
            }
        </script>
    </body>
</html>
