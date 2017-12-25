package com.dataonline.action;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONStringer;

import org.apache.log4j.Logger;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.intfc.UserOpt;
import com.dataonline.intfc.ValueOpt;
import com.dataonline.pojo.User;
import com.dataonline.pojo.Value;
import com.dataonline.util.LineNo;
import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;

@WebServlet(
    urlPatterns = { "/manager/ValueQuery.html" },
    name = "ValueQuery"
)

public class ValueQuery extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(ValueQuery.class);
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        
        String action = (String)request.getParameter("actionValue");
        
        if (action != null && action.equalsIgnoreCase("summary")) {
        	querySummary(request, response);
        	return;
        }
        
        response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_PARA)));
        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_PARA)); 
    }
	
	private void querySummary(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = new User();
        user.setName((String)request.getParameter("UserName"));
        user.setOpt(UserOpt.O_NAME.get());
        
        Vector<User> vecUser = MaintenanceFactory.getInstance().getMaintenance().userQuery(user);
        if (null == vecUser || 1 != vecUser.size()) {
        	response.getWriter().println(getFormatResult("default", ""));
        	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_USER_QUERY));
        	return;
        }
        
        Value value = new Value();
        value.setOpt(ValueOpt.O_LASTREC.get());
        Vector<Value> vecValue = MaintenanceFactory.getInstance().getMaintenance().valueQuery(vecUser.get(0).getID(), value);
        if (null == vecValue || 1 != vecValue.size()) {
        	response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_VALUE_QUERY)));
        	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_VALUE_QUERY));
        	return;
        }
        
        response.getWriter().println(getFormatResult("ok", "" + vecValue.get(0).getValue()));
	}
    
    private String getFormatResult(String result, String tipMsg) {
        JSONStringer stringer = new JSONStringer();
        
        stringer.object().key("result").value(result).key("tipMsg").value(tipMsg).endObject();
        
        return stringer.toString();
    }
}
