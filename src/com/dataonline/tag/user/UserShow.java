package com.dataonline.tag.user;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.dataonline.impl.UserTypeOpt;
import com.dataonline.pojo.User;

public class UserShow extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private User user = null;
	private String index = new String();
	
	@Override
	public int doStartTag() throws JspException {
	    if (null == user) {
	        return SKIP_BODY;
	    }

	    String content = "<td>" + user.getName()  + "</td>"
	                   + "<td>" + user.getPassword()   + "</td>"
	                   + "<td nowrap>" + getUserType(user.getType())  + "</td>"
	                   + "<td nowrap>" + user.getNodes() + "</td>"
	                   + "<td nowrap>"
                       + "<a href=\"#\" data-toggle=\"modal\" data-target=\"#editUser\" onclick=\"showUser('userEdit', " + index + ") \">"  + "修改</a>"
                       + "&nbsp"
                       + getDeleteBref(user.getType())
                       + "&nbsp"
                       + "<a href=\"#\" data-toggle=\"modal\" data-target=\"#editUser\" onclick=\"showUser('userResetPassword'," + index +  ")\">重置密码</a>"
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
	
	public void setUser(User user) {
	    this.user = user;
	}
	
	public User getUser() {
	    return user;
	}
	
	private String getUserType(int userType) {
	    return ((UserTypeOpt.ADMINISTRATOR.get() == userType) ? "管理员" : "普通");
	}
	   
    private String getDeleteBref(int userType) {
        String bref = new String();
        
        if (UserTypeOpt.ADMINISTRATOR.get() == userType) {
            bref = "<a href=\"#\" style=\"text-decoration:none\"><font color=#666>删除</font></a>";
        } else {
            bref = "<a href=\"#\" onclick=\"deleteUser(" + String.valueOf(user.getID()) + ",'" + jsonEncode(user.getName()) + "')\">删除</a>";
        }
        
        return bref;
    }
    
    private String jsonEncode(String str) {
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").replaceAll("\"", "&quot;");
    }
}
