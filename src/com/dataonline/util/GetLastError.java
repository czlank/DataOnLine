package com.dataonline.util;

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
        
        // 账户
        errMsgMap.put(ErrorCode.E_USER_ADD,                 "用户信息添加失败");
        errMsgMap.put(ErrorCode.E_USER_EDIT,                "用户信息更新失败");
        errMsgMap.put(ErrorCode.E_USER_DELETE,              "删除用户信息失败");
        errMsgMap.put(ErrorCode.E_USER_RESET_PASSWORD,      "重置用户密码失败");
        errMsgMap.put(ErrorCode.E_USER_PARA,                "参数错误");
        
        // 类型
        errMsgMap.put(ErrorCode.E_TYPE_ADD,                 "类型信息添加失败");
        errMsgMap.put(ErrorCode.E_TYPE_EDIT,                "类型信息更新失败");
        errMsgMap.put(ErrorCode.E_TYPE_DELETE,              "删除类型信息失败");
        errMsgMap.put(ErrorCode.E_TYPE_PARA,                "参数错误");
        errMsgMap.put(ErrorCode.E_TYPE_DUPLICATE, 			"类型值重复");
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
