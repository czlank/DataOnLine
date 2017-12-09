package com.dataonline.tag.user;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class UserHeader extends TagSupport {
    private static final long serialVersionUID = 1L;
    
    @Override
    public int doStartTag() throws JspException {
        String content = "<th style=\"min-width: 60px;\">账户名 </th>"
                + "<th>密码</th>"
                + "<th style=\"min-width: 60px;\">类型</th>"
                + "<th>节点</th>"
                + "<th style=\"width: 150px;\">操作</th>";

        try {
            pageContext.getOut().println("<tr>" + content + "</tr>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        return EVAL_PAGE;
    }

}
