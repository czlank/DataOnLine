package com.dataonline.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.apache.log4j.Logger;

public class XML {
    private Logger log = Logger.getLogger(XML.class);
    
    private Document doc = null;
    private String filePath = new String();

    public XML(String filePath) {
        this.filePath = filePath;
        read();
    }
    
    public String getData(String path) {
        if (null == path || "".equals(path) || null == doc) {
            return null;
        }

        try {
            Element elem = (Element)doc.selectSingleNode(path);
            return elem.getTextTrim();
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return null;
    }
    
    public boolean setData(String path, String value) {
        if (null == path || "".equals(path) || null == value || "".equals(value) || null == doc) {
            return false;
        }
        
        Element elem = null;

        elem = (Element)doc.selectSingleNode(path);
        if (null == elem) {
            return false;
        }
        
        elem.setText(value);
        return write();
    }
    
    public String getData(String path, String attr) {
        if (null == path || "".equals(path) || null == attr || "".equals(attr) || null == doc) {
            return null;
        }
        
        try {
            String attrNode = path + "[@" + attr + "]";
            Element elem = (Element)doc.selectSingleNode(attrNode);
            
            return elem.attributeValue(attr);
        } catch (Exception e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return null;
    }
    
    public boolean setData(String path, String attr, String value) {
        if (null == path || "".equals(path) || null == attr || "".equals(attr) || null == value || "".equals(value) || null == doc) {
            return false;
        }
        
        Element elem = null;

        elem = (Element)doc.selectSingleNode(path + "[@" + attr + "]");
        if (null == elem) {
            return false;
        }
        
        Attribute tempAttr = elem.attribute(attr);
        tempAttr.setValue(value);
        return write();
    }

    private void read() {
        if (null == filePath || "".equals(filePath)) {
            return;
        }

        try {
            SAXReader reader = new SAXReader();
            doc = reader.read(new File(filePath));
        } catch (DocumentException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
    }

    private boolean write() {
        if (null == filePath || "".equals(filePath)) {
            return false;
        }
        
        OutputFormat format = OutputFormat.createPrettyPrint();
        
        format.setEncoding("UTF-8");
        format.setNewlines(true);
        format.setIndent(true);

        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileWriter(filePath), format);
            
            writer.write(doc);
            writer.close();
            
            return true;
        } catch (IOException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            if (writer != null)  {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
                }
            }
        }
        
        return false;
    }
}