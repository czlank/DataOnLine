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
        <link href="../assets/css/font-awesome.css" rel="stylesheet"/>

        <!-- CUSTOM STYLES-->        
        <link href="../assets/css/custom.css" rel="stylesheet"/>
        <link href="../assets/js/dataTables/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="../assets/css/jquery.datetimepicker.css" rel="stylesheet"/>
        
        <link href="../assets/css/chosen.css" rel="stylesheet"/>
    </head>

    <body>
        <statistics:collect/>
        
        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <h2>数据统计</h2>
                </div>
            </div>

            <hr/>

            <div class="row" style="display: inline-block">
            <div class="col-md-12" style="margin-top:20px;">
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
            parent.showLoading();

            window.onload=function() {
                dateTimePicker.init();  
                periodRadio.init(); 
                parent.staSearchText = {};
                myChart.setOption(option);// 使用刚指定的配置项和数据显示图表。                
                window.onresize = myChart.resize;//图标内容大小自适应
                checkAndRedraw();
            };
            
            // datetimepicker
            var dateTimePicker = {
                init : function () {
                    var myDate = new Date;
                    var today, preToday;
                    if (typeof(parent.staSearchText.startTime) != "undefined" || typeof(parent.staSearchText.endTime) != "undefined"){
                        preToday = parent.staSearchText.startTime;
                        today = parent.staSearchText.endTime;
                    } else {
                        today = myDate.getFullYear() + '/' + (myDate.getMonth()+1) + '/' + myDate.getDate();
                        myDate.setDate(myDate.getDate() - 6);
                        preToday = myDate.getFullYear() + '/' + (myDate.getMonth()+1) + '/' + myDate.getDate();
                    }
                        
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
            
            // table
            var dataSet;
            var myTable;
            function initTable(){
                myTable = $('#sta-table').DataTable({
                    data: dataSet,
                    columns: [
                        {"title": "版本名", data : "name"},
                        {"title": "总数", data : "num", "width": "10%"},
                        {"title": "检测成功数", data : "checkNum", "width": "12%"},
                        {"title": "下载成功数", data : "downSucNum", "width": "12%"},
                        {"title": "下载失败数", data : "downFailNum", "width": "12%"},
                        {"title": "升级成功数", data : "updateSucNum", "width": "12%"},
                        {"title": "升级失败数", data : "updateFailNum", "width": "12%"},
                        {"title": "详情" ,data : null, "width": "10%"}
                    ],
                    columnDefs:[
                        {
                            targets: 7,
                            render: function (data, type, row, meta) {
                                return '<a href="#" onclick=goToStatisticsVer(\'' + row.name + '\') >详情</a>';
                            }
                        },
                        {   "orderable": false, "targets": 7 },
                        {   className: "tablecenter-colum", "targets": [1,2,3,4,5,6,7] },
                        {   "orderable": true, "targets": 0 }
                    ]
                });

                $("select[name='sta-table_length']") .change(function () {
                    parent.setIframeHeight();
                });
            }            
            
            function reDrawMytable(tabledata) {
                dataSet = tabledata; 
                if (typeof(myTable) != "undefined") {
                    myTable.clear();    //清空数据  
                    myTable.destroy();  //还原初始化了的datatable
                }
                initTable();
            }
            
            function goToStatisticsVer(verName){
                parent.showLoading();
                //parent.saveStatisInfo($("input[type='search'").val());//保存所选的项目、时间、X轴，表格搜索框信息
                $.ajax({
                        type: "POST",
                        url:"../manager/Statistics.html",
                        data:{
                            prjID : prjID,
                            versionName : verName,
                            actionStatistic : "goToStatisticsVer",
                            startTime :startTime,
                            endTime : endTime,
                            period : period//day  mon  year
                        },
                        async: false,
                        error: function(request) {
                            parent.hideLoading();
                            FotaAlert("Connection error");
                        },
                        success: function(data) {
                            var url = "../manager/StatisticsVer.jsp" + "?prjId=" + prjID + "&version=" + verName + "&startTime=" + startTime + "&endTime=" + endTime + "&period=" + period;
                            parent.staSearchText.prjID = prjID;
                            parent.staSearchText.startTime = startTime;
                            parent.staSearchText.endTime = endTime;
                            parent.staSearchText.period = period;
                            onAjaxSuccess(data, url);
                        }
                });
            }
            
            function checkAndRedraw(){
                if(!checkTimer(1)){
                    FotaAlert("结束时间不能小于结束时间！");
                    return;
                }
                parent.showLoading();
                var params = {
                    actionStatistic : "getPrjStadata",
                    prjID : $("#prj-select").val(),
                    startTime : $("#startdate").val(),
                    endTime : $("#enddate").val(),
                    period : $('input:radio:checked').val()//day  mon  year
                };
                $.ajax({
                    type : "post",
                    async : false, //同步执行
                    url : "../manager/Statistics.html",
                    data : params,
                    error:function(){
                         parent.hideLoading();
                         FotaAlert("Connection error");
                    },
                    success : function(result) {//返回结果格式{chartdata:[1,2,3,4,5],tabledata:[["",1,2,3,4,5,6,'详情'],["",1,2,3,4,5,6,'详情']]}
                        onAjaxSuccess(result);
                    }
                });
            }
            
            function onAjaxSuccess(msg, href, confirmHandler) {
                var Info = eval('(' + msg + ')');
                
                if ('ok' == Info.result) {
                    if("" != href){
                        window.location.href = href;
                    }else{
                        parent.hideLoading();
                    }
                } else if("logout" == Info.result) {
                    parent.hideLoading();
                    FotaAlert("登陆超时，请重新登录！",function() {
                        window.location.href = "../login.jsp";
                    });                                
                } else if ('error' == Info.result) {
                    parent.hideLoading();
                    FotaAlert(Info.tipMsg); 
                } else if ('confirm' == Info.result) {
                    parent.hideLoading();
                    if($.isFunction(confirmHandler)) {
                        FotaConfirm(Info.tipMsg, confirmHandler);
                    }                    
                } else {
                    Info.chartdata = typeof(Info.chartdata) == "undefined" ? [] : Info.chartdata;
                    Info.tabledata = typeof(Info.tabledata) == "undefined" ? [] : Info.tabledata;
                    if (Info) {
                        prjID = $("#prj-select").val();
                        startTime = $("#startdate").val();
                        endTime = $("#enddate").val();
                        period = $('input:radio:checked').val();
                        parent.hideLoading();
                        reDrawMychart(Info.chartdata);
                        reDrawMytable(Info.tabledata);
                    }
                    parent.hideLoading();
                }
            }
        </script>
    </body>
</html>
