package com.fota.service.base;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.fota.db.Connector;
import com.fota.util.error.ErrorCode;
import com.fota.intfc.base.IRecord;
import com.fota.intfc.base.IUser;
import com.fota.intfc.base.UserOpt;
import com.fota.impl.base.RecordImpl;
import com.fota.impl.base.RecordTypeOpt;
import com.fota.impl.base.UserImpl;
import com.fota.manager.BaseSyncManager;
import com.fota.pojo.base.Record;
import com.fota.pojo.base.User;
import com.fota.util.common.DataTimeCvt;
import com.fota.util.common.LineNo;

public class UserService implements IUser {
    private Connector dbc = null;
    private IUser intfc = null;
    private IRecord intfcRec = null;
    
    private Logger log = Logger.getLogger(UserService.class);

    public UserService(String databaseName, String user, String password) throws Exception {
        dbc = new Connector(databaseName, user, password);
        intfc = new UserImpl(dbc.getConnection(), databaseName);
        intfcRec = new RecordImpl(dbc.getConnection(), databaseName);
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
            
            // 查询操作账户的ID
            User userQry = new User();
            Vector<User> vecUser = query(userQry);
            
            userQry.setID(user.getOpID());
            userQry.setOpt(UserOpt.O_ALL.get() | UserOpt.O_ID.get());
            vecUser = query(userQry);
            
            if (vecUser != null && 1 == vecUser.size()) {
                user.setID(vecUser.get(0).getID());
            } else {
                user.setID(-1);
            }
            
            AddRecord("添加账户：", user);
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
            AddRecord("更新账户：", user);
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
            
            // 在删除前查询被删除的记录
            User userQry = new User();
            userQry.setID(user.getID());
            userQry.setOpt(UserOpt.O_ALL.get() | UserOpt.O_ID.get());
            Vector<User> vecUser = query(userQry);
            
            result = intfc.remove(user);
            
            if (vecUser != null && 1 == vecUser.size()) {
                User userDel = vecUser.get(0);
                
                // 查询操作账户的ID
                userQry.setID(user.getOpID());
                vecUser = query(userQry);
                if (vecUser != null && 1 == vecUser.size()) {
                    userDel.setID(vecUser.get(0).getID());
                } else {
                    userDel.setID(-1);
                }
                
                userDel.setIP(user.getIP());
                userDel.setOpt(UserOpt.O_NAME.get());
                AddRecord("删除账户：", userDel);
            }
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
    
    private void AddRecord(String operate, User user) {
        Record record = new Record();
        
        record.setUserID(user.getID());
        record.setIP(user.getIP());
        record.setType(RecordTypeOpt.USER.get());
        record.setTypeID(user.getID());
        record.setOperate(operate + user.toString());
        record.setTime(DataTimeCvt.getCurrentDate());
        
        try {
            intfcRec.add(record);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            RecordImpl recordImpl = (RecordImpl)intfcRec;
            recordImpl.closeRs();
            recordImpl.closePstmt();
        }
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
