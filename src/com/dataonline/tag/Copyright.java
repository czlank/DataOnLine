package com.dataonline.tag.index;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class Copyright extends TagSupport {
    private static final long serialVersionUID = 1L;
    
    @Override
    public int doStartTag() throws JspException {
        com.dataonline.config.Copyright xml = new com.dataonline.config.Copyright("config.xml");
        
        String copyright = xml.getCopyright();
        String version = xml.getVersion();
        String content =  "<span>" 
                + copyright
                +" | " 
                + version
                + "</span>";
        
        try {
            pageContext.getOut().println(content);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        return EVAL_PAGE;
    }
}
