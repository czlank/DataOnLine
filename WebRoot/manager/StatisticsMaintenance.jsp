<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta content="text/html;charset=UTF-8"/>
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

        <script src="../assets/msgbox/fota.msg.js" language="JavaScript" type="text/javascript"></script>
        <link href="../assets/css/jquery.datetimepicker.css" rel="stylesheet"/>
    </head>

    <body>
        <div class="modal fade" id="pagChart" tabindex="-1" role="dialog" aria-labelledby="projectLabel" aria-hidden="true"  data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content" style="width: 1100px;text-align: center;margin-left: -250px;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="editProjectTitle"></h4>
                    </div>

                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12" style="margin-top:20px;" >
                                <div class="stainput-group">
                                    <font id="package" size="5" ></font>
                                </div>
                            </div>
                            <div class="col-md-12" style="margin-top:40px;">
                                <div class="stainput-group input-group" style="float:right;">
                                    <input type="button" value="查询" class="btn btn-primary btn-sm" onclick="checkAndRedraw(pageState.PACKAGE)"/>
                                </div>
                                <div class="stainput-group input-group" style="float:right;">
                                    <label class="radio-inline">
                                        <input type="radio" name="pagRadioOptions" id="dayRadioPag" value="day"> 日
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="pagRadioOptions" id="monRadioPag" value="mon"> 月度
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="pagRadioOptions" id="yearRadioPag" value="year"> 年度
                                    </label>
                                </div>
                                <div class="stainput-group input-group" style="float:right;">
                                    <div class="input-group" style="display:inline-block;vertical-align: middle;">
                                        <div class="input-icon-group">
                                            <input id="startdatePag" type="text" class="form-control" ></input>
                                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span" ></span>        
                                        </div>
                                    </div>
                                    <label class="timerpicker-label">-</label>
                                    <div class="input-group" style="display:inline-block;vertical-align: middle;">
                                        <div class="input-icon-group">
                                            <input id="enddatePag" type="text" class="form-control" ></input>
                                            <span class="glyphicon glyphicon-calendar form-control-feedback timepicker-span"></span>        
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>            
            
                        <!-- 折线图-->
                        <div class="row" style="margin-top:10px;">
                            <div class="col-md-12">
                                <div class="panel panel-default">
                                    <div class="panel-body">
                                        <div id="chartPag-panel" style="width: 100%;height:300px;"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 折线图结束-->
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
        <script src="../assets/js/jquery.datetimepicker.full.js"></script>
        <script src="../assets/msgbox/fota.msg.js" language="JavaScript" type="text/javascript"></script>
        <script src="../assets/js/echarts.js"></script>        
        <script src="../assets/js/macarons.js"></script>

    </body>
</html>
