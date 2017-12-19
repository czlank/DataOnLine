package com.dataonline.tag.node;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class NodeHeader extends TagSupport {
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
	    try {
	        String content = "<th style=\"min-width: 80px;\">节点名称</th>"
                    + "<th>节点ID</th>"
                    + "<th style=\"min-width: 80px;width: 80px;\">操作</th>";
	        
            pageContext.getOut().println("<tr role='row'>" + content + "</tr>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
	    
		return EVAL_PAGE;
	}
}
