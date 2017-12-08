package com.dataonline.util;

public enum ErrorCode {
    // 公共
    E_OK(0),
    E_FAIL(1),
    E_TABLE_ALREADY_EXIST(2),
    E_TABLE_NOT_EXIST(3),
    E_PARSE_USERID(4),
    E_PARSE_PROJECTID(5),
    E_DUPLICATE_ENTRY(6),
    E_LOGOUT(7),
    // 部署               E_DEPLOY_*
    E_DEPLOY_DATABASE_FAIL(1001),
    
    // 账户               E_USER_*
    E_USER_ADD(2000),
    E_USER_EDIT(2001),
    E_USER_DELETE(2002),
    E_USER_RESET_PASSWORD(2003),
    E_USER_PARA(2004);
    
    private final int ec;

    ErrorCode(int ec) {
        this.ec = ec;
    }

    public int get() {
        return ec;
    }
}
