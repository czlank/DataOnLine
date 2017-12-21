<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="node" uri="/node-tags"%>

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
        <node:collect/>

        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <h2>节点管理</h2>
                    <div>
                        <div align="right">
                            <a href="#" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#editNode" onclick="showNode('nodeAdd', -1)">添加节点</a>
                            &nbsp;&nbsp;
                            <a href="UserManager.jsp" class="btn btn-default btn-sm">返回</a>
                        </div>

                        <jsp:include page="NodeMaintenance.jsp"/>
                    </div>
                </div>
            </div>

            <hr/>

            <div class="row">
                <div class="col-md-12">
                    <!-- 节点表开始 -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <font face=宋体 color=#E90000 size=5>节点</font>
                        </div>

                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="type-table" style="word-break:break-all; word-wrap:break-all;">
                                    <thead>
                                        <node:header/>
                                    </thead>

                                    <tbody>
                                        <node:table index="idx" itemName="nodeItem" items="${nodes}">
                                            <node:show index="${idx}" node="${nodeItem}"/>
                                        </node:table>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- 节点表结束 -->
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
                $('#node-table').dataTable();
            });
            
            window.onload=function() {
                $("select[name='node-table_length']").change(function() {
                    parent.setIframeHeight();
                });
            };

            function showNode(action, idx) {
                if ("nodeAdd" == action) {
                    document.getElementById("editNodeTitle").innerHTML = "<font face=宋体 color=#F90000 size=4>添加节点</font>";
                    
                    document.getElementById("nodeNameInput").removeAttribute("readOnly");
                    document.getElementById("nodeNameInput").style.backgroundColor = "#FFF";
                    
                    document.getElementById("nodeNameInput").value = "";
                    document.getElementById("nodeValue").value = "";
                    
                    document.getElementById("userId").value = '${userid4editnode}';
                    document.getElementById("actionNode").value = "addNode";
                } else if ("nodeEdit" == action) {
                	document.getElementById("editNodeTitle").innerHTML = "<font face=宋体 color=#F90000 size=4>修改节点信息</font>";
                	
                	document.getElementById("nodeNameInput").setAttribute("readOnly", true);
                	document.getElementById("nodeNameInput").style.backgroundColor = "#d2d2d2";
                	
                    var jsNode = '${jsonNode}';
                    var jsonVector = eval("(" + jsNode + ")");                    

                    document.getElementById("nodeNameInput").value = parent.jsonDecode(jsonVector[idx]["name"]);
                    document.getElementById("nodeValue").value = jsonVector[idx]["value"];
                    
                    document.getElementById("userId").value = '${userid4editnode}';
                    document.getElementById("actionNode").value = "editNode";
                }
            }
            
            function deleteNode(ndId, ndName) {
            	MSGConfirm("是否删除节点？", function (tp) {
                    if (tp == 'ok') {
                    	var userId = '${userid4editnode}';
                    	
                        deleteAction(userId, ndId, ndName);
                    }
                });
            }
            
            function deleteAction(usrId, ndId, ndName) {
                parent.showLoading();
                $.ajax({
                    type: "POST",
                    url:"../manager/NodeEdit.html",
                    data: {
                        actionNode : "deleteNode",
                        userId : usrId,
                        nodeId : ndId,
                        nodeName : ndName,
                    },
                    async: false,
                    error: function(request) {
                        parent.hideLoading();
                        FotaAlert("Connection error");
                    },
                    success: function(data) {
                        onAjaxSuccess(data, "../manager/NodeManager.jsp?userid4editnode=" + '${userid4editnode}');
                    }
                });
            }
            
            function onAjaxSuccess(msg, href, confirmHandler) {
                var Info = eval('(' + msg + ')');
                
                if ('ok' == Info.result) {
                    if ("" != href) {
                        window.location.href = href;
                    } else {
                        parent.hideLoading();
                    }                   
                } else if ("logout" == Info.result) {
                    parent.hideLoading();
                    MSGAlert("登陆超时，请重新登录！",function() {
                        window.location.href = "../login.jsp";
                    });                                
                } else if ('error' == Info.result) {
                    parent.hideLoading();
                    MSGAlert(Info.tipMsg); 
                } else if ('confirm' == Info.result) {
                    parent.hideLoading();
                    if ($.isFunction(confirmHandler)) {
                        MSGConfirm(Info.tipMsg, confirmHandler);
                    }                    
                }
            }
        </script>

        <!-- CUSTOM SCRIPTS -->
        <script src="../assets/js/custom.js"></script>
    </body>
</html>
