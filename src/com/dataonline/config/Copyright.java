package com.fota.config;

import com.fota.util.common.XML;

public class Copyright extends BuildXMLFile {
    public Copyright(String fileName) {
        super(fileName);
    }
    
    public String getCopyright() {
        XML u = new XML(filePath);        
        return u.getData("/config/copyright/info");
    }
    
    public String getVersion() {
        XML u = new XML(filePath);
        return u.getData("/config/copyright/version");
    }
}
