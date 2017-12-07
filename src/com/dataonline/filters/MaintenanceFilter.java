package com.fota.filters;

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

import org.apache.log4j.Logger;

@WebFilter(
    description = "maintenance目录登录过滤器",
    filterName = "maintenanceLoginFilter",
    urlPatterns = { "/maintenance/*" }
)

public class MaintenanceFilter implements Filter {
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
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
