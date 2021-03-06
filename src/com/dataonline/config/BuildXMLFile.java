package com.dataonline.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.dataonline.util.LineNo;

public class BuildXMLFile {
    private Logger log = Logger.getLogger(BuildXMLFile.class);
    protected String filePath = new String();
    
    // 移动这个文件的时候，切记修改filePath的内容，使其保证在/<项目名称>/目录下生成config.xml文件
    public BuildXMLFile(String fileName) {
        String resPath = Database.class.getResource("/").getPath();
        String rootPath = resPath.substring(0, resPath.indexOf("WEB-INF"));

        filePath = rootPath + fileName;
        
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        
        Document doc = DocumentHelper.createDocument();

        Element root = doc.addElement("config");
        
        Element database = root.addElement("database");
        database.addComment("DATABASE CONFIGURATION");
        database.addAttribute("flag", "false");
        database.addElement("userName").setText("");
        database.addElement("password").setText("");
        database.addElement("databaseName").setText("");
        
        Element copyRight = root.addElement("copyright");
        copyRight.addElement("info").setText("");
        copyRight.addElement("version").setText("");
        
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setNewlines(true);
            format.setIndent(true);

            XMLWriter writer = new XMLWriter(new FileWriter(filePath),format);
            writer.write(doc);
            writer.close();
        } catch (IOException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
    }
}
