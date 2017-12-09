package com.dataonline.service;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.dataonline.db.Connector;
import com.dataonline.util.ErrorCode;
import com.dataonline.intfc.IType;
import com.dataonline.impl.TypeImpl;
import com.dataonline.pojo.Type;
import com.dataonline.manager.BaseSyncManager;
import com.dataonline.util.LineNo;

public class TypeService implements IType {
    private Connector dbc = null;
    private IType intfc = null;
    
    private Logger log = Logger.getLogger(TypeService.class);

    public TypeService(String databaseName, String user, String password) throws Exception {
        dbc = new Connector(databaseName, user, password);
        intfc = new TypeImpl(dbc.getConnection(), databaseName);
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
            TypeImpl typeImpl = (TypeImpl)intfc;
            typeImpl.closePstmt();
            
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
        	TypeImpl typeImpl = (TypeImpl)intfc;
            typeImpl.closePstmt();
            
            BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode add(Type type) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.add(type);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                result = ErrorCode.E_DUPLICATE_ENTRY;

                TypeImpl typeImpl = (TypeImpl)intfc;
                typeImpl.setLastError(result);
            }

            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	TypeImpl typeImpl = (TypeImpl)intfc;
        	typeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode update(Type type) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.update(type);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	TypeImpl typeImpl = (TypeImpl)intfc;
        	typeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode remove(Type type) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.remove(type);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	TypeImpl typeImpl = (TypeImpl)intfc;
        	typeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public Vector<Type> query(Type type) throws SQLException {
        Vector<Type> vecType = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
        	vecType = intfc.query(type);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	TypeImpl typeImpl = (TypeImpl)intfc;
        	typeImpl.closeRs();
        	typeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return vecType;
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
