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
import com.dataonline.intfc.TypeOpt;
import com.dataonline.pojo.Type;
import com.dataonline.util.LineNo;
import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;

@WebServlet(
    urlPatterns = { "/manager/TypeEdit.html" },
    name = "TypeEdit"
)

public class TypeEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(TypeEdit.class);
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (String)request.getParameter("actionType");
        response.setContentType("text/html;charset=utf-8");
        
        // 新建、修改、删除
        if (action != null && action.equalsIgnoreCase("addType")) {
        	Type type = new Type();
        	
        	type.setName((String)request.getParameter("typeName"));
        	type.setOpt(TypeOpt.O_NAME.get());
        	
        	Vector<Type> vecType = MaintenanceFactory.getInstance().getMaintenance().typeQuery(type);
        	if (vecType != null && vecType.size() != 0) {
        		response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_TYPE_DUPLICATE)));
        		return;
        	}
        	
            if (false == addType(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_TYPE_ADD)));                
                return;
            }
        } else if (action != null && action.equalsIgnoreCase("editType")) {
        	Type type = new Type();
        	
        	type.setName((String)request.getParameter("typeName"));
        	type.setOpt(TypeOpt.O_NAME.get());
        	
        	Vector<Type> vecType = MaintenanceFactory.getInstance().getMaintenance().typeQuery(type);
        	if (null == vecType || 0 == vecType.size()) {
        		response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_TYPE_DUPLICATE)));
        		return;
        	}
        	
            if (false == editType(vecType.get(0).getID(), request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_TYPE_EDIT)));                
                return;
            }
        } else if (action != null && action.equalsIgnoreCase("deleteType")) {
            if (false == deleteType(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_TYPE_DELETE)));
                return;
            }
        } else {
            response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_TYPE_PARA)));
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_TYPE_PARA)); 
            return;
        }
        
        response.getWriter().println(getFormatResult("ok", GetLastError.instance().getErrorMsg(ErrorCode.E_OK)));
    }
    
    private boolean addType(HttpServletRequest request) {
        Type type = new Type();
        
        try {
        	String typeName = (String)request.getParameter("typeName");
        	int typeValue = Integer.parseInt(request.getParameter("typeValue"));
        	double typeMin = Double.parseDouble(request.getParameter("typeMin"));
        	double typeMax = Double.parseDouble(request.getParameter("typeMax"));
        	
        	type.setName(typeName);
            type.setType(typeValue);
            type.setMin(typeMin);
            type.setMax(typeMax);
          
            if (ErrorCode.E_OK == MaintenanceFactory.getInstance().getMaintenance().typeAdd(type)) {
                return true;
            }
            
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "新增类型信息失败"); 
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }
    
    private boolean editType(int typeId, HttpServletRequest request) {
    	Type type = new Type();
        
        try {
        	double typeMin = Double.parseDouble(request.getParameter("typeMin"));
        	double typeMax = Double.parseDouble(request.getParameter("typeMax"));
        	
        	type.setID(typeId);
            type.setMin(typeMin);
            type.setMax(typeMax);
            type.setOpt(TypeOpt.O_MIN.get() | TypeOpt.O_MAX.get());
          
            if (ErrorCode.E_OK == MaintenanceFactory.getInstance().getMaintenance().typeModify(type)) {
                return true;
            }
            
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "修改类型信息失败"); 
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }

    private boolean deleteType(HttpServletRequest request) {
        try {
        	int typeId = Integer.parseInt(request.getParameter("typeId"));
        	String typeName = (String)request.getParameter("typeName");
            
            Type type = new Type();
            
            type.setID(typeId);
            type.setOpt(TypeOpt.O_TYPE.get());
            
            if (ErrorCode.E_OK != MaintenanceFactory.getInstance().getMaintenance().typeRemove(type)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - (" + typeName + ")类型信息删除失败");
                return false;
            }
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "类型值获取错误");
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
