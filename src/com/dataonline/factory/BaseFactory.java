package com.fota.factory.base;

import org.apache.log4j.Logger;

import com.fota.config.Database;
import com.fota.intfc.base.*;
import com.fota.service.base.*;
import com.fota.util.common.LineNo;

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

    public IProject getProject() {
        IProject project = null;

        try {
            project = new ProjectService(databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return project;
    }

    public IVersion getVersion() {
        IVersion version = null;

        try {
            version = new VersionService(databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return version;
    }

    public IIMEI getIMEI() {
        IIMEI imei = null;

        try {
            imei = new IMEIService(databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return imei;
    }

    public ITerminal getTerminal() {
        ITerminal terminal = null;

        try {
            terminal = new TerminalService(databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return terminal;
    }
    
    public IRecord getRecord() {
        IRecord record = null;
        
        try {
            record = new RecordService(databaseName, userName, password);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return record;
    }
}
