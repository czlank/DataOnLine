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
        errMsgMap.put(ErrorCode.E_PARA,                     "参数错误");
        errMsgMap.put(ErrorCode.E_TABLE_ALREADY_EXIST,      "表已经存在");
        errMsgMap.put(ErrorCode.E_TABLE_NOT_EXIST,          "表不存在");
        errMsgMap.put(ErrorCode.E_PARSE_USERID,             "解析iUserID出错，userID = ");
        errMsgMap.put(ErrorCode.E_DUPLICATE_ENTRY,          "重复的数据");
        errMsgMap.put(ErrorCode.E_LOGOUT,                   "登陆超时，请重新登录");
        
        // 部署
        errMsgMap.put(ErrorCode.E_DEPLOY_DATABASE_FAIL,     "部署数据库失败");
        
        // 账户
        errMsgMap.put(ErrorCode.E_USER_ADD,                 "帐户信息添加失败");
        errMsgMap.put(ErrorCode.E_USER_EDIT,                "帐户信息更新失败");
        errMsgMap.put(ErrorCode.E_USER_QUERY,				"未找到该帐户");
        errMsgMap.put(ErrorCode.E_USER_DELETE,              "删除帐户信息失败");
        errMsgMap.put(ErrorCode.E_USER_RESET_PASSWORD,      "重置帐户密码失败");
        
        // 节点
        errMsgMap.put(ErrorCode.E_NODE_ADD,                 "节点信息添加失败");
        errMsgMap.put(ErrorCode.E_NODE_EDIT,                "节点信息更新失败");
        errMsgMap.put(ErrorCode.E_NODE_QUERY,				"未找到该节点");
        errMsgMap.put(ErrorCode.E_NODE_DELETE,              "删除节点信息失败");
        errMsgMap.put(ErrorCode.E_NODE_DUPLICATE_VALUE, 	"节点值重复");
        errMsgMap.put(ErrorCode.E_NODE_DUPLICATE_NAME, 		"节点名称重复");
        
        // 类型
        errMsgMap.put(ErrorCode.E_TYPE_ADD,                 "类型信息添加失败");
        errMsgMap.put(ErrorCode.E_TYPE_EDIT,                "类型信息更新失败");
        errMsgMap.put(ErrorCode.E_TYPE_QUERY,				"未找到该类型");
        errMsgMap.put(ErrorCode.E_TYPE_DELETE,              "删除类型信息失败");
        errMsgMap.put(ErrorCode.E_TYPE_DUPLICATE, 			"类型值重复");
        
        // 数据
        errMsgMap.put(ErrorCode.E_VALUE_QUERY, 				"未找到数据");
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
