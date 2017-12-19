package com.dataonline.service;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.dataonline.db.Connector;
import com.dataonline.util.ErrorCode;
import com.dataonline.intfc.INode;
import com.dataonline.impl.NodeImpl;
import com.dataonline.pojo.Node;
import com.dataonline.manager.BaseSyncManager;
import com.dataonline.util.LineNo;

public class NodeService implements INode {
    private Connector dbc = null;
    private INode intfc = null;
    
    private Logger log = Logger.getLogger(NodeService.class);

    public NodeService(int userID, String databaseName, String user, String password) throws Exception {
        dbc = new Connector(databaseName, user, password);
        intfc = new NodeImpl(userID, dbc.getConnection(), databaseName);
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
        	NodeImpl nodeImpl = (NodeImpl)intfc;
        	nodeImpl.closePstmt();
            
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
        	NodeImpl nodeImpl = (NodeImpl)intfc;
        	nodeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode add(Node node) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.add(node);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                result = ErrorCode.E_DUPLICATE_ENTRY;

                NodeImpl nodeImpl = (NodeImpl)intfc;
                nodeImpl.setLastError(result);
            }

            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	NodeImpl nodeImpl = (NodeImpl)intfc;
        	nodeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode update(Node node) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.update(node);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	NodeImpl nodeImpl = (NodeImpl)intfc;
        	nodeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public ErrorCode remove(Node node) throws SQLException {
        ErrorCode result = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
            result = intfc.remove(node);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	NodeImpl nodeImpl = (NodeImpl)intfc;
        	nodeImpl.closePstmt();
        	
        	BaseSyncManager.getInstance().rwUnLock();
        }

        return result;
    }

    @Override
    public Vector<Node> query(Node node) throws SQLException {
        Vector<Node> vecValue = null;

        try {
        	BaseSyncManager.getInstance().rwLock();
        	vecValue = intfc.query(node);
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
        	NodeImpl nodeImpl = (NodeImpl)intfc;
        	nodeImpl.closeRs();
        	nodeImpl.closePstmt();
        	
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
