package com.fota.util.error;

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
    
    // 服务器管理    E_SERVER_*
 
    // 账户               E_USER_*
    E_USER_ADD(3000),
    E_USER_EDIT(3001),
    E_USER_DELETE(3002),
    E_USER_RESET_PASSWORD(3003),
    E_USER_PARA(3004),
    
    // 项目               E_PROJECT_*
    E_PROJECT_DUPLICATE_NAME(4000),
    E_PROJECT_EDIT(4001),
    E_PROJECT_DELETE(4002),
    E_PROJECT_NOT_EXIST(4003),
    E_PROJECT_VERSION_IN_USE(4004),
    
    // 版本               E_VERSION_*
    E_VERSION_ADD(5000),
    E_VERSION_EDIT(5001),
    E_VERSION_DELETE(5002),
    E_VERSION_RELEASE(5003),
    E_VERSION_EXIST(5004),
    E_VERSION_DATABASE(5005),
    E_VERSION_PARA(5006),
    E_VERSION_INFO(5007),
    E_VERSION_STATE(5008),
    E_VERSION_EXIST_REALSE(5009),
    E_VERSION_IMEI_UPDATE(5010),
    E_VERSION_IMEI_EMPTY(5011),
    E_VERSION_IMEI_DATABASE(5012),
    E_VERSION_UPGRADE_PINGPANG(5013),
    
    // 统计               E_STATISTIC_*
    E_STATISTIC_PRJGET(6000),
    E_STATISTIC_PARA(6001),
    E_STATISTIC_NOVERSION(6002),
    
    // 日志               E_RECORD_*
    E_RECORD_TIME(7000),
    E_RECORD_EMPTY(7001);
    
    private final int ec;

    ErrorCode(int ec) {
        this.ec = ec;
    }

    public int get() {
        return ec;
    }
}
