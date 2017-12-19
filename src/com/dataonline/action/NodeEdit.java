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
import com.dataonline.intfc.NodeOpt;
import com.dataonline.pojo.Node;
import com.dataonline.util.LineNo;
import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;

@WebServlet(
    urlPatterns = { "/manager/NodeEdit.html" },
    name = "NodeEdit"
)

public class NodeEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(NodeEdit.class);
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (String)request.getParameter("actionNode");
        int userId = Integer.parseInt(request.getParameter("userid"));
        response.setContentType("text/html;charset=utf-8");
        
        // 新建、修改、删除
        if (action != null && action.equalsIgnoreCase("addNode")) {
        	Node node = new Node();
        	
        	node.setValue(Integer.parseInt(request.getParameter("nodeValue")));
        	node.setOpt(NodeOpt.O_VALUE.get());
        	
        	// 查询是否有重复的节点ID
        	Vector<Node> vecNode = MaintenanceFactory.getInstance().getMaintenance().nodeQuery(userId, node);
        	if (vecNode != null && vecNode.size() != 0) {
        		response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_DUPLICATE_VALUE)));
        		return;
        	}
        	
        	vecNode = null;
        	node.clearOpt(NodeOpt.O_VALUE.get());
        	
        	node.setName((String)request.getParameter("nodeName"));
        	node.setOpt(NodeOpt.O_NAME.get());
        	
        	// 查询是否有重复的节点名称
        	vecNode = MaintenanceFactory.getInstance().getMaintenance().nodeQuery(userId, node);
        	if (vecNode != null && vecNode.size() != 0) {
        		response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_DUPLICATE_NAME)));
        		return;
        	}
        	
            if (false == addNode(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_ADD)));                
                return;
            }
        } else if (action != null && action.equalsIgnoreCase("editNode")) {
        	Node node = new Node();
        	
        	node.setName((String)request.getParameter("nodeName"));
        	node.setOpt(NodeOpt.O_NAME.get());
        	
        	Vector<Node> vecNode = MaintenanceFactory.getInstance().getMaintenance().nodeQuery(userId, node);
        	if (null == vecNode || 0 == vecNode.size()) {
        		response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_QUERY)));
        		return;
        	}
        	
            if (false == editNode(vecNode.get(0).getID(), request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_EDIT)));                
                return;
            }
        } else if (action != null && action.equalsIgnoreCase("deleteNode")) {
            if (false == deleteNode(request)) {
                response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_DELETE)));
                return;
            }
        } else {
            response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_PARA)));
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_NODE_PARA)); 
            return;
        }
        
        response.getWriter().println(getFormatResult("ok", GetLastError.instance().getErrorMsg(ErrorCode.E_OK)));
    }
    
    private boolean addNode(HttpServletRequest request) {
        Node node = new Node();
        
        try {
        	int userId = Integer.parseInt(request.getParameter("userid"));
        	int nodeValue = Integer.parseInt(request.getParameter("nodeValue"));
        	String nodeName = (String)request.getParameter("nodeName");
        	
        	node.setValue(nodeValue);
        	node.setName(nodeName);
          
            if (ErrorCode.E_OK == MaintenanceFactory.getInstance().getMaintenance().nodeAdd(userId, node)) {
                return true;
            }
            
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "新增节点信息失败"); 
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }
    
    private boolean editNode(int nodeId, HttpServletRequest request) {
    	Node node = new Node();
        
        try {
        	int userId = Integer.parseInt(request.getParameter("userid"));
        	String nodeName = (String)request.getParameter("nodeName");
        	
        	node.setName(nodeName);
        	node.setOpt(NodeOpt.O_NAME.get());
          
            if (ErrorCode.E_OK == MaintenanceFactory.getInstance().getMaintenance().nodeModify(userId, node)) {
                return true;
            }
            
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "修改节点信息失败"); 
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        return false;
    }

    private boolean deleteNode(HttpServletRequest request) {
        try {
        	int userId = Integer.parseInt(request.getParameter("userid"));
        	int nodeId = Integer.parseInt(request.getParameter("nodeId"));
        	String nodeName = (String)request.getParameter("nodeName");
            
            Node node = new Node();
            
            node.setID(nodeId);
            node.setOpt(NodeOpt.O_VALUE.get());
            
            if (ErrorCode.E_OK != MaintenanceFactory.getInstance().getMaintenance().nodeRemove(userId, node)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - (" + nodeName + ")节点信息删除失败");
                return false;
            }
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "节点值获取错误");
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
