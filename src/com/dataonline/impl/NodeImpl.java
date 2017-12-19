package com.dataonline.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;
import com.dataonline.intfc.INode;
import com.dataonline.intfc.NodeOpt;
import com.dataonline.pojo.Node;
import com.dataonline.util.LineNo;

import org.apache.log4j.Logger;

public class NodeImpl implements INode {
    private String tableName = new String("");

    private Logger log = Logger.getLogger(NodeImpl.class);
    private Connection conn = null;
    private String databaseName = null;
    private ErrorCode lastError = ErrorCode.E_FAIL;

    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    public NodeImpl(int userID, Connection conn, String databaseName) {
        this.conn = conn;
        this.databaseName = databaseName;
        
        this.tableName = "node_" + userID;
    }

    @Override
    public ErrorCode create() throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (isTableExist()) {
            lastError = ErrorCode.E_TABLE_ALREADY_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "已存在");
            
            return lastError;
        }

        String sql = "CREATE TABLE "
                + tableName
                + " ("
                + "id INT NOT NULL AUTO_INCREMENT,"
                + "value INT NOT NULL,"
                + "name VARCHAR(256) UNIQUE,"
                + "PRIMARY KEY (id)"
                + ");";

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 0 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public ErrorCode drop() throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");
            
            return lastError;
        }

        String sql = "DROP TABLE IF EXISTS " + tableName;

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 0 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public ErrorCode add(Node node) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");
            
            return lastError;
        }

        String sql = "insert into "
                + tableName
                + " (value, name) values ("
                + node.getValue() + ", "
                + "BINARY '" + node.getName() + "'"
                + ")";

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public ErrorCode update(Node node) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return lastError;
        }

        String sql = getUpdateSql(node);
        if (null == sql) {
            lastError = ErrorCode.E_OK;
            return lastError;
        }

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public ErrorCode remove(Node node) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return lastError;
        }

        String sql = "delete from "
                + tableName
                + " where id="
                + node.getID();

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public Vector<Node> query(Node node) throws SQLException {
        Vector<Node> vecNode = null;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return vecNode;
        }

        String sql = getQuerySql(node);
        if (null == sql) {
            lastError = ErrorCode.E_OK;
            return vecNode;
        }

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery(sql);

        vecNode = new Vector<Node>();

        while (rs.next()) {
        	Node nodeRS = new Node();

        	nodeRS.setValue(rs.getInt(1));
        	nodeRS.setName(rs.getString(2));

        	vecNode.add(nodeRS);
        }

        lastError = vecNode.isEmpty() ? ErrorCode.E_FAIL : ErrorCode.E_OK;

        return vecNode;
    }

    @Override
    public String getLastError() {
        return GetLastError.instance().getErrorMsg(lastError);
    }

    public void setLastError(ErrorCode errorCode) {
        lastError = errorCode;
    }
    
    public void closePstmt() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "关闭PreparedStatement错误：" + e.getMessage());
        } finally {
            pstmt = null;
        }
    }
    
    public void closeRs() {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "关闭ResultSet错误：" + e.getMessage());
        } finally {
            rs = null;
        }
    }

    private boolean isTableExist() {
        boolean result = false;
        ResultSet rsTables = null;

        try {
            DatabaseMetaData meta = conn.getMetaData();
            rsTables = meta.getTables(databaseName, null, tableName, new String [] {"TABLE"});

            while (rsTables.next()) {
                result = rsTables.getString("TABLE_NAME").equalsIgnoreCase(tableName);
                if (result) {
                    break;
                }
            }
        } catch (SQLException e) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
        } finally {
            try {
                if (rsTables != null) {
                    rsTables.close();
                }
            } catch (SQLException e) {
                log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
            }
        }

        return result;
    }

    private String getUpdateSql(Node node) {
    	if (NodeOpt.O_NULL.get() == node.getOpt()) {
            return null;
        }

        if (NodeOpt.O_ALL.get() == node.getOpt()) {
            String sql = "update "
                    + tableName
                    
                    + "value="
                    + node.getValue() + ", "
                    
                    + " set name=BINARY "
                    + "'" + node.getName() + "' "

                    + "where id="
                    + node.getID();

            return sql;
        }

        String sql = "update " + tableName + " set ";

        if (testOpt(node.getOpt(), NodeOpt.O_VALUE)) {
            sql += "value=" + node.getValue() + ", ";
        }
        
        if (testOpt(node.getOpt(), NodeOpt.O_NAME)) {
            sql += "name=BINARY '" + node.getName() + "', ";
        }

        sql = sql.substring(0, sql.lastIndexOf(", "));
        sql += " where id=" + node.getID();

        return sql;
    }

    private String getQuerySql(Node node) {
    	if (NodeOpt.O_NULL.get() == node.getOpt()) {
            return null;
        }
        
        String sql = "select * from " + tableName + " where ";
        
        if (NodeOpt.O_ALL.get() == node.getOpt()) {
            sql = "select * from " + tableName;
            return sql;
        }

        if (testOpt(node.getOpt(), NodeOpt.O_ID)) {
            sql += "id=" + node.getID() + " and ";
        }
        
        if (testOpt(node.getOpt(), NodeOpt.O_VALUE)) {
            sql += "value=" + node.getValue() + " and ";
        }

        if (testOpt(node.getOpt(), NodeOpt.O_NAME)) {
            sql += "name= BINARY '" + node.getName() + "' and ";
        }
        
        sql = sql.substring(0, sql.lastIndexOf(" and "));

        return sql;
    }
    
    private boolean testOpt(int srcopt, NodeOpt destopt) {
        return ((srcopt & destopt.get()) == destopt.get());
    }

    @Override
    public void destroy() throws SQLException {
        conn.close();
    }
}
