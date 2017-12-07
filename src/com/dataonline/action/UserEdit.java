package com.fota.action.business;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.util.JSONStringer;
import org.apache.log4j.Logger;
import com.fota.factory.business.MaintenanceFactory;
import com.fota.impl.base.UserTypeOpt;
import com.fota.intfc.base.UserOpt;
import com.fota.pojo.base.User;
import com.fota.util.common.DataTimeCvt;
import com.fota.util.common.GetIP;
import com.fota.util.common.LineNo;
import com.fota.util.common.MD5;
import com.fota.util.error.ErrorCode;
import com.fota.util.error.GetLastError;

@WebServlet(
    urlPatterns = { "/manager/UserEdit.html" },
    name = "UserEdit"
)

public class UserEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(VersionEdit.class);
    String rootpath = new String(); 
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (String)request.getParameter("actionUser");
        response.setContentType("text/html;charset=utf-8");
        rootpath = request.getSession().getServletContext().getRealPath("/"); 
        
        //新建、修改、重置密码、删除
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
            user.setOEM((String)request.getParameter("userManufacturer"));
            user.setToken(createUserToken(userName));
            user.setType(UserTypeOpt.COMMON.get());
            
            int opID = Integer.parseInt((String)request.getSession().getAttribute("userid"));
            user.setOpID(opID);
            user.setIP(GetIP.getIpAddress(request));
            
            user.setOpt(UserOpt.O_NAME.get() 
                    | UserOpt.O_PASSWORD.get()
                    | UserOpt.O_OEM.get()
                    | UserOpt.O_TOKEN.get()
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
            user.setOEM((String)request.getParameter("userManufacturer"));
            user.setIP(GetIP.getIpAddress(request));
            
            user.setOpt(UserOpt.O_OEM.get() | UserOpt.O_ID.get());
          
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
            user.setIP(GetIP.getIpAddress(request));
            
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
    
    private String createUserToken(String userName) {
        String srcToken = userName + DataTimeCvt.getCurrentDate() + String.valueOf(Math.random());
        MD5 md = new MD5();
        
        return md.getMD5ofStr(srcToken);
    }
 
    private boolean delUser(HttpServletRequest request) {
        try {
            int userID = Integer.parseInt((String)request.getParameter("userId"));
            String userName = (String)request.getParameter("userName");
            
            if (null == userName || userName.isEmpty()) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户账户获取错误");
                return false;
            }
            
            // 删除用户账户
            int opID = Integer.parseInt((String)request.getSession().getAttribute("userid"));
            
            if (false == delUserInfo(userID, userName, opID, GetIP.getIpAddress(request))) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户账户删除失败"); 
            }
            
            User user = new User();
            user.setID(userID);
            user.setIP(GetIP.getIpAddress(request));
            
            // 删除用户名下所有版本
            if (ErrorCode.E_OK != MaintenanceFactory.getInstance().getMaintenance().versionRemove(user)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户版本删除失败");  
                return false;
            }
            
            // 删除用户名下所有项目
            if (ErrorCode.E_OK != MaintenanceFactory.getInstance().getMaintenance().projectRemove(user)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户项目删除失败"); 
                return false;
            }
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "用户账户ID获取错误");
            return false;
        }
        
        return true;
    }
    
    public boolean delUserInfo(int userID, String userName, int opID, String ip) {
        User user = new User();
        
        try {
            user.setID(userID);
            user.setOpt(UserOpt.O_ID.get());
            
            user.setOpID(opID);
            user.setIP(ip);
     
            if (ErrorCode.E_OK != MaintenanceFactory.getInstance().getMaintenance().userRemove(user)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - (" + userName + ")用户信息删除失败");
                return false;
            }
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }
    
    private String getFormatResult(String result, String tipMsg) {
        JSONStringer stringer = new JSONStringer();
        
        stringer.object().key("result").value(result).key("tipMsg").value(tipMsg).endObject();
        
        return stringer.toString();
    }
}
