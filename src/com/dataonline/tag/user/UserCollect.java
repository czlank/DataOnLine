package com.dataonline.tag.user;

import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.util.JSONStringer;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.intfc.UserOpt;
import com.dataonline.pojo.User;
import com.dataonline.util.LineNo;

public class UserCollect extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Vector<User> vecUser = new Vector<User>();
	private Logger log = Logger.getLogger(UserCollect.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
            User user = new User();
          
            user.setOpt(UserOpt.O_ALL.get());
            vecUser = MaintenanceFactory.getInstance().getMaintenance().userQuery(user);
            
            pageContext.setAttribute("users", vecUser);
            
            JSONStringer stringer = new JSONStringer();
            stringer.array();
            if (vecUser != null) {
                for (int i = 0; i < vecUser.size(); i++) {
                    stringer.object().key("id").value(vecUser.get(i).getID())
                        .key("userName").value(jsonEncode(vecUser.get(i).getName()))
                        .key("password").value(jsonEncode(vecUser.get(i).getPassword()))
                        .key("type").value(vecUser.get(i).getType())
                        .key("nodes").value(jsonEncode(vecUser.get(i).getNodes()))
                        .endObject();
                }
            }
            
            stringer.endArray();
       
            // 给JavaScript使用
            pageContext.getRequest().setAttribute("jsonUser", stringer.toString());
            
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + e.getMessage());
        } catch (JSONException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + "用户表为空"); 
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
    
    private String jsonEncode(String str) {
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").replaceAll("\"", "&quot;");
    }
}
