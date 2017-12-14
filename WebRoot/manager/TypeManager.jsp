<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="type" uri="/type-tags"%>

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
                    <h2>类型管理</h2>
                    <div>
                        <div align="right">
                            <a href="#" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#editType" onclick="showType('typeAdd', -1)">添加类型</a>
                        </div>

                        <jsp:include page="TypeMaintenance.jsp"/>
                    </div>
                </div>
            </div>

            <hr/>

            <div class="row">
                <div class="col-md-12">
                    <!-- 类型表开始 -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <font face=宋体 color=#E90000 size=5>类型</font>
                        </div>

                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="type-table" style="word-break:break-all; word-wrap:break-all;">
                                    <thead>
                                        <type:header/>
                                    </thead>

                                    <tbody>
                                        <type:table index="idx" itemName="typeItem" items="${types}">
                                            <type:show index="${idx}" type="${typeItem}"/>
                                        </type:table>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- 类型表结束 -->
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
                $('#type-table').dataTable();
            });
            
            window.onload=function() {
                $("select[name='type-table_length']") .change(function () {
                    parent.setIframeHeight();
                });
            };

            function showType(action, idx) {
                if ("typeAdd" == action) {
                    document.getElementById("editTypeTitle").innerHTML = "<font face=宋体 color=#F90000 size=4>添加类型</font>";
                    
                    document.getElementById("typeName").value = "";
                    document.getElementById("typeValue").value = "";
                    document.getElementById("typeMin").value = "";
                    document.getElementById("typeMax").value = "";
                    
                    document.getElementById("actionType").value = "addType";
                } else if ("typeEdit" == action) {
                	document.getElementById("editTypeTitle").innerHTML = "<font face=宋体 color=#F90000 size=4>修改类型信息</font>";
                	
                    var jsType = '${jsonType}';
                    var jsonVector = eval("(" + jsType + ")");                    

                    document.getElementById("typeName").value = parent.jsonDecode(jsonVector[idx]["name"]);
                    document.getElementById("typeValue").value = parent.jsonDecode(jsonVector[idx]["value"]);
                    document.getElementById("typeMin").value = parent.jsonDecode(jsonVector[idx]["min"]);
                    document.getElementById("typeMax").value = parent.jsonDecode(jsonVector[idx]["max"]);
                    
                    document.getElementById("actionType").value = "editType";
                }
            }
            
            function deleteType(tpId, tpName) {
            	MSGConfirm("是否删除类型？", function (tp) {
                    if (tp == 'ok') {
                        deleteAction(typeId, typeName);
                    }
                });           
            }
            
            function deleteAction(tpId, tpName) {
                parent.showLoading();
                $.ajax({
                    type: "POST",
                    url:"../manager/TypeEdit.html",
                    data:{
                        actionType : "deleteType",
                        typeId : tpId,
                        typeName : tpName,
                    },
                    async: false,
                    error: function(request) {
                        parent.hideLoading();
                        FotaAlert("Connection error");
                    },
                    success: function(data) {
                        onAjaxSuccess(data, "../manager/TypeManager.jsp");
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
