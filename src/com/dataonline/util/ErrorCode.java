package com.dataonline.util;

public enum ErrorCode {
    // 公共
    E_OK(0),
    E_FAIL(1),
    E_PARA(2),
    E_TABLE_ALREADY_EXIST(3),
    E_TABLE_NOT_EXIST(4),
    E_PARSE_USERID(5),
    E_DUPLICATE_ENTRY(6),
    E_LOGOUT(7),
    
    // 部署               E_DEPLOY_*
    E_DEPLOY_DATABASE_FAIL(1001),
    
    // 账户               E_USER_*
    E_USER_ADD(2000),
    E_USER_EDIT(2001),
    E_USER_QUERY(2002),
    E_USER_DELETE(2003),
    E_USER_RESET_PASSWORD(2004),
    
    // 节点               E_NODE_*
    E_NODE_ADD(3000),
    E_NODE_EDIT(3001),
    E_NODE_QUERY(3002),
    E_NODE_DELETE(3003),
    E_NODE_DUPLICATE_VALUE(3004),
    E_NODE_DUPLICATE_NAME(3005),
    
    // 类型               E_TYPE_*
    E_TYPE_ADD(4000),
    E_TYPE_EDIT(4001),
    E_TYPE_QUERY(4002),
    E_TYPE_DELETE(4003),
    E_TYPE_DUPLICATE(4004),
    
    // 数据	   E_VALUE_*
    E_VALUE_QUERY(5000);
    
    private final int ec;

    ErrorCode(int ec) {
        this.ec = ec;
    }

    public int get() {
        return ec;
    }
}
