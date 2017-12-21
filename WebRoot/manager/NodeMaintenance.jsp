<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta content="text/html;charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
        <meta http-equiv="X-UA-Compatible" content="IE=9" />

        <title></title>
        <!-- BOOTSTRAP STYLES-->
        <link href="../assets/css/bootstrap.css" rel="stylesheet" />
        <!-- FONTAWESOME STYLES-->
        <link href="../assets/css/font-awesome.css" rel="stylesheet" />

        <!-- CUSTOM STYLES-->
        <link href="../assets/css/custom.css" rel="stylesheet" />

        <script src="../assets/msgbox/msg.js" language="JavaScript" type="text/javascript"></script>
    </head>

    <body>
        <div class="modal fade" id="editNode" tabindex="-1" role="dialog" aria-labelledby="nodeLabel" aria-hidden="true" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="editNodeTitle"></h4>
                    </div>

                    <div class="modal-body">
                        <form id="nodeEditForm" role="form" action="NodeEdit.html" method="post">
                            <div class="form-group" id="nodeNameGroup">
							    <label>节点名称</label>
							    <input class="form-control" id="nodeNameInput" name="nodeNameInput" maxlength="256" title="最多可输入256个字符"/>
							</div>
							
							<div class="form-group" id="nodeValueGroup">
							    <label>节点ID</label>
							    <input class="form-control" id="nodeValue" name="nodeValue" maxlength="32" title="最多可输入32个字符"/>
							</div>

                            <input type="hidden" name="userId" id="userId"/>
                            <input type="hidden" name="actionNode" id="actionNode"/>
                        </form>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-danger" onclick="saveNode()">保存</button>
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
        <!-- CUSTOM SCRIPTS -->
        <script src="../assets/js/custom.js"></script>

        <script>
            function saveNode() {
                if ('addNode' == $("#actionNode").val()) {
                    if ($("#nodeValue").val() == '') {
                    	MSGAlert("请填写节点ID");
                    	return;
                    }
                }
                
                nodeSubmit();
            }
            
            function nodeSubmit() {
                parent.showLoading();
                $.ajax({
                        type : "POST",
                        url : "NodeEdit.html",
                        data : $("#nodeEditForm").serialize(),
                        async : false,
                        error : function(request) {
                            parent.hideLoading();
                            MSGAlert("Connection error");
                        },
                        success: function(data) {
                            onAjaxSuccess(data, "../manager/NodeManager.jsp?userid4editnode=" + '${userid4editnode}');
                        }
                    });
            }
        </script>
    </body>
</html>
