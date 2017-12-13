package com.dataonline.tag.type;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.dataonline.pojo.Type;

public class TypeTable extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private String index = new String();
	private String itemName = new String();
	private Collection<Type> items = null;
	private Iterator<Type> it;
	private int count = 0;
	
	@Override
	public int doStartTag() throws JspException {
		if (null == items || 0 == items.size()) {
		    return SKIP_BODY;
		}
		
		it = items.iterator();
		count = 0;
		
		if (it.hasNext()) {
			pageContext.setAttribute(index, count++);
		    pageContext.setAttribute(itemName, it.next());
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doAfterBody() throws JspException {
	    if (it.hasNext()) {
	    	pageContext.setAttribute(index, count++);
	        pageContext.setAttribute(itemName, it.next());
	        return EVAL_BODY_AGAIN;
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
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItems(Collection<Type> items) {
		this.items = items;
	}
	
	public Collection<Type> getItems() {
		return items;
	}
}
