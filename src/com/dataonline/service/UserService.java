package com.dataonline.service;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.dataonline.db.Connector;
import com.dataonline.util.ErrorCode;
import com.dataonline.intfc.IUser;
import com.dataonline.impl.UserImpl;
import com.dataonline.pojo.User;
import com.dataonline.manager.BaseSyncManager;
import com.dataonline.util.LineNo;

public class UserService implements IUser {
    private Connector dbc = null;
    private IUser intfc = null;
    
    private Logger log = Logger.getLogger(UserService.class);

    public UserService(String databaseName, String user, String password) throws Exception {
        dbc = new Connector(databaseName, user, password);
        intfc = new UserImpl(dbc.getConnection(), databaseName);
    }

    @Override
    public ErrorCode create() throws SQLException {
        ErrorCode result = null;
        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.create();
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            UserImpl userImpl = (UserImpl)intfc;
            userImpl.closePstmt();
            
            BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode drop() throws SQLException {
        ErrorCode result = null;
        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.drop();
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            UserImpl userImpl = (UserImpl)intfc;
            userImpl.closePstmt();
            
            BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode add(User user) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.add(user);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                result = ErrorCode.E_DUPLICATE_ENTRY;

                UserImpl userImpl = (UserImpl)intfc;
                userImpl.setLastError(result);
            }

            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            UserImpl userImpl = (UserImpl)intfc;
            userImpl.closePstmt();
            
            BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode update(User user) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.update(user);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            UserImpl userImpl = (UserImpl)intfc;
            userImpl.closePstmt();
            
            BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode remove(User user) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.remove(user);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            UserImpl userImpl = (UserImpl)intfc;
            userImpl.closePstmt();
            
            BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public Vector<User> query(User user) throws SQLException {
        Vector<User> vecUser = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            vecUser = intfc.query(user);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            UserImpl userImpl = (UserImpl)intfc;
            userImpl.closeRs();
            userImpl.closePstmt();
            
            BaseSyncManager.getInstance().rwUnLock();
        }

        return vecUser;
    }

    @Override
    public String getLastError() {
        return intfc.getLastError();
    }
    
    @Override
    public void destroy() {
        try {
            intfc.destroy();
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        }
    }
}
