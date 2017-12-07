package com.fota.util.error;

import java.util.Map;
import java.util.HashMap;

public class GetLastError {
    private static final GetLastError errorCode = new GetLastError();

    private Map<ErrorCode, String> errMsgMap = new HashMap<ErrorCode, String>();

    private GetLastError() {
        // 公共
        errMsgMap.put(ErrorCode.E_OK,                       "ok");
        errMsgMap.put(ErrorCode.E_FAIL,                     "失败");
        errMsgMap.put(ErrorCode.E_TABLE_ALREADY_EXIST,      "表已经存在");
        errMsgMap.put(ErrorCode.E_TABLE_NOT_EXIST,          "表不存在");
        errMsgMap.put(ErrorCode.E_PARSE_USERID,             "解析iUserID出错，userID = ");
        errMsgMap.put(ErrorCode.E_PARSE_PROJECTID,          "解析iUserID出错，projectID = ");
        errMsgMap.put(ErrorCode.E_DUPLICATE_ENTRY,          "重复的数据");
        errMsgMap.put(ErrorCode.E_LOGOUT,                   "登陆超时，请重新登录");
        
        // 部署
        errMsgMap.put(ErrorCode.E_DEPLOY_DATABASE_FAIL,     "部署数据库失败");
        
        // 服务器管理
        
        
        // 账户
        errMsgMap.put(ErrorCode.E_USER_ADD,                 "用户信息添加失败");
        errMsgMap.put(ErrorCode.E_USER_DELETE,              "删除用户信息失败");
        errMsgMap.put(ErrorCode.E_USER_EDIT,                "用户信息更新失败");
        errMsgMap.put(ErrorCode.E_USER_RESET_PASSWORD,      "重置用户密码失败");
        errMsgMap.put(ErrorCode.E_USER_PARA,                "参数错误");
        
        // 项目
        errMsgMap.put(ErrorCode.E_PROJECT_DUPLICATE_NAME,   "项目名称重复");
        errMsgMap.put(ErrorCode.E_PROJECT_EDIT,             "项目更新失败");
        errMsgMap.put(ErrorCode.E_PROJECT_DELETE,           "项目删除失败");
        errMsgMap.put(ErrorCode.E_PROJECT_NOT_EXIST,        "未找到项目");
        errMsgMap.put(ErrorCode.E_PROJECT_VERSION_IN_USE,   "版本正在使用中");
        
        // 版本        
        errMsgMap.put(ErrorCode.E_VERSION_ADD,              "版本添加失败");
        errMsgMap.put(ErrorCode.E_VERSION_EDIT,             "版本更新失败");
        errMsgMap.put(ErrorCode.E_VERSION_DELETE,           "版本删除失败");
        errMsgMap.put(ErrorCode.E_VERSION_RELEASE,          "版本发布/冻结失败");
        errMsgMap.put(ErrorCode.E_VERSION_EXIST,            "版本已存在，请检查版本后,重新提交请求");
        errMsgMap.put(ErrorCode.E_VERSION_INFO,             "版本信息错误");
        errMsgMap.put(ErrorCode.E_VERSION_DATABASE,         "数据库异常，版本更新失败");
        errMsgMap.put(ErrorCode.E_VERSION_PARA,             "版本参数错误");
        errMsgMap.put(ErrorCode.E_VERSION_STATE,            "版本状态更新失败");
        errMsgMap.put(ErrorCode.E_VERSION_EXIST_REALSE,     "已有同源版本处于发布状态,请先冻结该版本，重新提交请求");
        errMsgMap.put(ErrorCode.E_VERSION_IMEI_UPDATE,      "IMEI表更新失败");
        errMsgMap.put(ErrorCode.E_VERSION_IMEI_EMPTY,       "IMEI信息为空,请输入后,重新提交");
        errMsgMap.put(ErrorCode.E_VERSION_IMEI_DATABASE,    "数据库异常，IMEI更新失败");
        errMsgMap.put(ErrorCode.E_VERSION_UPGRADE_PINGPANG, "存在乒乓升级版本");
        
        // 统计
        errMsgMap.put(ErrorCode.E_STATISTIC_PRJGET,         "无法获取项目");
        errMsgMap.put(ErrorCode.E_STATISTIC_NOVERSION,      "版本数据为空");
        errMsgMap.put(ErrorCode.E_STATISTIC_PARA,           "统计参数错误");
        
        // 日志
        errMsgMap.put(ErrorCode.E_RECORD_TIME,              "无法获取时间范围");
        errMsgMap.put(ErrorCode.E_RECORD_EMPTY,             "记录为空");
    }

    public static GetLastError instance() {
        return errorCode;
    }
    
    public String getErrorMsg(ErrorCode errorCode) {
        if (errMsgMap.containsKey(errorCode)) {
            return errMsgMap.get(errorCode);
        }

        return new String("未知错误：") + String.valueOf(errorCode.get());
    }
}
