<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="prj" uri="/prj-tags"%>

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
        <prj:collect/>
        
        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <h2>项目统计</h2>
                </div>
            </div>

            <hr/>

            <div class="row" style="display: inline-block">
            <div class="col-md-12" style="margin-top:20px;" >
                <div class="stainput-group input-group">
                   <select id="prj-select" class="chosen-select-deselect"></select>
                </div>
                <div class="stainput-group input-group">
                    <div class="input-group" style="display:inline-block;vertical-align: middle;">
                        <div class="input-icon-group">
                            <input id="startdate" type="text" class="form-control"/>
                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span" ></span>        
                        </div>
                    </div>
                    <label class="timerpicker-label">-</label>
                    <div class="input-group" style="display:inline-block;vertical-align: middle;">
                        <div class="input-icon-group">
                            <input id="enddate" type="text" class="form-control"/>
                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span"></span>        
                        </div>
                    </div>
                </div>
                <div class="stainput-group input-group">
                    <label class="radio-inline">
                        <input type="radio" name="inlineRadioOptions" id="dayRadio" value="day"> 日
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="inlineRadioOptions" id="monRadio" value="mon"> 月度
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="inlineRadioOptions" id="yearRadio" value="year"> 年度
                    </label>
                </div>
                <div class="stainput-group input-group">
                    <input type="button" value="查询" class="btn btn-primary btn-sm" onclick="checkAndRedraw()"/>
                </div>
            </div>
            </div>
            
            <!-- 折线图-->
            <div class="row" style="margin-top:20px;">
                <div class="col-md-12">
                    <!-- 项目表开始 -->
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
        <script src="../assets/msgbox/fota.msg.js" language="JavaScript" type="text/javascript"></script>        
        <script src="../assets/js/chosen.jquery.js"></script>
        
        <script type="text/javascript">
            var prjID = parent.staSearchText.prjID;
            var period,startTime,endTime;
            parent.showLoading();

            window.onload=function() {
                initPrjOption();
                dateTimePicker.init();  
                periodRadio.init(); 
                parent.staSearchText = {};
                myChart.setOption(option);// 使用刚指定的配置项和数据显示图表。                
                window.onresize = myChart.resize;//图标内容大小自适应
                checkAndRedraw();
            };
            
            //project select
            function initPrjOption(){
                var jsPrj = '${jsonProject}';
                var jsPrjSelect = eval("(" + jsPrj + ")"); 
                var len = parent.JSONLength(jsPrjSelect);
                var opts = "";
                for (var i = 0; i < len; i++) {
                    opts += "<option value='"+jsPrjSelect[i]["id"]+"'>"+parent.jsonDecode(jsPrjSelect[i]["name"])+"</option>";
                }
                var select = $("#prj-select");
                select.append(opts);
                select.chosen({search_contains: true, width: '350px'});
                if(typeof(prjID) != "undefined" && prjID != null){
                    select.val(prjID);
                    select.trigger("chosen:updated");
                }
           }
            
            //datetimepicker
            var dateTimePicker = {
                init : function () {
                    var myDate = new Date;
                    var today, preToday;
                    if(typeof(parent.staSearchText.startTime) != "undefined" || typeof(parent.staSearchText.endTime) != "undefined"){
                        preToday = parent.staSearchText.startTime;
                        today = parent.staSearchText.endTime;
                    }else {
                        today = myDate.getFullYear() + '/' + (myDate.getMonth()+1) + '/' + myDate.getDate();
                        myDate.setDate(myDate.getDate() - 6);
                        preToday = myDate.getFullYear() + '/' + (myDate.getMonth()+1) + '/' + myDate.getDate();
                    }
                        
                    $("#startdate").datetimepicker({
                            lang:'ch',
                            timepicker:false,
                            format:'Y-m-d',
                            formatDate:'Y/m/d',
                            maxDate:0,
                            minDate:'2012/12/12',
                            value:preToday,
                            onChangeDateTime: function(dp, $input) {
                                startDate = $(this).val();
                            },
                            onClose: function(current_time, $input) {
                                if(checkTimer(0)){
                                    periodRadio.setPeriodEnable();
                                }
                            }
                    });
                    $("#enddate").datetimepicker({
                            lang:'ch',
                            timepicker:false,
                            format:'Y-m-d',
                            formatDate:'Y/m/d',
                            maxDate:0,
                            minDate:'2012/12/13',
                            value:today,
                            onClose: function(current_time, $input) {
                                if(checkTimer(1)){
                                    periodRadio.setPeriodEnable();
                                }
                            }
                    });
                }
            };
            
            //periodradio
            var periodRadio = {
                init : function (){//默认按日统计，日月可选           
                    $("#yearRadio").attr("disabled","disabled");
                    $("#dayRadio").attr("checked",true);
                },
                
                setPeriodEnable : function (){ //重置 radio样式 
                    var start = $("#startdate").val();
                    var end = $("#enddate").val();
                    var diffDays = DateDiff(start, end);
                    $("input:radio").attr("disabled",false).attr("checked",false);
                    if (diffDays < 90) {
                        //默认按日统计，日月可选
                        $("#yearRadio").attr("disabled","disabled");
                        $("#dayRadio").attr("checked",true).prop("checked", true);
                    } else {
                        //默认按月统计，年月可选
                        $("#dayRadio").attr("disabled","disabled");
                        $("#monRadio").attr("checked",true).prop("checked", true);
                    }
                }
            };
            
            //eCharts
            var myChart = echarts.init(document.getElementById('chart-panel'), 'macarons');
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
                    right: '4%',
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
            
            function getXData(){
                var start = $("#startdate").val();
                var end = $("#enddate").val();
                var type = $('input:radio:checked').val();
                if("year" == type){
                    return getYears(start, end);
                } else if("mon" == type){
                    return getMonths(start, end);
                } else{
                    return getDays(start, end);
                }
            }
            
            function reDrawMychart(chartdata){
                option.xAxis.data = getXData();
                option.series[0].data = chartdata;
                myChart.setOption(option); 
            }
            
            //time相关计算
            
            function checkTimer(oper){//检查 起始时间是否＜结束时间
                var startTime = $("#startdate").val();
                var endTime = $("#enddate").val();
                if (startTime > endTime) {
                    if(1 == oper){//end
                        $("#startdate").addClass('attention-focus');
                        window.setTimeout(function () {
                            $("#startdate").removeClass('attention-focus');
                        }, 1500);
                    }else{//start
                        $("#enddate").addClass('attention-focus');
                        window.setTimeout(function () {
                            $("#enddate").removeClass('attention-focus');
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
                    result.push((new Date(parseInt(k))).format());///console.log((new Date(parseInt(k))).format());
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
            
            function reDrawMytable(tabledata){
                dataSet = tabledata; 
                if(typeof(myTable) != "undefined"){
                    myTable.clear();//清空数据.fnClearTable();//清空数据  
                    myTable.destroy(); //还原初始化了的datatable
                
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
                }else{
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
