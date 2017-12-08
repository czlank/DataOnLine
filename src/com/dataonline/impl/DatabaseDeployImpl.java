package com.dataonline.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.dataonline.util.LineNo;
import com.dataonline.intfc.IDatabaseDeploy;
import com.dataonline.config.Database;
import com.dataonline.factory.*;
import com.dataonline.intfc.*;
import com.dataonline.pojo.User;
import com.dataonline.service.DatabaseDeployService;
import com.dataonline.util.ErrorCode;

public class DatabaseDeployImpl implements IDatabaseDeploy {
	private Connection connection = null;
	private Logger log = Logger.getLogger(DatabaseDeployImpl.class);
	private String databaseName = "";
	
	public DatabaseDeployImpl(Connection connection, String databaseName) {
        this.connection = connection;
        this.databaseName = databaseName;
    }
	
	@Override
	public boolean config(String userName, String password, String dbName) {
	    Database xml = new Database("config.xml");
	    String oldDatabaseName = xml.getDatabaseName();
	    if (null == oldDatabaseName) {
	        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "无法获取旧数据库名称，请检查\"config.xml\"文件");
	        return false;
	    }

	    reset(userName, password, dbName);
	    
        if (!oldDatabaseName.isEmpty() && !dropDataBase(oldDatabaseName)) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "删除数据库出错");
            return false;
        }
        
        // 部署数据库时，如果xml文件不存在，但是MySQL中有同名的数据库，则重新做一遍create动作
        if (!createDatabase(databaseName) && !createDatabase(databaseName)) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建数据库出错");
            return false;
        }
        
        if (!useDatabase(databaseName)) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "切换数据库出错");
            return false;
        }
        
        BaseFactory.getInstance().Init();
        
        if (!createTable(userName, password, dbName)) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建表格出错");
            return false;
        }
        
        update(true);

        return true;
    }
	
	private boolean dropDataBase(String strDbName) {
        boolean result = true;
        PreparedStatement prest = null;
        String strSql = "drop database if exists " + strDbName;

        try {
            prest = connection.prepareStatement(strSql);
            prest.execute(strSql);
        } catch (SQLException e) {
            result = false;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            if (null != prest) {
                try {
                    prest.close();
                } catch (SQLException e) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
                }
                prest = null;
             }
        }

        return result;
    }

	private boolean createDatabase(String strDbName) {
		boolean result = true;
		PreparedStatement prest = null;
		String strSql = "create database " + strDbName;

		try {
			prest = connection.prepareStatement(strSql);
		    prest.executeUpdate(strSql);
		} catch (SQLException e) {
		    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		    
		    // 已存在同名数据库时，进行删除操作
		    if (e.getMessage().contains("database exists")) {
		        log.info(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建数据库时，遇到无法删除旧的同名数据库问题，重建数据库");
		        
		        dropDataBase(strDbName);
		    }
		    
		    result = false;
		} finally {
		    if (null != prest) {
		        try {
		            prest.close();
		        } catch (SQLException e) {
		            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		        }
		        prest = null;
		    }
		}
		
		return result;
	}
	
	private boolean useDatabase(String strDbName) {
		boolean result = true;
		PreparedStatement prest = null;
		String strSql = "use " + strDbName;
		
		try {
			prest = connection.prepareStatement(strSql);
			prest.executeUpdate(strSql);
		} catch (SQLException e) {
			result = false;
			log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		} finally {
		    if (null != prest) {
		        try {
		            prest.close();
		        } catch (SQLException e) {
		            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		        }
		        prest = null;
		    }
		}
	
		return result;
	}
	
	private boolean createTable(String userName, String password, String dbName) {
		IUser userService = BaseFactory.getInstance().getUser();
		IProject projectService = BaseFactory.getInstance().getProject();
		IVersion versionService = BaseFactory.getInstance().getVersion();
		IRecord recordService = BaseFactory.getInstance().getRecord();
		
		if (null == userService) {
		    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IUser接口失败");
            return false;
		}
		
		if (null == projectService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IProject接口失败");
            return false;
        }
		
		if (null == versionService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IVersion接口失败");
            return false;
        }
		
		if (null == recordService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IRecord接口失败");
            return false;
        }
		
		try {
		    if (userService.create() != ErrorCode.E_OK) {
		        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建用户表失败");
		        return false;
		    }
			
		    User user = new User();
	        
	        user.setName("admin");
	        user.setPassword("admin");
	        user.setToken("admin");
	        user.setType(0);
	        user.setOEM("admin");
		    
		    if (userService.add(user) != ErrorCode.E_OK) {
		        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "添加用户失败");
		    	return false;
		    }

		    if (projectService.create() != ErrorCode.E_OK) {
	            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建项目表失败");
		        return false;
		    }
		    
		    if (versionService.create() != ErrorCode.E_OK) {
		        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建版本表失败");
		        return false;
		    }
		    
		    if (recordService.create() != ErrorCode.E_OK) {
		        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建日志表失败");
		        return false;
		    }
		} catch (SQLException e) {
			log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		    return false;
		} finally {
		    try {
                userService.destroy();
                projectService.destroy();
                versionService.destroy();
                recordService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
		}
	
		return true;
	}
	
	private void reset(String userName, String password, String dbName) {
	    Database xml = new Database("config.xml");
        
        // 将数据库信息写入配置文件
	    xml.setFlag(false);
        xml.setUserName(userName);
        xml.setPassword(password);
        xml.setDatabaseName(dbName);
    }
	
	private void update(boolean flag) {
	    Database xml = new Database("config.xml");
        xml.setFlag(flag);
	}
	
	private boolean deleteVersions() {
        Server xml = new Server("config.xml");
        String rootpath = xml.getRootPath();
        String sPath = rootpath + "Versions";
        
        boolean bRet = FileOperate.deleteFolder(sPath);
        if (bRet) {
            File dir = new File(sPath + File.separator);
            bRet = dir.mkdir();
        }
        
        return bRet;
    }
}
