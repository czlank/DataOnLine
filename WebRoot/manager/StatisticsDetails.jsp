<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="statistics" uri="/statistics-tags"%>

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
                    <h2>详情</h2>
                </div>
            </div>
            <hr/>
<!-- 
userId=${param.u}&nbsp;nodeId=${param.n}
 -->
            <div class="row">
	            <div class="col-md-12" align="right" style="margin-top:0px;">
	                <div class="stainput-group input-group">
	                    <div class="input-group" style="display:inline-block; vertical-align: middle;">
	                        <div class="input-icon-group">
	                            <input id="querydate" type="text" class="form-control"/>
	                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span"></span>        
	                        </div>
	                    </div>
	                </div>
	                <div class="stainput-group input-group">
	                    <input type="button" value="查询" class="btn btn-primary btn-sm" onclick="queryValues()"/>
	                    &nbsp;&nbsp;
	                    <a href="#" class="btn btn-default btn-sm" onclick="goBack()">返回</a>
	                </div>
	            </div>
            </div>
            
            <statistics:collect/>
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
            	var userId = '${u}';
            	parent.showLoading();
                dateTimePicker.init();
                parent.hideLoading();
            };
            
            function goBack() {
            	history.go(-1);
            }
            
            // datetimepicker
            var dateTimePicker = {
                init : function () {
                    var myDate = new Date;
                    var today = myDate.getFullYear() + '/' + (myDate.getMonth() + 1) + '/' + myDate.getDate();
                    myDate.setDate(myDate.getDate());
                        
                    $("#querydate").datetimepicker({
                            lang:'ch',
                            timepicker:false,
                            format:'Y-m-d',
                            formatDate:'Y/m/d',
                            maxDate:0,
                            minDate:'2017/01/01',
                            value:today,
                            onChangeDateTime: function(dp, $input) {
                            	queryDate = $(this).val();
                            }
                    });
                }
            };
            
            function queryValues() {
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
                    
                    if (document.getElementById("selectUserName")) {
                    	// 查找到的账号放在了tipMsg中
                    	//document.getElementById("selectUserName").value = Info.tipMsg;
                    }
                    
                } else if ("logout" == Info.result) {
                    parent.hideLoading();
                    MSGAlert("登陆超时，请重新登录！",function() {
                        window.location.href = "../login.jsp";
                    });
                } else if ('error' == Info.result) {
                    parent.hideLoading();
                    MSGAlert(Info.tipMsg);
                }
            }
        </script>
    </body>
</html>
