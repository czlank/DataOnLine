package com.fota.impl.business;

import java.io.File;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.fota.config.Database;
import com.fota.config.Server;
import com.fota.factory.base.BaseFactory;
import com.fota.impl.base.VersionStateOpt;
import com.fota.intfc.business.IMaintenance;
import com.fota.util.error.ErrorCode;
import com.fota.util.error.GetLastError;
import com.fota.intfc.base.IIMEI;
import com.fota.intfc.base.IProject;
import com.fota.intfc.base.IRecord;
import com.fota.intfc.base.ITerminal;
import com.fota.intfc.base.IUser;
import com.fota.intfc.base.IVersion;
import com.fota.intfc.base.ProjectOpt;
import com.fota.intfc.base.VersionOpt;
import com.fota.pojo.base.Project;
import com.fota.pojo.base.Record;
import com.fota.pojo.base.User;
import com.fota.pojo.base.Version;
import com.fota.util.common.FileOperate;
import com.fota.util.common.LineNo;

public class MaintenanceImpl implements IMaintenance {
    private static final BaseFactory factory = BaseFactory.getInstance();
    private Logger log = Logger.getLogger(MaintenanceImpl.class);

    @Override
    public ErrorCode projectAdd(Project project) {
        ErrorCode result = ErrorCode.E_FAIL;
        IProject projectService = factory.getProject();
        
        if (null == projectService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Project接口失败");
            return result;
        }

        try {
            result = projectService.add(project);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                projectService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }

        return result;
    }

    @Override
    public ErrorCode projectModify(Project project) {
        ErrorCode result = ErrorCode.E_FAIL;
        IProject projectService = factory.getProject();
        
        if (null == projectService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Project接口失败");
            return result;
        }
        
        try {
            result = projectService.update(project);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                projectService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return result;
    }

    @Override
    public ErrorCode projectRemove(Project project) {
        ErrorCode result = ErrorCode.E_PROJECT_DELETE;
        IProject projectService = factory.getProject();
        
        if (null == projectService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Project接口失败");
            return result;
        }
        
        Version version = new Version();
        version.setProjectID(project.getID());
        version.setOpt(VersionOpt.O_PROJECTID.get());
        
        Vector<Version> vecVersion = versionQuery(version);
        if (null == vecVersion || 0 == vecVersion.size()) {
            try {
                //删除空目录
                if (!deleteProjectFolder(project)) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "项目目录删除失败" 
                            + "userid=" + project.getUserID() + ",prjName="+ project.getName());
                }
                result = projectService.remove(project);
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            } finally {
                try {
                    projectService.destroy();
                } catch (SQLException e) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
                }
            }
            
            return result;
        }
        
        int iInUseCount = 0;
        for (int i = 0; i < vecVersion.size(); i++) {
            int state = vecVersion.get(i).getState();
            
            if (VersionStateOpt.INIT.get() == state
                    || VersionStateOpt.READY.get() == state
                    || VersionStateOpt.FREEZE.get() == state) {
                
                Version curVersion = vecVersion.get(i);
                curVersion.setIP(project.getIP());
                result = versionRemove(curVersion);
                
                if (result != ErrorCode.E_OK) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(ErrorCode.E_VERSION_DELETE) + "（" + vecVersion.get(i).getName() + "）");
                   
                    try {
                        projectService.destroy();
                    } catch (SQLException e) {
                        log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
                    } 
                    
                    return ErrorCode.E_VERSION_DELETE;
                }
            } else if (VersionStateOpt.RELEASE.get() == state || VersionStateOpt.FREEZEING.get() == state) {
                iInUseCount++;
            }
        }
        
        if (0 == iInUseCount) {
            try {
                ErrorCode ec = projectService.remove(project);
                if (ec != ErrorCode.E_OK) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "项目信息删除失败");
                    return ec;
                }
                //删除Project目录
                if (!deleteProjectFolder(project)) {
                   log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "项目目录删除失败" 
                           + "userid=" + project.getUserID() + ",prjName="+ project.getName());
                }
                
                return ErrorCode.E_OK;
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            } finally {
                try {
                    projectService.destroy();
                } catch (SQLException e) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
                }
            }
            
            return ErrorCode.E_PROJECT_DELETE;
        }
        else {
            return ErrorCode.E_PROJECT_VERSION_IN_USE;
        }
    }

    @Override
    public ErrorCode projectRemove(User user) {
        boolean hasError = false;
        Project project = new Project();
        
        project.setUserID(user.getID());
        project.setOpt(ProjectOpt.O_USERID.get());
        
        Vector<Project> vecProject = projectQuery(project);
        
        if (null == vecProject || 0 == vecProject.size()) {
            return ErrorCode.E_OK;
        }

        IProject projectService = factory.getProject();
        
        if (null == projectService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Project接口失败");
            return null;
        }
        
        for (int prjIdx = 0; prjIdx < vecProject.size(); prjIdx++) {
            project = vecProject.get(prjIdx);
            project.setIP(user.getIP());
            try {
                if (ErrorCode.E_OK != projectService.remove(project)) {
                    log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + project.getName() + "项目删除失败"); 
                    hasError = true;
                }
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
                hasError = true;
            }
        }
        
        try {
            projectService.destroy();
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
        
        // 删除用户目录
        if (!deleteUserFolder(user.getID())) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "删除用户目录失败" + "userID=" + user.getID());
        }
        
        return hasError ? ErrorCode.E_FAIL : ErrorCode.E_OK;
    }
    
    @Override
    public Vector<Project> projectQuery(Project project) {
        Vector<Project> vecProject = null;
        IProject projectService = factory.getProject();
        
        if (null == projectService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取Project接口失败");
            return null;
        }
        
        try {
            vecProject = projectService.query(project);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                projectService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return vecProject;
    }
    
    @Override
    public ErrorCode versionAdd(Version version) {
        IVersion versionService = factory.getVersion();
        if (null == versionService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IVersion接口失败");
            return ErrorCode.E_FAIL;
        }
        
        try {
            if (ErrorCode.E_FAIL == versionService.add(version)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "版本添加失败");
                return ErrorCode.E_FAIL;
            }
            //获得版本号
            Vector<Version> vecVersion = versionQuery(version);
            if (null == vecVersion || 1 != vecVersion.size()) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "版本信息不存在");  
                return ErrorCode.E_FAIL;
            }
            
            Version newVersion = vecVersion.get(0);
            if (ErrorCode.E_FAIL == createTable(newVersion)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "版本添加--终端表添加失败");
                versionRemove(newVersion);
                
                return ErrorCode.E_FAIL; 
            }

            //更新版本State
            newVersion.setState(0);
            newVersion.setIP(version.getIP());
            newVersion.setOpt(VersionOpt.O_STATE.get() | VersionOpt.O_ID.get());
            
            if (ErrorCode.E_OK != versionModify(newVersion)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "更新版本状态失败 -1->0");
                versionRemove(newVersion);
                return ErrorCode.E_FAIL; 
            }
            
            return ErrorCode.E_OK;
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                versionService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return ErrorCode.E_FAIL;
    }
    
    private ErrorCode createTable(Version version) {
        IIMEI imeiService = null;
        ITerminal terminalService = null;
       
        imeiService = factory.getIMEI();
        if (null == imeiService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IIMEI接口失败");
            return ErrorCode.E_FAIL;
        }
        
        terminalService = factory.getTerminal();
        if (null == terminalService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取终端表接口失败");
            return ErrorCode.E_FAIL;
        }
            
        try {
            if (ErrorCode.E_OK != imeiService.create(version.getUserID(), version.getProjectID(), version.getID())) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建IMEI表失败");
                return ErrorCode.E_FAIL;
            }
            
            if (ErrorCode.E_OK != terminalService.create(version.getUserID(), version.getProjectID(), version.getID())) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "创建终端表失败");
                return ErrorCode.E_FAIL;
            }
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            return ErrorCode.E_FAIL;
        } finally {
            try {
                imeiService.destroy();
                terminalService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return ErrorCode.E_OK;
    }

    @Override
    public ErrorCode versionModify(Version version) {
        IVersion versionService = factory.getVersion();
        if (null == versionService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IVersion接口失败");
            return ErrorCode.E_FAIL;
        }
        
        try {
            return versionService.update(version);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                versionService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return ErrorCode.E_FAIL;
    }

    @Override
    public ErrorCode versionRemove(Version version) {
        IIMEI     imeiService     = factory.getIMEI();
        IVersion  versionService  = factory.getVersion();
        ITerminal terminalService = factory.getTerminal();
        
        if (null == imeiService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IIMEI接口失败");
            return ErrorCode.E_FAIL;
        }
        
        if (null == versionService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IVersion接口失败");
            return ErrorCode.E_FAIL;
        }
        
        if (null == terminalService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取ITerminal接口失败");
            return ErrorCode.E_FAIL;
        }
        
        try {
            imeiService.drop(version.getUserID(), version.getProjectID(), version.getID());
            terminalService.drop(version.getUserID(), version.getProjectID(), version.getID());
            ErrorCode ec = versionService.remove(version);
            if (ErrorCode.E_OK != ec) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "版本记录删除失败");
                return ec;
            }
            //删除版本文件
            if (!deleteVersionFolder(version)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "版本文件删除失败" 
                        + "userid=" + version.getUserID() + ",prjid="+ version.getProjectID() + ",verid="+ version.getID() + ",verName=" + version.getName());        
            }
            
            return ErrorCode.E_OK;
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                imeiService.destroy();
                versionService.destroy();
                terminalService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return ErrorCode.E_FAIL;
    }
    
    @Override
    public ErrorCode versionRemove(User user) {
        boolean hasError = false;
        Version version = new Version();
            
        version.setUserID(user.getID());
        version.setOpt(ProjectOpt.O_USERID.get());
        
        Vector<Version> vecversion = versionQuery(version);
        
        if (null == vecversion || 0 == vecversion.size()) {
            return ErrorCode.E_OK;
        }
        
        for (int verIdx = 0; verIdx < vecversion.size(); verIdx++) {
            version = vecversion.get(verIdx);
            version.setIP(user.getIP());
            
            if (ErrorCode.E_OK != versionRemove(version)) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + version.getName() + "项目删除失败"); 
                hasError = true;
            }
        }
        
        return (hasError == true)?ErrorCode.E_FAIL:ErrorCode.E_OK;
    }

    @Override
    public Vector<Version> versionQuery(Version version) {
        Vector<Version> vecVersion = null;
        IVersion versionService = factory.getVersion();
        if (null == versionService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IVersion接口失败");
            return null;
        }
        
        try {
            vecVersion = versionService.query(version);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                versionService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            } 
        }
        
        return vecVersion;
    }
    
    @Override
    public boolean versionIsUnique(Version version) {
        IVersion versionService = factory.getVersion();
        if (null == versionService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IVersion接口失败");
            return false;
        }
        
        try {
            Vector<Version> vecVersion = versionService.query(version);
            
           if (null == vecVersion || 0 == vecVersion.size()) {
               return true;
           }
           
           // 此种场景适用于只更新版本差分包，源目版本号都不变
           if (1 == vecVersion.size() && version.getID() == vecVersion.get(0).getID()) {
               return true;
           } else {
               return false;
           }
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                versionService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return false;
    }
    
    @Override
    public ErrorCode userAdd(User user) {
        ErrorCode result = ErrorCode.E_FAIL;
        IUser userService = factory.getUser();
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IUser接口失败");
            return result;
        }
        
        try {
            result = userService.add(user);
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
    public ErrorCode userModify(User user) {
        ErrorCode result = ErrorCode.E_FAIL;
        IUser userService = factory.getUser();
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IUser接口失败");
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
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IUser接口失败");
            return result;
        }
        
        try {
            result = userService.remove(user);
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
    public Vector<User> userQuery(User user) {
        Vector<User> vecUser = null;
        IUser userService = factory.getUser();
        
        if (null == userService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IUser接口失败");
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
    public Vector<Record> recordQuery(Record record, String beginTime, String endTime) {
        Vector<Record> vecRecord = null;
        IRecord recordService = factory.getRecord();
        
        if (null == recordService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IRecord接口失败");
            return null;
        }
        
        try {
            vecRecord = recordService.query(record, beginTime, endTime);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } catch (NullPointerException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                recordService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return vecRecord;
    }
    
    @Override
    public ErrorCode recordRemove() {
        ErrorCode result = ErrorCode.E_FAIL;
        
        Database xml = new Database("config.xml");
        if (!xml.getFlag()) {
            return ErrorCode.E_OK;
        }
        
        IRecord recordService = factory.getRecord();
        
        if (null == recordService) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "获取IRecord接口失败");
            return null;
        }

        try {
            result = recordService.remove();
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                recordService.destroy();
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }
        
        return result;
    }
    
    private boolean deleteVersionFolder(Version version) {
        Server xml = new Server("config.xml");
        String rootpath = xml.getRootPath();
        String sPath = rootpath + "Versions" 
                      + File.separator + version.getUserID()
                      + File.separator + version.getProjectID()
                      + File.separator + version.getID();
        
        return FileOperate.deleteFolder(sPath);
    }
    
    private boolean deleteProjectFolder(Project prj) {
        Server xml = new Server("config.xml");
        String rootpath = xml.getRootPath();
        String sPath = rootpath + "Versions" 
                      + File.separator + prj.getUserID()
                      + File.separator + prj.getID();
        
        return FileOperate.deleteFolder(sPath);
    }
    
    private boolean deleteUserFolder(int userID) {
        Server xml = new Server("config.xml");
        String rootpath = xml.getRootPath();
        String sPath = rootpath + "Versions"
                      + File.separator + userID;
        
        return FileOperate.deleteFolder(sPath);
    }
}
