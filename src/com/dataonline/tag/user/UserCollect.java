package com.dataonline.tag.user;

import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONStringer;

import com.dataonline.factory.MaintenanceFactory;
import com.dataonline.intfc.UserOpt;
import com.dataonline.pojo.User;
import com.dataonline.util.LineNo;

public class UserCollect extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Vector<User> vecUser = new Vector<User>();
	private Logger log = Logger.getLogger(UserCollect.class);
	
	@Override
	public int doStartTag() throws JspException {
		test();
		try {
            User user = new User();
          
            user.setOpt(UserOpt.O_ALL.get());
            vecUser = MaintenanceFactory.getInstance().getMaintenance().userQuery(user);
            
            pageContext.setAttribute("users", vecUser);
            
            JSONStringer stringer = new JSONStringer();
            stringer.array();
            if (vecUser != null) {
                for (int i = 0; i < vecUser.size(); i++) {
                    stringer.object().key("id").value(vecUser.get(i).getID())
                        .key("name").value(jsonEncode(vecUser.get(i).getName()))
                        .key("password").value(jsonEncode(vecUser.get(i).getPassword()))
                        .key("type").value(vecUser.get(i).getType())
                        .endObject();
                }
            }
            
            stringer.endArray();
            
            // 给JavaScript使用
            pageContext.getRequest().setAttribute("jsonUser", stringer.toString());
            
        } catch (NumberFormatException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + e.getMessage());
        } catch (JSONException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - "  + "用户表为空"); 
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
    
    private String jsonEncode(String str) {
    	if (null == str) {
    		return "";
    	}
    	
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").replaceAll("\"", "&quot;");
    }
    
    private void test() {
    	// 构造数据
    	String nodeValue[] = {value2string(1, 11.1), value2string(2, 22.2)};
    	String nodeValueArray = array2string(nodeValue, 2);
    	String typeValue[] = {type2string(1, nodeValueArray), type2string(2, nodeValueArray)};
    	String typeValueArray = array2string(typeValue, 2);
    	
    	String sTotalString = "{\"id\":2,\"type\":" + typeValueArray + "}";
    	
    	// 解析数据
    	try {
    		JSONObject json = JSONObject.fromObject(sTotalString);
    		
    		// 解析id
    		System.out.println(json.get("id"));
    		
    		// 解析type
        	JSONArray jsonTypeArray = JSONArray.fromObject(json.get("type"));
        	System.out.println(jsonTypeArray.size());
        	if(jsonTypeArray.size() > 0) {
	        	for(int i = 0; i < jsonTypeArray.size(); i++) {
	        		JSONObject jsonType = JSONObject.fromObject(jsonTypeArray.getString(i));
	        		
	        		// 解析类型id
	        		System.out.println(jsonType.get("t"));
	        		// 解析此类型的数据
	        		JSONArray jsonValueArray = JSONArray.fromObject(jsonType.get("v"));
	        		
	        		for (int j = 0; j < jsonValueArray.size(); j++) {
	        			// 解析节点数据
	        			JSONObject jsonValue = JSONObject.fromObject(jsonValueArray.getString(j));
	        			
	        			// 解析节点Id
	        			System.out.println(jsonValue.get("n"));
	        			// 解析节点值
	        			System.out.println(jsonValue.get("v"));
	        		}
	        	}
        	}
        } catch (JSONException e) {
        	System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String value2string(int nodeId, double value) {
    	return "{\"n\":" + nodeId + ",\"v\":" + value + "}";
    }
    
    private String type2string(int typeId, String value) {
    	return "{\"t\":" + typeId + ",\"v\":" + value + "}";
    }
    
    private String array2string(String array[], int size) {
    	String v = "[";
    	
    	for (int i = 0; i < size - 1; i++) {
    		v += array[i] + ",";
    	}
    	
    	v += array[size - 1] + "]";
    	
    	return v;
    }
}
