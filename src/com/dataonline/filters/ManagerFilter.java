package com.dataonline.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONStringer;

import org.apache.log4j.Logger;

import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;

@WebFilter(
    description = "manager目录登录过滤器",
    filterName = "managerLoginFilter",
    urlPatterns = { "/manager/*" }
)

public class ManagerFilter implements Filter {
    private static Logger log = Logger.getLogger(ManagerFilter.class);
    private String filterName = new String();

    public void init(FilterConfig filterConfig) throws ServletException {
        filterName = filterConfig.getFilterName();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        String userName = (String)req.getSession().getAttribute("username");
        
        if (null == userName && !(req.getServletPath().indexOf("login.jsp") > 0)) {
            log.debug("请求被" + filterName + "过滤");
            if (req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
                response.getWriter().println(getFormatResult("logout", GetLastError.instance().getErrorMsg(ErrorCode.E_LOGOUT)));
            }else{
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
    
    private String getFormatResult(String result, String tipMsg) {
        JSONStringer stringer = new JSONStringer();
        
        stringer.object().key("result").value(result).key("tipMsg").value(tipMsg).endObject();
        
        return stringer.toString();
    }
}
