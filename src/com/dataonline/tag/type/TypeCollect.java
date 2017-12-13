package com.dataonline.tag.type;

import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.util.JSONStringer;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.intfc.TypeOpt;
import com.dataonline.pojo.Type;
import com.dataonline.util.LineNo;

public class TypeCollect extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(TypeCollect.class);
	
	private Vector<Type> vecType = new Vector<Type>();
	
	@Override
	public int doStartTag() throws JspException {
		try {
			vecType = getTypes();
			
            // 给自定义标签使用
            pageContext.setAttribute("types", vecType);

            JSONStringer stringer = new JSONStringer();
            stringer.array();
            
            if (vecType != null) {
                for (int i = 0; i < vecType.size(); i++) {
                    Type currType = vecType.get(i);
                    
                    stringer.object().key("id").value(currType.getID())
                                     .key("name").value(jsonEncode(currType.getName()))
                                     .key("type").value(currType.getType())
                                     .key("min").value(currType.getMin())
                                     .key("max").value(currType.getMax())
                                     .endObject();
                }
            }

            stringer.endArray();
            
            // 给JavaScript使用
            pageContext.getRequest().setAttribute("jsonType", stringer.toString());
        } catch (JSONException e) {
        	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + "获取类型表时发生异常"); 
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
	   
    private Vector<Type> getTypes() {
        Type type = new Type();
        type.setOpt(TypeOpt.O_ALL.get());
        
        Vector<Type> vecType = MaintenanceFactory.getInstance().getMaintenance().typeQuery(type);
       
        return vecType;
    }
    
    private String jsonEncode(String str) {
    	if (null == str) {
    		return "";
    	}
    	
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").replaceAll("\"", "&quot;");
    }
}
