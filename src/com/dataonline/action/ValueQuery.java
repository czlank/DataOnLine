package com.dataonline.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONStringer;

import org.apache.log4j.Logger;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.intfc.NodeOpt;
import com.dataonline.intfc.TypeOpt;
import com.dataonline.intfc.UserOpt;
import com.dataonline.intfc.ValueOpt;
import com.dataonline.pojo.Node;
import com.dataonline.pojo.Type;
import com.dataonline.pojo.User;
import com.dataonline.pojo.Value;
import com.dataonline.util.LineNo;
import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;

@WebServlet(
    urlPatterns = { "/manager/ValueQuery.html" },
    name = "ValueQuery"
)

public class ValueQuery extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(ValueQuery.class);
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        
        String action = (String)request.getParameter("actionValue");
        
        if (action != null && action.equalsIgnoreCase("summary")) {
        	querySummary(request, response);
        	return;
        } else if (action != null && action.equalsIgnoreCase("detail")) {
        	queryDetail(request, response);
        	return;
        }
        
        response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_PARA)));
        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_PARA)); 
    }
	
	private void querySummary(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = new User();
        user.setName((String)request.getParameter("UserName"));
        user.setOpt(UserOpt.O_NAME.get());
        
        Vector<User> vecUser = MaintenanceFactory.getInstance().getMaintenance().userQuery(user);
        if (null == vecUser || 1 != vecUser.size()) {
        	response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_USER_QUERY)));
        	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_USER_QUERY));
        	return;
        }
        
        Value value = new Value();
        value.setOpt(ValueOpt.O_LASTREC.get());
        Vector<Value> vecValue = MaintenanceFactory.getInstance().getMaintenance().valueQuery(vecUser.get(0).getID(), value);
        if (null == vecValue || 1 != vecValue.size()) {
        	response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_VALUE_QUERY)));
        	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_VALUE_QUERY));
        	return;
        }
        
        String jsonValue = RePackage(vecUser.get(0).getID(), vecValue.get(0).getValue());
        response.getWriter().println(getFormatResult("ok", jsonValue));
	}
	
	private void queryDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int userId = Integer.parseInt((String)request.getParameter("UserID"));
			int nodeId = Integer.parseInt((String)request.getParameter("NodeID"));
			String date = (String)request.getParameter("Date");
			
			Value value = new Value();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			value.setDate(d);
			value.setOpt(ValueOpt.O_ONEDAYRECS.get());
			
			Vector<Value> vecValue = MaintenanceFactory.getInstance().getMaintenance().valueQuery(userId, value);
			if (null == vecValue) {
				response.getWriter().println(getFormatResult("error", GetLastError.instance().getErrorMsg(ErrorCode.E_VALUE_QUERY)));
	        	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_VALUE_QUERY));
	        	return;
			}
			
			String jsonValue = RePackage(nodeId, vecValue);
	        response.getWriter().println(getFormatResult("ok", jsonValue));
		} catch (NumberFormatException e) {
			log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		} catch (ParseException e) {
			log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		}
	}
	
	private String RePackage(int nodeId, Vector<Value> vecValue) {
		try {
			
		} catch (JSONException e) {
			log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());			
		}
		
		return "";
	}
	
	private String RePackage(int userId, String value) {
		try {
			JSONObject json = JSONObject.fromObject(value);
		
			// 解析type
			JSONArray jsonTypeArray = JSONArray.fromObject(json.get("type"));
			
			// 重新组装数据
			JSONStringer stringerTypeArray = new JSONStringer();
			stringerTypeArray.array();
			
			for(int i = 0; i < jsonTypeArray.size(); i++) {
				JSONObject jsonType = JSONObject.fromObject(jsonTypeArray.getString(i));
				
				// 解析类型id
        		int typeId = Integer.parseInt(String.valueOf(jsonType.get("t")));
        		
        		// 查询type表，获取类型名称、阈值
        		Type type = new Type();
        		type.setType(typeId);
        		type.setOpt(TypeOpt.O_TYPE.get());
        		
        		Vector<Type> vecType = MaintenanceFactory.getInstance().getMaintenance().typeQuery(type);
        		String typeName = "未知类型(" + typeId + ")";
        		double min = 0.0, max = 0.0;
        		if (vecType != null && 1 == vecType.size()) {
        			typeName = vecType.get(0).getName();
        			min = vecType.get(0).getMin();
        			max = vecType.get(0).getMax();
        		}
        		
        		// 解析此类型的数据
        		JSONArray jsonValueArray = JSONArray.fromObject(jsonType.get("v"));
        		
        		// 重新组装数据
        		JSONStringer stringerValueArray = new JSONStringer();
        		stringerValueArray.array();
        		
        		for (int j = 0; j < jsonValueArray.size(); j++) {
        			// 解析节点数据
        			JSONObject jsonValue = JSONObject.fromObject(jsonValueArray.getString(j));
        			
        			int nodeId = Integer.parseInt(String.valueOf(jsonValue.get("n")));
        			
        			// 查询node表，获取节点的别名
        			Node node = new Node();
        			node.setValue(nodeId);
        			node.setOpt(NodeOpt.O_VALUE.get());
        			
        			Vector<Node> vecNode = MaintenanceFactory.getInstance().getMaintenance().nodeQuery(userId, node);
        			String nodeAlias = new String("未命名节点");
        			if (vecNode != null && 1 == vecNode.size()) {
        				nodeAlias = vecNode.get(0).getName();
        			}
        			
        			stringerValueArray.object()
        				.key("n").value(jsonValue.get("n"))		// 节点Id
        				.key("a").value(nodeAlias)				// 节点别名
        				.key("v").value(jsonValue.get("v"))		// 节点值
        				.endObject();
        		}
        		
        		stringerValueArray.endArray();
        		
        		stringerTypeArray.object()
        			.key("t").value(typeName)						// 类型名称
        			.key("min").value(min)							// 最小值
        			.key("max").value(max)							// 最大值
        			.key("v").value(stringerValueArray.toString())	// 该类型中的数据
        			.endObject();
			}
			
			stringerTypeArray.endArray();
			
			JSONStringer stringer = new JSONStringer();
			stringer.object()
				.key("userId").value(userId)
				.key("type").value(stringerTypeArray.toString())
				.endObject();
			return stringer.toString();
		} catch (JSONException e) {
			log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());			
		} catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
		
		return "";
	}

    private String getFormatResult(String result, String tipMsg) {
        JSONStringer stringer = new JSONStringer();
        
        stringer.object().key("result").value(result).key("tipMsg").value(tipMsg).endObject();
        
        return stringer.toString();
    }
}
