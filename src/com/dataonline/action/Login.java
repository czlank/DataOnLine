package com.fota.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fota.config.Database;
import com.fota.factory.base.BaseFactory;
import com.fota.intfc.base.IUser;
import com.fota.intfc.base.UserOpt;
import com.fota.pojo.base.User;
import com.fota.util.common.LineNo;

@WebServlet(
    urlPatterns = { "/login.html" },
    name = "Login"
)

public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(Login.class);
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String action = request.getParameter("action");
        String path = new String("login.jsp");
        //request.getSession().setMaxInactiveInterval(5);

        if (action.equals("login")) {
            User user = getUser(userName);
            
            if (null == user || !user.getPassword().equals(password)) {
                Database xml = new Database("config.xml");
                
                if (xml.getFlag()) {
                    request.getSession().removeAttribute("username");
                    request.getSession().removeAttribute("oem");
                    request.getSession().removeAttribute("userid");
                    request.setAttribute("error", "请输入正确的用户名和密码！");
                    path = "login.jsp";
                } else if ("admin".equals(userName)) {
                    request.getSession().setAttribute("username", "admin");
                    path = "index.jsp";
                }
            } else if (user.getPassword().equals(password)) {
                request.getSession().setAttribute("username", user.getName());
                request.getSession().setAttribute("oem", user.getOEM());
                request.getSession().setAttribute("userid", String.valueOf(user.getID()));
                path = "index.jsp";
            }
        } else if (action.equals("logout")) {
            request.getSession().removeAttribute("username");
            request.getSession().removeAttribute("userid");
            path = "login.jsp";
        }
        
        if (path != "login.jsp") {
        	/** 
             * 客户端跳转：效率低 
             * session范围属性，url中的参数会传递下去，request范围属性不传递 
             */
        	response.sendRedirect(path);
        } else {
        	/** 
             * 服务器端跳转：常用，效率高 
             * request范围属性，session范围属性，url中的参数会传递 ，浏览器地址栏中不会出现转向后的地址，无法重定向至有frame的jsp文件，可以重定向至有frame的html文件
             */ 
            request.getRequestDispatcher(path).forward(request, response);
        }
    }
    
    private User getUser(String userName) {
        IUser userService = BaseFactory.getInstance().getUser();
        User userRet = null;
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IUser接口失败");
        	return userRet;
        }
        
        try {
            User user = new User();
            user.setName(userName);
            user.setOpt(UserOpt.O_NAME.get());
            
            Vector<User> vecUser = userService.query(user);
            
            if (null == vecUser || vecUser.size() != 1) {
                return userRet;
            }
            
            userRet = vecUser.get(0);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "查询用户失败");
        } finally {
            try {
                userService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return userRet;
    }
}
