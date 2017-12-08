package com.dataonline.filters;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

import com.dataonline.util.LineNo;

public class RequestEncodingWrapper extends HttpServletRequestWrapper{
    private Logger log = Logger.getLogger(RequestEncodingWrapper.class);
    private String encoding = new String();

    public RequestEncodingWrapper(HttpServletRequest request) {
        super(request);
    }

    public RequestEncodingWrapper(HttpServletRequest request, String encoding) {
        super(request);
        this.encoding = encoding;
    }

    @Override
    public String getParameter(String name){
        String value = getRequest().getParameter(name);

        try {
            // 将参数值进行编码转换
            if (value != null && !"".equals(value)) {
                value = new String(value.trim().getBytes("ISO-8859-1"), encoding);
            }
        } catch (UnsupportedEncodingException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }

        return value;
    }
}
