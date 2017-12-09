package com.dataonline.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.util.JSONStringer;
import org.apache.log4j.Logger;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.impl.UserTypeOpt;
import com.dataonline.intfc.UserOpt;
import com.dataonline.pojo.User;
import com.dataonline.util.LineNo;
import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;

@WebServlet(
    urlPatterns = { "/manager/UserEdit.html" },
    name = "UserEdit"
)

public class UserEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(UserEdit.class);
    String rootpath = new String(); 
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (String)request.getParameter("actionUser");
        response.setContentType("text/html;charset=utf-8");
        rootpath = request.getSession().getServletContext().getRealPath("/"); 
        
        // 新建、修改、重置密码、删除
        if (action != null && action.equalsIgnoreCase("addUser")) {
            if (false == addUser(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_USER_ADD)));                
                return;
            }
        } else if (action != null && action.equalsIgnoreCase("editUser")) {
            if (false == editUser(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_USER_EDIT)));                
                return;
            }
        } else if (action != null && action.equalsIgnoreCase("resetPassword")) {
            if (false == resetPassword(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_USER_RESET_PASSWORD)));
                return;
            }
        } else if (action != null && action.equalsIgnoreCase("delUser")) {
            if (false == delUser(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_USER_DELETE)));
                return;
            }
        } else {
            response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_USER_PARA)));
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_USER_PARA)); 
            return;
        }
        
        response.getWriter().println(getFormatResult("ok", GetLastError.instance().getErrorMsg(ErrorCode.E_OK)));
    }
    
    private boolean addUser(HttpServletRequest request) {
        User user = new User();
        String userName = (String)request.getParameter("userName");
        try {
            user.setName(userName);
            user.setPassword((String)request.getParameter("userPassword"));
            user.setType(UserTypeOpt.COMMON.get());
            
            user.setOpt(UserOpt.O_NAME.get() 
                    | UserOpt.O_PASSWORD.get()
                    | UserOpt.O_TYPE.get());            
          
            if (ErrorCode.E_OK == MaintenanceFactory.getInstance().getMaintenance().userAdd(user)) {
                return true;
            }
            
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "新增用户信息失败"); 
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }
    
    private boolean editUser(HttpServletRequest request) {
        User user = new User();
        try {
            user.setID(Integer.parseInt((String)request.getParameter("userId")));
            user.setName((String)request.getParameter("userName"));
            user.setNodes((String)request.getParameter("userNodes"));
            
            user.setOpt(UserOpt.O_NAME.get() | UserOpt.O_NODES.get() | UserOpt.O_ID.get());
          
            if (ErrorCode.E_OK == MaintenanceFactory.getInstance().getMaintenance().userModify(user)) {
                return true;
            }
            
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户信息修改失败"); 
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean resetPassword(HttpServletRequest request) {
        User user = new User();
        try {
            user.setID(Integer.parseInt((String)request.getParameter("userId")));
            user.setPassword((String)request.getParameter("userPassword"));
            
            user.setOpt(UserOpt.O_PASSWORD.get() | UserOpt.O_ID.get());
    
            if (ErrorCode.E_OK == MaintenanceFactory.getInstance().getMaintenance().userModify(user)) {
                return true;
            }
            
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户密码修改失败"); 
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }
 
    private boolean delUser(HttpServletRequest request) {
        try {
            int userID = Integer.parseInt((String)request.getParameter("userId"));
            String userName = (String)request.getParameter("userName");
            
            User user = new User();
            
            user.setID(userID);
            user.setOpt(UserOpt.O_ID.get());
            
            if (ErrorCode.E_OK != MaintenanceFactory.getInstance().getMaintenance().userRemove(user)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - (" + userName + ")用户信息删除失败");
                return false;
            }
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户账户ID获取错误");
            return false;
        }
        
        return true;
    }
    
    private String getFormatResult(String result, String tipMsg) {
        JSONStringer stringer = new JSONStringer();
        
        stringer.object().key("result").value(result).key("tipMsg").value(tipMsg).endObject();
        
        return stringer.toString();
    }
}
