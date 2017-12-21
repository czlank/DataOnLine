package com.dataonline.tag.statistics;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

public class StatisticsCollect extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(StatisticsCollect.class);
	
	@Override
	public int doStartTag() throws JspException {
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
}
