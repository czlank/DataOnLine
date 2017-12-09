package com.dataonline.impl;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.dataonline.factory.*;
import com.dataonline.intfc.*;
import com.dataonline.pojo.*;
import com.dataonline.util.*;

public class MaintenanceImpl implements IMaintenance {
    private static final BaseFactory factory = BaseFactory.getInstance();
    private Logger log = Logger.getLogger(MaintenanceImpl.class);

    @Override
    public ErrorCode userAdd(User user) {
        ErrorCode result = ErrorCode.E_FAIL;
        IUser userService = factory.getUser();
        IValue valueService = null;
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取User接口失败");
            return result;
        }
        
        try {
        	// 创建账户
            result = userService.add(user);
            
            // 查询新账户ID
            user.clearOpt(UserOpt.O_ALL.get());
            user.setOpt(UserOpt.O_NAME.get());
            Vector<User> userVec = userService.query(user);
            
            // 创建账户对应的数据表
            if (userVec != null && 1 == userVec.size()) {
            	valueService = factory.getValue(userVec.get(0).getID());
            	valueService.create();
            } else {
            	log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "查询新账户失败：" + userVec == null ? "未查询到新创建的账户" : "查询到" + userVec.size() + "条账户记录");
            }
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                userService.destroy();
                
                if (valueService != null) {
                	valueService.destroy();
                }
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return result;
    }
    
    @Override
    public ErrorCode userModify(User user) {
        ErrorCode result = ErrorCode.E_FAIL;
        IUser userService = factory.getUser();
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取User接口失败");
            return result;
        }
        
        try {
            result = userService.update(user);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                userService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return result;
    }
    
    @Override
    public ErrorCode userRemove(User user) {
        ErrorCode result = ErrorCode.E_FAIL;
        IUser userService = factory.getUser();
        IValue valueService = factory.getValue(user.getID());
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取User接口失败");
            return result;
        }
        
        try {
        	// 删除账户
            result = userService.remove(user);
            
            // 删除账户对应的数据表
            valueService.drop();
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                userService.destroy();
                valueService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return result;
    }
    
    @Override
    public Vector<User> userQuery(User user) {
        Vector<User> vecUser = null;
        IUser userService = factory.getUser();
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取User接口失败");
            return null;
        }
        
        try {
            vecUser = userService.query(user);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                userService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return vecUser;
    }
    
    @Override
    public ErrorCode typeAdd(Type type) {
        ErrorCode result = ErrorCode.E_FAIL;
        IType typeService = factory.getType();
        
        if (null == typeService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Type接口失败");
            return result;
        }

        try {
            result = typeService.add(type);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
            	typeService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }

        return result;
    }

    @Override
    public ErrorCode typeModify(Type type) {
        ErrorCode result = ErrorCode.E_FAIL;
        IType typeService = factory.getType();
        
        if (null == typeService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Type接口失败");
            return result;
        }
        
        try {
            result = typeService.update(type);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
            	typeService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return result;
    }

    @Override
    public ErrorCode typeRemove(Type type) {
        ErrorCode result = ErrorCode.E_TYPE_DELETE;
        IType typeService = factory.getType();
        
        if (null == typeService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Type接口失败");
            return result;
        }
        
        try {
            result = typeService.remove(type);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
            	typeService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return result;
    }

    @Override
    public Vector<Type> typeQuery(Type type) {
        Vector<Type> vecType = null;
        IType typeService = factory.getType();
        
        if (null == typeService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Type接口失败");
            return null;
        }
        
        try {
        	vecType = typeService.query(type);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
            	typeService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return vecType;
    }
    
    public Vector<Value> valueQuery(int userID, Value value) {
    	Vector<Value> vecValue = null;
    	IValue valueService = factory.getValue(userID);
    	
    	if (null == valueService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Value接口失败");
            return null;
        }
    	
    	try {
    		vecValue = valueService.query(value);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
            	valueService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return vecValue;
    }
}
