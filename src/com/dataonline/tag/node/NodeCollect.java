package com.dataonline.tag.node;

import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.util.JSONStringer;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.intfc.NodeOpt;
import com.dataonline.pojo.Node;
import com.dataonline.util.LineNo;

public class NodeCollect extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(NodeCollect.class);
	
	private Vector<Node> vecNode = new Vector<Node>();
	
	@Override
	public int doStartTag() throws JspException {
		try {
			vecNode = getNodes();
			
            // 给自定义标签使用
            pageContext.setAttribute("nodes", vecNode);

            JSONStringer stringer = new JSONStringer();
            stringer.array();
            
            if (vecNode != null) {
                for (int i = 0; i < vecNode.size(); i++) {
                    Node currNode = vecNode.get(i);
                    
                    stringer.object().key("id").value(currNode.getID())
                    				 .key("value").value(currNode.getValue())
                                     .key("name").value(jsonEncode(currNode.getName()))
                                     .endObject();
                }
            }

            stringer.endArray();
            
            // 给JavaScript使用
            pageContext.getRequest().setAttribute("jsonNode", stringer.toString());
            pageContext.getRequest().setAttribute("userid4editnode", pageContext.getRequest().getParameter("userid4editnode"));
        } catch (JSONException e) {
        	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + "获取节点表时发生异常"); 
        }

		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		super.release();
	}

    private Vector<Node> getNodes() {
    	int userId = Integer.parseInt(pageContext.getRequest().getParameter("userid4editnode"));
    	
        Node node = new Node();
        node.setOpt(NodeOpt.O_ALL.get());
        
        Vector<Node> vecNode = MaintenanceFactory.getInstance().getMaintenance().nodeQuery(userId, node);
       
        return vecNode;
    }
    
    private String jsonEncode(String str) {
    	if (null == str) {
    		return "";
    	}
    	
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").replaceAll("\"", "&quot;");
    }
}
