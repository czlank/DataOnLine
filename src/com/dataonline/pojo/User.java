package com.fota.pojo.base;

import com.fota.impl.base.UserTypeOpt;
import com.fota.intfc.base.UserOpt;

public class User {
    private int opt          = UserOpt.O_NULL.get();

    private int id           = -1;
    private String name      = new String();
    private String password  = new String();
    private int type         = 0;
    private String token     = new String();
    private String oem       = new String();
    
    private int opID         = -1;              // 操作者id
    private String ip        = new String();    // 操作者ip

    public User() {
        
    }

    public void setOpt(int opt) {
        this.opt |= opt;
    }

    public void clearOpt(int opt) {
        this.opt &= ~(opt);
    }

    public int getOpt() {
        return opt;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setOEM(String oem) {
        this.oem = oem;
    }

    public String getOEM() {
        return oem;
    }
    
    public void setOpID(int opID) {
        this.opID = opID;
    }
    
    public int getOpID() {
        return opID;
    }
    
    public void setIP(String ip) {
        this.ip = ip;
    }
    
    public String getIP() {
        return ip;
    }
    
    public String toString() {
        String opCode = new String();
        String userTypeOpt = "";
        
        if (UserTypeOpt.ADMINISTRATOR.get() == type) {
            userTypeOpt = "管理员账号";
        } else if (UserTypeOpt.COMMON.get() == type) {
            userTypeOpt = "普通账号";
        }
        
        if (testOpt(opt, UserOpt.O_NAME)) {
            opCode += "名称(" + name + ") ";
        }

        if (testOpt(opt, UserOpt.O_PASSWORD)) {
            opCode += "密码(" + password + ") ";
        }

        if (testOpt(opt, UserOpt.O_TYPE)) {
            opCode += "类型(" + userTypeOpt + ") ";
        }

        if (testOpt(opt, UserOpt.O_TOKEN)) {
            opCode += "密钥(" + token + ") ";
        }

        if (testOpt(opt, UserOpt.O_OEM)) {
            opCode += "OEM(" + oem + ")";
        }
        
        return opCode;
    }
    
    private boolean testOpt(int srcopt, UserOpt destopt) {
        if (srcopt == UserOpt.O_ALL.get()) {
            return true;
        }
            
        return ((srcopt & destopt.get()) == destopt.get());
    }
}
