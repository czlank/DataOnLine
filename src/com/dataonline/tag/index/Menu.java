package com.dataonline.tag.index;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class Menu extends TagSupport {
    private static final long serialVersionUID = 1L;
    
    @Override
    public int doStartTag() throws JspException {
        String userName = (String)pageContext.getSession().getAttribute("username");
        boolean bAdmin = userName != null && userName.equals("admin");
        
        String content = getDefaultMenu();
        
        if (bAdmin) {
            // 管理员增加了额外的管理功能
            content += getUserMenu() + getTypeMenu() + getSystemMenu();
        }
        
        try {
            pageContext.getOut().println(content);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        return EVAL_PAGE;
    }
    
    private String getDefaultMenu() {
        String content = "<li>"
                 + "<a href=\"Statistics.html\"><i class=\"fa fa-bar-chart fa-3x\"></i>统计信息</a>"
                 + "</li>";
        
        return content;
    }
    
    private String getUserMenu() {
        String content = "<li>"
                + "<a href=\"User.html\"><i class=\"fa fa-user fa-3x\"></i>账户管理</a>"
                + "</li>";
        
        return content;
    }
    
    private String getTypeMenu() {
        String content = "<li>"
                + "<a href=\"Type.html\"><i class=\"fa fa-tumblr fa-3x\"></i>类型管理</a>"
                + "</li>";
        
        return content;
    }
    
    private String getSystemMenu() {
        String content = "<li>"
                + "<a href=\"Database.html\"><i class=\"fa fa-cog fa-3x\"></i>服务器管理</a>"
                + "</li>";
        
        return content;
    }
}
