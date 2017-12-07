package com.fota.tag.index;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.fota.config.Server;

public class Menu extends TagSupport {
    private static final long serialVersionUID = 1L;
    
    @Override
    public int doStartTag() throws JspException {
        Server xml = new Server("config.xml");
        String userName = (String)pageContext.getSession().getAttribute("username");
        
        boolean bServerMaintenance = xml.getStatus();
        boolean bAdmin = userName != null && userName.equals("admin");
        
        String content = "";
        
        if (bServerMaintenance && bAdmin) {
            // 维护状态 - 管理员
            content += getSystemMenu();
        } else {
            if (!bServerMaintenance) {
                // 使用状态
                content += getManagerMenu() + getStatisticMenu() + getRecordMenu();
            }
            
            if (bAdmin) {
                // 管理员增加了额外的管理功能
                content += getUserMenu() + getSystemMenu();
            }
        }
        
        try {
            pageContext.getOut().println(content);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        return EVAL_PAGE;
    }
    
    private String getManagerMenu() {
        String content = "<li>"
                + "<!-- 这里使用了Fontawesome库作为图标 -->"
                + "<a href=\"Project.html\"><i class=\"fa fa-list-alt fa-3x\"></i>版本控制<span class=\"fa arrow\" style=\"padding-top: 22px;\"></span></a>"
                + "<ul class=\"nav nav-second-level\">"
                + "<li><a href=\"Project.html\" class=\"active-menu\">项目管理</a></li>"
                + "<li><a href=\"Version.html\">版本管理</a></li>"
                + "</ul>"
                + "</li>";
        
        return content;
    }
    
    private String getUserMenu() {
        String content = "<li>"
                + "<a href=\"User.html\" ><i class=\"fa fa-user fa-3x\"></i>账户管理</a>"
                + "</li>";
        
        return content;
    }
    
    private String getSystemMenu() {
        String content = "<li>"
                + "<a href=\"Database.html\"><i class=\"fa fa-cog fa-3x\"></i>系统管理<span class=\"fa arrow\" style=\"padding-top: 22px;\"></span></a>"
                + "<ul class=\"nav nav-second-level\">"
                + "<li><a href=\"Database.html\">数据库管理</a></li>"
                + "<li><a href=\"Server.html\">服务器管理</a></li>"
                + "</ul>"
                + "</li>";
        
        return content;
    }
    
    private String getStatisticMenu() {
        String content = "<li>"
                 + "<a href=\"Statistics.html\" ><i class=\"fa fa-bar-chart fa-3x\"></i>统计信息</a>"
                 + "</li>";
        
        return content;
    }
    
    private String getRecordMenu() {
        String content = "<li>"
                 + "<a href=\"Record.html\" ><i class=\"fa fa-video-camera fa-3x\"></i>日志记录</a>"
                 + "</li>";
        
        return content;
    }
}
