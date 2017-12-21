package com.dataonline.tag.node;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.dataonline.pojo.Node;

public class NodeShow extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private Node node = null;
	private String index = new String();
	
	@Override
	public int doStartTag() throws JspException {
	    if (null == node) {
	        return SKIP_BODY;
	    }
	    
	    String content = "<td>" + jsonEncode(node.getName()) + "</td>"
	                   + "<td>" + node.getValue() + "</td>"
	                   + "<td nowrap style='text-align: center;'>"
	                   + "<a href=\"#\" data-toggle=\"modal\" data-target=\"#editNode\" onclick=\"showNode('nodeEdit', " + index + ")\">" + "修改</a>"
	                   + "&nbsp;<a href=\"#\" onclick=\"deleteNode(" + node.getValue() + ", '" + jsonEncode(node.getName()) + "'" + ")\">"
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
	
	public void setNode(Node node) {
	    this.node = node;
	}
	
	public Node getNode() {
	    return node;
	}

    private String jsonEncode(String str) {
    	if (null == str) {
    		return "";
    	}
    	
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").replaceAll("\"", "&quot;");
    }
}
