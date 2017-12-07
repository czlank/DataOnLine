package com.dataonline.tag.index;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.dataonline.config.Server;

public class MainFrame extends TagSupport {
    private static final long serialVersionUID = 1L;
    
    @Override
    public int doStartTag() throws JspException {
        Server xml = new Server("config.xml");
        String userName = (String)pageContext.getSession().getAttribute("username");
        
        boolean bServerMaintenance = xml.getStatus();
        boolean bAdmin = userName != null && userName.equals("admin");
        
        String content = "";
        
        if (bServerMaintenance && bAdmin) {
            content = "<iframe src=\"maintenance/DatabaseManager.jsp\" frameborder=\"0\" scrolling=\"no\" id=\"pageContent\" onload=\"setIframeHeight(this)\" style=\"width:100%\"></iframe>";
        } else if (bServerMaintenance) {
            content = "<iframe src=\"maintenance/ServerUnderMaintenance.jsp\" frameborder=\"0\" scrolling=\"no\" id=\"pageContent\" onload=\"setIframeHeight(this)\" style=\"width:100%\"></iframe>";
        } else {
            content = "<iframe src=\"manager/ProjectManager.jsp\" frameborder=\"0\" scrolling=\"no\" id=\"pageContent\" onload=\"setIframeHeight(this)\" style=\"width:100%\"></iframe>";
        }
        
        try {
            pageContext.getOut().println(content);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        return EVAL_PAGE;
    }
}
