package com.fota.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(
    description = "字符编码过滤器",
    filterName = "encodingFilter",
    urlPatterns = { "/*" },
    initParams = {
        @WebInitParam(name = "ENCODING", value = "UTF-8")
    }
)

public class EncodeFilter implements Filter {
    private String encoding = new String();

    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("ENCODING");
        
        if (encoding == null || "".equals(encoding)) {
            encoding="UTF-8";
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        
        if ("GET".equals(request.getMethod())) {
            request = new RequestEncodingWrapper(request, encoding);
        } else {
            request.setCharacterEncoding(encoding);
        }

        response.setCharacterEncoding(encoding);
        
        // 传输给过滤器链过滤
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}