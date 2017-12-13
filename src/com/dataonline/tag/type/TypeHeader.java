package com.dataonline.tag.type;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class TypeHeader extends TagSupport {
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
	    try {
	        String content = "<th style=\"min-width: 80px;\">类型名称</th>"
                    + "<th>类型值</th>"
                    + "<th>最小值</th>"
                    + "<th>最大值</th>"
                    + "<th style=\"min-width: 80px;width: 80px;\">操作</th>";
	        
            pageContext.getOut().println("<tr role='row'>" + content + "</tr>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
	    
		return EVAL_PAGE;
	}
}
