package com.dataonline.tag.index;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class MainFrame extends TagSupport {
    private static final long serialVersionUID = 1L;
    
    @Override
    public int doStartTag() throws JspException {
        String content = "<iframe src=\"manager/Statistics.jsp\" frameborder=\"0\" scrolling=\"no\" id=\"pageContent\" onload=\"setIframeHeight(this)\" style=\"width:100%\"></iframe>";
        
        try {
            pageContext.getOut().println(content);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        return EVAL_PAGE;
    }
}
