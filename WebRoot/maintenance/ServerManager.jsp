<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fota.pojo.business.ServerManager"%>
<%@ taglib prefix="svr" uri="/svr-tags"%>

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
        <!-- GOOGLE FONTS-->

        <!-- TABLE STYLES-->
        <link href="../assets/js/dataTables/dataTables.bootstrap.min.css" rel="stylesheet"/>
    </head>

    <body>
        <svr:collect/>

        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <h2>服务器管理</h2>
                    <div>
                        <div align="right">
                            <label style="font-size: 20px;vertical-align: middle;margin-bottom: 0px;" for="serverSwitch">维护服务器&nbsp;&nbsp;&nbsp;</label>
                            <a href="javascript: void(0)" id="serverSwitch" onclick="serverOper()" style="display: inline-block; height: 35px; width: 72px;vertical-align: middle;margin-right: 15px;"></a>
                        </div>
                        
                    </div>
                </div>
            </div>

            <hr/>

            <div class="row">
                <div class="col-md-12">
                    <!-- 服务器连接状态表开始 -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <font face=宋体 color=#23ADCF size=5>服务器</font>
                            
                            <div align="right">
                                <span id="refreshTime" style="vertical-align: middle;"></span>
                                <a href="#" class="btn btn-primary btn-sm" data-toggle="modal" onclick="refresh()">刷新</a>
                            </div>
                        </div>

                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="project-table" style="word-break:break-all; word-wrap:break-all;">
                                    <thead>
                                        <svr:header/>
                                    </thead>

                                    <tbody>
                                        <svr:table index="idx" itemName="serverItem" items="${server}">
                                            <svr:show index="${idx}" manager="${serverItem}"/>
                                        </svr:table>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- 服务器连接状态表结束 -->
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
                $('#project-table').dataTable();
                $('#refreshTime').text(getNowFormatDate()); // 初始化时间
                
                $.ajax({
                    type: "POST",
                    url:"../manager/ServerMaintenance.html",
                    data:{
                        actionServer : "query",
                    },
                    async: false,
                    error: function(request) {
                        parent.hideLoading();
                        FotaAlert("Connection error");
                    },
                    success: function(data) {
                        parent.hideLoading();
                       if (data.indexOf("true") >= 0) {
                           // 开启服务器维护状态
                           $("#serverSwitch").addClass("h_server_on");
                       } else {
                           // 关闭服务器维护状态
                           $("#serverSwitch").addClass("h_server_off");
                       }
                    }
                });
            });
            
            function refresh() {
                parent.showLoading();
                $('#refreshTime').text(getNowFormatDate());
                parent.iframeRefresh();
            }
            
            // 获取当前的日期时间 格式“yyyy-MM-dd HH:MM:SS”
            function getNowFormatDate() {
                var date = new Date();
                var seperator1 = "-";
                var seperator2 = ":";
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                
                var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                        + " " + date.getHours() + seperator2 + date.getMinutes()
                        + seperator2 + date.getSeconds();
                return currentdate;
            }
            
            function serverOper() {
                parent.showLoading();
                $.ajax({
                    type: "POST",
                    url:"../manager/ServerMaintenance.html",
                    data:{
                        actionServer : "switch",
                    },
                    async: false,
                    error: function(request) {
                        parent.hideLoading();
                        FotaAlert("Connection error");
                    },
                    success: function(data) {
                        parent.hideLoading();
                       if (data.indexOf("true") >= 0) {
                           // 开启服务器维护状态
                           $("#serverSwitch").removeClass("h_server_off");
                           $("#serverSwitch").addClass("h_server_on");
                       } else {
                           // 关闭服务器维护状态
                           $("#serverSwitch").removeClass("h_server_on");
                           $("#serverSwitch").addClass("h_server_off");
                       }
                    }
                });
            }
        </script>

        <!-- CUSTOM SCRIPTS -->
        <script src="../assets/js/custom.js"></script>
    </body>
</html>
