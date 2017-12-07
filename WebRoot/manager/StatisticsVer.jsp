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
        <link href="../assets/css/font-awesome.css" rel="stylesheet"/>

        <!-- CUSTOM STYLES-->        
        <link href="../assets/css/custom.css" rel="stylesheet"/>
        <link href="../assets/js/dataTables/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="../assets/css/jquery.datetimepicker.css" rel="stylesheet"/>
        
        <%
        String prjId = (String)request.getParameter("prjId");
        String version = (String)request.getParameter("version");
        String start = (String)request.getParameter("startTime");
        String end = (String)request.getParameter("endTime");
        String period = (String)request.getParameter("period");          
        %>
    </head>

    <body>
        
        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <h2>版本统计</h2>
                    <a href="#" onclick="backToPrjSta()" style="float: right;"><i class="fa fa-reply"></i>返回</a>
                </div>
            </div>

            <hr/>

            <div class="row">
            <div class="col-md-12" style="margin-top:20px;" >
                <div class="stainput-group input-group">
                     <font id="verName" size="5" ><%=version%></font>
                </div>
                <div class="stainput-group input-group">
                    <div class="input-group" style="display:inline-block;vertical-align: middle;">
                        <div class="input-icon-group">
                            <input id="startdate" type="text" class="form-control" value=<%=start%> ></input>
                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span" ></span>        
                        </div>
                    </div>
                    <label class="timerpicker-label">-</label>
                    <div class="input-group" style="display:inline-block;vertical-align: middle;">
                        <div class="input-icon-group">
                            <input id="enddate" type="text" class="form-control" value=<%=end%> ></input>
                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span"></span>        
                        </div>
                    </div>
                </div>
                <div class="stainput-group input-group">
                    <label class="radio-inline">
                        <input type="radio" name="verRadioOptions" id="dayRadio" value="day"> 日
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="verRadioOptions" id="monRadio" value="mon"> 月度
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="verRadioOptions" id="yearRadio" value="year"> 年度
                    </label>
                </div>
                <div class="stainput-group input-group">
                    <input type="button" value="查询" class="btn btn-primary btn-sm" onclick="checkAndRedraw(pageState.VERSION)"/>
                </div>
            </div>
            </div>
            
            <!-- 折线图-->
            <div class="row" style="margin-top:20px;">
                <div class="col-md-12">
                    <!-- 版本表开始 -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <font face=宋体 color=#23ADCF size=5>统计</font>
                        </div>

                        <div class="panel-body">
                            <div id="chart-panel" style="width: 100%;height:300px;">
                                
                            </div>
                        </div>
                    </div>
                    <!-- 项目表结束 -->
                </div>
            </div>
            
            <!-- 详情表-->
            <div class="row">
                <div class="col-md-12">
                    <!-- 项目表开始 -->
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <font face=宋体 color=#23ADCF size=5>详情表</font>
                        </div>

                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="sta-table" style="word-break:break-all; word-wrap:break-all;" width="100%">
                                   
                                </table>                                
                            </div>
                        </div>
                    </div>
                    <!-- 项目表结束 -->
                </div>
            </div>
            
            <!-- 弹出框-->
            <div>
                <jsp:include page="StatisticsMaintenance.jsp"/>
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
        <script src="../assets/msgbox/fota.msg.js" language="JavaScript" type="text/javascript"></script>
        <script src="../assets/js/echarts.js"></script>        
        <script src="../assets/js/macarons.js"></script>

        <script type="text/javascript">
            var prjID = <%=prjId%>;
            var versionName;
            var period,startTime,endTime;
            var srcVersion,desVersion,versionID,userID;
            var pageState = {VERSION:0, PACKAGE:1};
            var curPage = pageState.VERSION;
            
            parent.showLoading();
            
            window.onload=function() {
                versionName = $("#verName").text();
                initDatapicker(pageState.VERSION);
                initDatapicker(pageState.PACKAGE);
                setPeriodEnable(pageState.VERSION);
                myChart.setOption(option);
                window.onresize = myChart.resize;
                initTable();
                checkAndRedraw(pageState.VERSION);
                $('#pagChart').on('shown.bs.modal',function(){
                    //1、传入时间、版本号等信息；2、初始化折线图；3、重绘折线图
                    $("#package").text(srcVersion + " - " + desVersion);
                    $("#startdatePag").val(startTime);
                    $("#enddatePag").val(endTime);
                    setPeriodEnable(pageState.PACKAGE);
                    pagChart = echarts.init(document.getElementById('chartPag-panel'),'macarons');
                    checkAndRedraw(pageState.PACKAGE);
                    $(window).scrollTop(0);
                });
            };
            
            
            //Datapicker
            function initDatapicker(page) {
                var $start,$end;
                if(pageState.PACKAGE == page){
                    $start = $("#startdatePag");
                    $end = $("#enddatePag");
                } else {
                    $start = $("#startdate");
                    $end = $("#enddate");
                }
                $start.datetimepicker({
                    lang:'ch',
                    timepicker:false,
                    format:'Y-m-d',
                    formatDate:'Y/m/d',
                    maxDate:0,
                    minDate:'2012/12/12',
                    onClose: function(current_time, $input) {
                        if(checkTimer(0, $start, $end)){
                            setPeriodEnable(page);
                        }
                    }
                });
                $end.datetimepicker({
                    lang:'ch',
                    timepicker:false,
                    format:'Y-m-d',
                    formatDate:'Y/m/d',
                    maxDate:0,
                    minDate:'2012/12/13',
                    onClose: function(current_time, $input) {
                        if(checkTimer(1, $start, $end)){
                            setPeriodEnable(page);
                        }
                    }
                });
            }
            
            //radio
            function setPeriodEnable(page){ //重置 radio样式 
                var $radio,$year,$mon,$day;
                var $start,$end;
                if(pageState.PACKAGE == page){
                    $radio = $("input[name=pagRadioOptions]");
                    $year = $("#yearRadioPag");
                    $mon = $("#monRadioPag");
                    $day = $("#dayRadioPag");
                    $start = $("#startdatePag");
                    $end = $("#enddatePag");
                } else {
                    $radio = $("input[name=verRadioOptions]");
                    $year = $("#yearRadio");
                    $mon = $("#monRadio");
                    $day = $("#dayRadio");
                    $start = $("#startdate");
                    $end = $("#enddate");
                }
                var diffDays = DateDiff($start.val(), $end.val());
                $radio.attr("disabled",false).attr("checked",false);
                if (diffDays < 90) {
                    //默认按日统计，日月可选
                    $year.attr("disabled","disabled");
                    $day.attr("checked",true).prop("checked", true);
                } else {
                    //默认按月统计，年月可选
                   $day.attr("disabled","disabled");
                   $mon.attr("checked",true).prop("checked", true);
                }
            }
            
            //eCharts
            var myChart = echarts.init(document.getElementById('chart-panel'),'macarons');
            var pagChart;
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['升级成功数']
                },
                grid: {
                    left: '3%',
                    right: '4.2%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    name: '时间',
                    boundaryGap: false,
                    data: []
                },
                yAxis: {
                    type: 'value',
                    name: '升级成功数'
                },
                series: [{
                    name: '升级成功数',
                    type: 'line',
                    stack: '总量',
                    data: []
                }]
            };
            
            function reDrawChart(xaxisData, chartdata, page){
                option.xAxis.data = xaxisData;
                option.series[0].data = chartdata;
                if(pageState.PACKAGE == page){
                	pagChart.setOption(option);
                } else {
                	myChart.setOption(option);
                }                
            }
            
            //time相关计算            
            function checkTimer(oper, start, end){//检查 起始时间是否＜结束时间
                var startTime = start.val();
                var endTime = end.val();
                if (startTime > endTime) {
                    if(1 == oper){//end
                        start.addClass('attention-focus');
                        window.setTimeout(function () {
                            start.removeClass('attention-focus');
                        }, 1500);
                    }else{//start
                        end.addClass('attention-focus');
                        window.setTimeout(function () {
                            end.removeClass('attention-focus');
                        }, 1500);
                    }
                    return false;
                } else{
                    return true;
                }
            }
            
            function DateDiff(sDate1, sDate2) {  //sDate1和sDate2是yyyy-MM-dd格式, 计算时间差--天数 
                var aDate, oDate1, oDate2, iDays;
                aDate = sDate1.split("-");
                oDate1 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);  //转换为yyyy-MM-dd格式
                aDate = sDate2.split("-");
                oDate2 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
                iDays = parseInt(Math.abs(oDate1.getTime() - oDate2.getTime()) / 1000 / 60 / 60 / 24); //把相差的毫秒数转换为天数
             
                return iDays;  //返回相差天数
            }
            
            Date.prototype.format = function (){
                var s='';
                s+=this.getFullYear()+'-';// 获取年份。
                s+=(this.getMonth()+1)+"-";         // 获取月份。
                s+= this.getDate();                 // 获取日。
                return(s);                          // 返回日期。
            };
            
            function getDays(begin,end){
                var result = [];
                var starts = begin.split("-");
                var ends = end.split("-");
                
                var db = new Date();
                db.setUTCFullYear(starts[0], starts[1]-1, starts[2]);
                var de = new Date();
                de.setUTCFullYear(ends[0], ends[1]-1, ends[2]);
                var unixDb = db.getTime();
                var unixDe = de.getTime();
                for(var k = unixDb; k <= unixDe;){
                    result.push((new Date(parseInt(k))).format());
                    k = k+24*60*60*1000;
                }
                
                return result;
            }
            
            function getMonths(start, end) {
                var result = [];
                var starts = start.split('-');
                var ends = end.split('-');
                
                var staYear = parseInt(starts[0]);
                var staMon = parseInt(starts[1]);
                var endYear = parseInt(ends[0]);
                var endMon = parseInt(ends[1]);
                while (staYear <= endYear) {
                    if (staYear === endYear) {
                        while (staMon <= endMon) {
                            result.push(staYear + '-' + staMon);
                            staMon++;
                        }
                        staYear++;
                    } else {
                        if (staMon > 12) {
                            staMon = 1;
                            staYear++;
                        }
                        result.push(staYear + '-' + staMon);
                        staMon++;
                    }
                }                
                return result;
            }
            
            function getYears(start, end) {
                var result = [];
                var starts = start.split('-');
                var ends = end.split('-');
                
                var staYear = parseInt(starts[0]);
                var endYear = parseInt(ends[0]);
                while (staYear <= endYear) {
                    result.push(staYear);
                    staYear++;
                }                
                return result;
            }
            
            //table
            var dataSet = [];
            var myTable;
            function initTable(){
                myTable = $('#sta-table').DataTable({
                    data: dataSet,
                    columns: [
                        {"title": "源版本", data: "srcVersion"},
                        {"title": "目标版本", data: "DesVersion"},
                        {"title": "总数", data: "num"},
                        {"title": "检测成功数", data: "checkNum"},
                        {"title": "下载成功数", data: "downSucNum"},
                        {"title": "下载失败数", data: "downFailNum"},
                        {"title": "升级成功数", data: "updateSucNum"},
                        {"title": "升级失败数", data: "updateFailNum"},
                        {"title": "占比", data: "percent"},
                        {"title": "导出", data: null},
                        {"title": "详情", data: null},
                        {"title": "ID", data: "verID"},
                        {"title": "usrID", data: "userID"}
                    ],
                    columnDefs:[
                        {
                            targets: 9,
                            render: function (data, type, row, meta) {
                                return '<a href="#" onclick=checkout(' + row.verID + ') >导出</a>';
                            }
                        },{
                            targets: 10,
                            render: function (data, type, row, meta) {
                                return '<a href="#" data-toggle="modal" data-target="#pagChart" onclick=showPagChart(\'' + row.srcVersion + '\',\'' + row.DesVersion + '\',\'' + row.verID + '\',\'' + row.userID + '\') >详情</a>';
                            }
                        },
                        { "orderable": false, "targets": [9,10] },
                        { className: "tablecenter-colum", "targets": [2,3,4,5,6,7,8,9,10] },
                        { "orderable": true, "targets": 0 },
                        { "targets": [ 11,12 ], "visible": false }
                    ]
                });
                
                $("select[name='sta-table_length']") .change(function () {
                    parent.setIframeHeight();
                });
            }            
            
            function reDrawMytable(tabledata){
                dataSet = tabledata; 
                if(typeof(myTable) != "undefined"){
                	myTable.clear();//清空数据.fnClearTable();//清空数据  
                    myTable.destroy(); //还原初始化了的datatable
                	
                }
                initTable();
            } 
                       
            function checkout(verID) {
                window.location.href = "../manager/Statistics.html?actionStatistic=pkgTerminalTabCheckout&verID=" + verID +"&startTime=" + startTime + "&endTime=" + endTime;
            }
            function showPagChart(src, des, verid, usrID){
                srcVersion = src;
                desVersion = des;
                versionID = verid;
                userID    = usrID;
            }
            
            function checkAndRedraw(page){
                var start, end, radioChecked, action;
                if(pageState.PACKAGE == page){
                    start = $("#startdatePag").val();
                    end = $("#enddatePag").val();
                    radioChecked = $('input[name=pagRadioOptions]:checked').val();
                    action = "getPagStadata";
                    if(!checkTimer(1, $("#startdatePag"), $("#enddatePag"))){
                        FotaAlert("结束时间不能小于结束时间！");
                        return;
                    }
                } else {
                    start = $("#startdate").val();
                    end = $("#enddate").val();
                    radioChecked = $('input[name=verRadioOptions]:checked').val();
                    action = "getVerStadata";
                    if(!checkTimer(1, $("#startdate"), $("#enddate"))){
                        FotaAlert("结束时间不能小于结束时间！");
                        return;
                    }
                }
                
                parent.showLoading();
                var params = {
                        actionStatistic : action,
                        prjID : prjID,
                        versionName : versionName,
                        startTime : start,
                        endTime : end,
                        period : radioChecked
                };
                if(pageState.PACKAGE == page){
                   params.verID = versionID;
                   params.userID = userID;
                }
                
                $.ajax({
                    type : "post",
                    async : false, //同步执行
                    url : "../manager/Statistics.html",
                    data : params,
                    error:function(){
                        parent.hideLoading();
                        FotaAlert("Connection error");
                    },
                    success : function(result) {//返回结果格式{"chartdata":[1,2,3,4,5],"tabledata":[["",1,2,3,4,5,6,'详情'],["",1,2,3,4,5,6,'详情']]}
                        startTime = start;
                        endTime = end;
                        period = radioChecked;
                        curPage = page;
                        onAjaxSuccess(result);
                    }
                });
            }
            
            function backToPrjSta(){
                parent.showLoading();
                $.ajax({
                        type: "POST",
                        url:"../manager/Statistics.html",
                        data:{
                            prjID : prjID,
                            versionName : versionName,
                            actionStatistic : "backToStatisticsPrj",
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
                            var url = "../manager/Statistics.jsp";//var url = "../manager/Statistics.jsp" + "?prjId=" + prjID + "&version=" + versionName;
                            onAjaxSuccess(data, url);
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
                    FotaAlert("登陆超时，请重新登录！",function(){
                        window.location.href = "../login.jsp";
                    });                                
                } else if ('error' == Info.result) {
                    parent.hideLoading();
                    FotaAlert(Info.tipMsg); 
                } else if ('confirm' == Info.result) {
                    parent.hideLoading();
                    if($.isFunction(confirmHandler)){
                        FotaConfirm(Info.tipMsg, confirmHandler);
                    }                    
                } else {
                    if (Info) {
                        var xaxisData;
                        if("year" == period) {
                            xaxisData = getYears(startTime, endTime);
                        } else if("mon" == period) {
                            xaxisData = getMonths(startTime, endTime);
                        } else{
                            xaxisData = getDays(startTime, endTime);
                        }
                        Info.chartdata = typeof(Info.chartdata) == "undefined" ? [] : Info.chartdata;
                        reDrawChart(xaxisData, Info.chartdata, curPage);
                        if(pageState.VERSION == curPage) {
                            Info.tabledata = typeof(Info.tabledata) == "undefined" ? [] : Info.tabledata;
                            reDrawMytable(Info.tabledata);
                        }
                        parent.hideLoading();
                    }
                    parent.hideLoading();
                }
            }
        </script>

    </body>
</html>
