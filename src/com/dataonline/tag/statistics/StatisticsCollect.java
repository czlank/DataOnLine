package com.dataonline.tag.statistics;

import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.intfc.ValueOpt;
import com.dataonline.intfc.IValue;
import com.dataonline.pojo.Value;
import com.dataonline.util.LineNo;

public class StatisticsCollect extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(StatisticsCollect.class);
	
	@Override
	public int doStartTag() throws JspException {
		Value value = new Value();
		value.setOpt(ValueOpt.O_LASTREC.get());
		
		try {
			int userID = Integer.parseInt((String)pageContext.getRequest().getAttribute("userid4queryvalue"));
			Vector<Value> vecValue = MaintenanceFactory.getInstance().getMaintenance().valueQuery(userID, value);
			
			if (null == vecValue || 1 != vecValue.size()) {
				log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + "未找到数据");
				
				return SKIP_BODY;
			}
			
			
		} catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + e.getMessage());
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
}
