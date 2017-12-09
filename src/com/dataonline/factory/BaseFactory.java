package com.dataonline.factory;

import org.apache.log4j.Logger;

import com.dataonline.config.*;
import com.dataonline.intfc.*;
import com.dataonline.service.*;
import com.dataonline.util.LineNo;

public class BaseFactory {
    private static final BaseFactory baseFactory = new BaseFactory();

    private Logger log = Logger.getLogger(BaseFactory.class);
    
    private String userName = new String();
    private String password = new String();
    private String databaseName = new String();

    private BaseFactory() {
        Init();
    }

    public static BaseFactory getInstance() {
        return baseFactory;
    }

    public void Init() {
        Database xml = new Database("config.xml");
        
        userName = xml.getUserName();
        if (null == userName) {
            userName = new String();
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "无法获得\"userName\"参数，请检查config.xml配置文件");
        }
        
        password = xml.getPassword();
        if (null == password) {
            password = new String();
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "无法获得\"password\"参数，请检查config.xml配置文件");
        }
        
        databaseName = xml.getDatabaseName();
        if (null == databaseName) {
            databaseName = new String();
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "无法获得\"databaseName\"参数，请检查config.xml配置文件");
        }
    }
    
    public IUser getUser() {
        IUser user = null;

        try {
            user = new UserService(databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return user;
    }

    public IType getType() {
        IType type = null;

        try {
        	type = new TypeService(databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return type;
    }

    public IValue getValue(int userID) {
        IValue value = null;

        try {
        	value = new ValueService(userID, databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return value;
    }
}
