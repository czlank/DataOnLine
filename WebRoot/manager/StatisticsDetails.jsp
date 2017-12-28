<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

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
            <div class="row">
	            <div class="col-md-12">
	                <div class="panel panel-default">
	                    <div class="panel-heading" class="row">
	                        <font face=宋体 color=red size=5>类型：${param.tn} - 节点：${param.nn}</font>
	                        <a href="#" onclick="goBack()" style="float: right;"><i class="fa fa-reply"></i>返回</a>
		                </div>
	                </div>
	                
	                <div class="stainput-group input-group" style="float: right;">
		                <div class="input-group" style="display:inline-block; vertical-align: middle;">
	                        <div class="input-icon-group">
	                            <input id="querydate" type="text" class="form-control"/>
	                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span" ></span>
	                        </div>
	                    </div>
	                    <div class="query-group" style="display:inline-block; vertical-align: middle;">
	                        &nbsp;
	                        <input type="button" value="查询" class="btn btn-primary btn-sm" onclick="queryDetails()"/>
	                    </div>
	                </div>
	                <br/><br/><br/>
	                <div class="panel-body">
	                1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
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
                dateTimePicker.init();
                parent.hideLoading();
                queryDetails();
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
            
            function queryDetails() {
            	parent.showLoading();
                $.ajax({
                    type: "POST",
                    url:"../manager/ValueQuery.html",
                    data: {
                    	actionValue : "detail",
                        UserID : '${param.u}',
                        TypeValue : '${param.t}',
                        NodeID : '${param.n}',
                        Date : $("#querydate").val(),
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
                    showData(Info.tipMsg);
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
            
            function showData(data) {
            	var jsonValue = eval('(' + data + ')');
            	var jsonTypeArray = eval('(' + jsonValue.array + ')');
                var len = parent.JSONLength(jsonTypeArray);
            }
        </script>
    </body>
</html>
