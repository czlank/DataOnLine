package com.fota.action.business;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONStringer;
import org.apache.log4j.Logger;

import com.fota.factory.business.DatabaseDeployFactory;
import com.fota.intfc.business.IDatabaseDeploy;
import com.fota.util.common.LineNo;

import com.fota.util.error.ErrorCode;
import com.fota.util.error.GetLastError;
@WebServlet(
    urlPatterns = { "/manager/DatabaseDeploy.html" },
    name = "DatabaseDeploy"
)

public class DatabaseDeploy extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(DatabaseDeploy.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("dbUserName");
        String password = request.getParameter("dbPassword");
        String dbName   = request.getParameter("dbName");

        if (null == userName || "".equals(userName) || null == password || "".equals(password) || null == dbName || "".equals(dbName)) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "输入信息不完整，userName = " + userName + ", password = " + password + ", dbName = " + dbName);
        } else if (!config(userName, password, dbName)) {
            response.getWriter().print(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_DEPLOY_DATABASE_FAIL)));
            return;
        }
        
        response.getWriter().print(getFormatResult("ok", GetLastError.instance().getErrorMsg(ErrorCode.E_OK)));
    }
    
    private boolean config(String userName, String password, String dbName) {
        IDatabaseDeploy deploy = DatabaseDeployFactory.getDatabaseConfig(dbName, userName, password);
        
        if (null == deploy || !deploy.config(userName, password, dbName)) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "配置数据库失败，deploy(" + (null == deploy ? "null" : deploy.toString()) + ")");
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
