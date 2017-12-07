package com.fota.config;

import com.fota.util.common.XML;

public class Database extends BuildXMLFile {
    public Database(String fileName) {
        super(fileName);
    }

    public String getUserName() {
    	XML u = new XML(filePath);
    	return u.getData("/config/database/userName");
    }
    
    public boolean setUserName(String value) {
    	XML u = new XML(filePath);
    	return u.setData("/config/database/userName", value);
    }
    
    public String getPassword() {
    	XML u = new XML(filePath);
    	return u.getData("/config/database/password");	
    }
    
    public boolean setPassword(String value) {
    	XML u = new XML(filePath);
    	return u.setData("/config/database/password", value);
    }
    
    public String getDatabaseName() {
    	XML u = new XML(filePath);
    	return u.getData("/config/database/databaseName");	
    }
    
    public boolean setDatabaseName(String value) {
    	XML u = new XML(filePath);
    	return u.setData("/config/database/databaseName", value);
    }
    
    public boolean getFlag() {
    	XML u = new XML(filePath);
    	return "true".equals(u.getData("/config/database", "flag"));	
    }
    
    public boolean setFlag(boolean value) {
    	XML u = new XML(filePath);
    	return u.setData("/config/database", "flag", value ? "true" : "false");
    }
}
