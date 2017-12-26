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
            
            <div class="row" style="margin-top:20px;" id="statisticsrow">
                <div class="panel panel-default" id="statistics">
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
            	
            	if ("" == userName) {
            		return;
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
                    resetStatisticsContent();
                    showData(Info.tipMsg);
                } else if ("logout" == Info.result) {
                    parent.hideLoading();
                    MSGAlert("登陆超时，请重新登录！",function() {
                        window.location.href = "../login.jsp";
                    });
                } else if ('error' == Info.result) {
                	resetStatisticsContent();
                    parent.hideLoading();
                    MSGAlert(Info.tipMsg);
                } else {
                	resetStatisticsContent();
                	parent.hideLoading();
                }
            }
            
            function resetStatisticsContent() {
            	var statisticsElement = document.getElementById("statistics");

            	if (statisticsElement != null) {
            		var parentNode = statisticsElement.parentNode;
                    
                    if (parentNode) {
                        parentNode.removeChild(statisticsElement);
                    }
            	}
            	
            	var panel = document.createElement('div');
            	panel.setAttribute('class', 'panel panel-default');
            	panel.setAttribute('id', 'statistics');
            	
            	var row = document.getElementById("statisticsrow");
            	row.appendChild(panel);
            }
            
            function createTabPanel() {
            	var statisticsElement = document.getElementById("statistics");
            	
            	var panelHeader = document.createElement('div');
                panelHeader.setAttribute('class', 'panel-heading');
                panelHeader.innerHTML = '&nbsp';
                statisticsElement.appendChild(panelHeader);
                
                var panelBody = document.createElement('div');
                panelBody.setAttribute('class', 'panel-body');
                statisticsElement.appendChild(panelBody);
                
                var navTabs = document.createElement('ul');
                navTabs.setAttribute('class', 'nav nav-tabs');
                navTabs.setAttribute('id', 'navTabs');
                panelBody.appendChild(navTabs);
                
                var tabContent = document.createElement('div');
                tabContent.setAttribute('class', 'tab-content');
                tabContent.setAttribute('id', 'tabContent');
                panelBody.appendChild(tabContent);
            }
            
            function createsubTab(data, active, index) {
            	var typeName = data.t;
            	
            	var navTabs = document.getElementById("navTabs");
            	var navSubTab = document.createElement('li');
            	if (1 == active) {
            		navSubTab.setAttribute('class', 'active');
            	} else {
            		navSubTab.setAttribute('class', '');
            	}
            	navTabs.appendChild(navSubTab);
            	
            	var link = document.createElement('a');
            	link.setAttribute('href', '#Type_' + index);
            	link.setAttribute('data-toggle', 'tab');
            	link.innerHTML = typeName;
            	navSubTab.appendChild(link);
            	
            	var tabContent = document.getElementById('tabContent');
            	var tabSubPane = document.createElement('div');
            	if (1 == active) {
            		tabSubPane.setAttribute('class', 'tab-pane fade active in');
            	} else {
            		tabSubPane.setAttribute('class', 'tab-pane fade');
            	}
            	tabSubPane.setAttribute('id', 'Type_' + index);
            	tabContent.appendChild(tabSubPane);
            }
            
            function showData(data) {
            	var jsonValue = eval('(' + data + ')');
            	var jsonTypeArray = eval('(' + jsonValue.type + ')');
            	
            	var len = parent.JSONLength(jsonTypeArray);
            	
            	if (len != 0) {
            		createTabPanel();
            	}
            	
            	for (var i = 0; i < len; i++) {
            		createsubTab(jsonTypeArray[i], 0 == i ? 1 : 0, i);
            		showTab(jsonTypeArray[i]);
            	}
            }
            
            function showTab(data) {
            	var type = data.t;
            	
            	
            }
        </script>
    </body>
</html>
