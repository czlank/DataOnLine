package com.dataonline.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dataonline.config.Database;
import com.dataonline.factory.BaseFactory;
import com.dataonline.intfc.IUser;
import com.dataonline.intfc.UserOpt;
import com.dataonline.pojo.User;
import com.dataonline.util.LineNo;

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
                request.getSession().setAttribute("userid", String.valueOf(user.getID()));
                path = "index.jsp";
            }
        } else if (action.equals("logout")) {
            request.getSession().removeAttribute("username");
            request.getSession().removeAttribute("userid");
            path = "login.jsp";
        }
        
        if (path != "login.jsp") {
        	response.sendRedirect(path);
        } else {
            request.getRequestDispatcher(path).forward(request, response);
        }
    }
    
    private User getUser(String userName) {
        IUser userService = BaseFactory.getInstance().getUser();
        User userRet = null;
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取User接口失败");
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
