package com.dataonline.service;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.dataonline.db.Connector;
import com.dataonline.util.ErrorCode;
import com.dataonline.intfc.IValue;
import com.dataonline.impl.ValueImpl;
import com.dataonline.pojo.Value;
import com.dataonline.manager.BaseSyncManager;
import com.dataonline.util.LineNo;

public class ValueService implements IValue {
    private Connector dbc = null;
    private IValue intfc = null;
    
    private Logger log = Logger.getLogger(ValueService.class);

    public ValueService(int userID, String databaseName, String user, String password) throws Exception {
        dbc = new Connector(databaseName, user, password);
        intfc = new ValueImpl(userID, dbc.getConnection(), databaseName);
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
            ValueImpl valueImpl = (ValueImpl)intfc;
            valueImpl.closePstmt();
            
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
        	ValueImpl valueImpl = (ValueImpl)intfc;
        	valueImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode add(Value value) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.add(value);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                result = ErrorCode.E_DUPLICATE_ENTRY;

                ValueImpl valueImpl = (ValueImpl)intfc;
                valueImpl.setLastError(result);
            }

            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	ValueImpl valueImpl = (ValueImpl)intfc;
        	valueImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode update(Value value) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.update(value);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	ValueImpl valueImpl = (ValueImpl)intfc;
        	valueImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode remove(Value value) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.remove(value);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	ValueImpl valueImpl = (ValueImpl)intfc;
        	valueImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public Vector<Value> query(Value value) throws SQLException {
        Vector<Value> vecValue = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
        	vecValue = intfc.query(value);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	ValueImpl valueImpl = (ValueImpl)intfc;
        	valueImpl.closeRs();
        	valueImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return vecValue;
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
