package com.dataonline.tag.type;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.dataonline.pojo.Type;

public class TypeShow extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private Type type = null;
	private String index = new String();
	
	@Override
	public int doStartTag() throws JspException {
	    if (null == type) {
	        return SKIP_BODY;
	    }
	    
	    String content = "<td>" + jsonEncode(type.getName()) + "</td>"
	                   + "<td>" + type.getType() + "</td>"
	                   + "<td>" + type.getMin() + "</td>"
	                   + "<td>" + type.getMax() + "</td>"
	                   + "<td  nowrap style='text-align: center;'>"
	                   + "<a href=\"#\" data-toggle=\"modal\" data-target=\"#editType\" onclick=\"showType('typeEdit', " + index + ")\">" + "修改</a>"
	                   + "&nbsp;<a href=\"#\" onclick=\"deleteType(" + type.getID() + ", '" + jsonEncode(type.getName()) + "'" + ")\">"
	                   + "删除</a>"
	                   + "</td>";
	    
	    try {
            pageContext.getOut().println("<tr>" + content + "</tr>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
	    
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		super.release();
	}
	
	public void setIndex(String index) {
		this.index = index;
	}
	
	public String getIndex() {
		return index;
	}
	
	public void setType(Type type) {
	    this.type = type;
	}
	
	public Type getType() {
	    return type;
	}

    private String jsonEncode(String str) {
    	if (null == str) {
    		return "";
    	}
    	
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").replaceAll("\"", "&quot;");
    }
}
