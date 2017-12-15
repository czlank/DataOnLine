package com.dataonline.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;
import com.dataonline.intfc.IType;
import com.dataonline.intfc.TypeOpt;
import com.dataonline.pojo.Type;
import com.dataonline.util.LineNo;

import org.apache.log4j.Logger;

public class TypeImpl implements IType {
    private static String tableName = new String("type");

    private Logger log = Logger.getLogger(TypeImpl.class);
    private Connection conn = null;
    private String databaseName = null;
    private ErrorCode lastError = ErrorCode.E_FAIL;

    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    public TypeImpl(Connection conn, String databaseName) {
        this.conn = conn;
        this.databaseName = databaseName;
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
                + "type INT NOT NULL UNIQUE,"
                + "min DOUBLE NOT NULL,"
                + "max DOUBLE NOT NULL,"
                + "name VARCHAR(32) NOT NULL,"
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
    public ErrorCode add(Type type) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");
            
            return lastError;
        }

        String sql = "insert into "
                + tableName
                + " (type, min, max, name) values ("
                + String.valueOf(type.getType()) + ", "
                + String.valueOf(type.getMin()) + ", "
                + String.valueOf(type.getMax()) + ", "
                + "'" + type.getName() + "'"
                + ")";

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public ErrorCode update(Type type) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return lastError;
        }

        String sql = getUpdateSql(type);
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
    public ErrorCode remove(Type type) throws SQLException {
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
                + String.valueOf(type.getID());

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public Vector<Type> query(Type type) throws SQLException {
        Vector<Type> vecType = null;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return vecType;
        }

        String sql = getQuerySql(type);
        if (null == sql) {
            lastError = ErrorCode.E_OK;
            return vecType;
        }

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery(sql);

        vecType = new Vector<Type>();

        while (rs.next()) {
            Type typeRS = new Type();

            typeRS.setID(rs.getInt(1));
            typeRS.setType(rs.getInt(2));
            typeRS.setMin(rs.getDouble(3));
            typeRS.setMax(rs.getDouble(4));
            typeRS.setName(rs.getString(5));

            vecType.add(typeRS);
        }

        lastError = vecType.isEmpty() ? ErrorCode.E_FAIL : ErrorCode.E_OK;

        return vecType;
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

    private String getUpdateSql(Type type) {
        if (TypeOpt.O_NULL.get() == type.getOpt()) {
            return null;
        }

        if (TypeOpt.O_ALL.get() == type.getOpt()) {
            String sql = "update "
                    + tableName
                    
                    + "type="
                    + type.getType() + ", "
                    
                    + "min="
                    + type.getMin() + ", "
                    
                    + "max="
                    + type.getMax() + " "
                    
                    + "name="
                    + "'" + type.getName() + "' "
                    
                    + "where id="
                    + type.getID();

            return sql;
        }

        String sql = "update " + tableName + " set ";

        if (testOpt(type.getOpt(), TypeOpt.O_TYPE)) {
            sql += "type=" + type.getType() + ", ";
        }

        if (testOpt(type.getOpt(), TypeOpt.O_MIN)) {
            sql += "min=" + type.getMin() + ", ";
        }
        
        if (testOpt(type.getOpt(), TypeOpt.O_MAX)) {
            sql += "max=" + type.getMax() + ", ";
        }
        
        if (testOpt(type.getOpt(), TypeOpt.O_NAME)) {
        	sql += "name='" + type.getName() + "', ";
        }

        sql = sql.substring(0, sql.lastIndexOf(", "));
        sql += " where id=" + type.getID();

        return sql;
    }

    private String getQuerySql(Type type) {
        if (TypeOpt.O_NULL.get() == type.getOpt()) {
            return null;
        }
        
        String sql = "select * from " + tableName + " where ";
        
        if (TypeOpt.O_ALL.get() == type.getOpt()) {
            sql = "select * from " + tableName;
            return sql;
        }

        if (testOpt(type.getOpt(), TypeOpt.O_ID)) {
            sql += "id=" + type.getID() + " and ";
        }
        
        if (testOpt(type.getOpt(), TypeOpt.O_TYPE)) {
            sql += "type=" + type.getType() + " and ";
        }

        if (testOpt(type.getOpt(), TypeOpt.O_MIN)) {
            sql += "min=" + type.getMin() + " and ";
        }
        
        if (testOpt(type.getOpt(), TypeOpt.O_MAX)) {
            sql += "max=" + type.getMax() + " and ";
        }
        
        if (testOpt(type.getOpt(), TypeOpt.O_NAME)) {
        	sql += "name='" + type.getName() + "' and ";
        }

        sql = sql.substring(0, sql.lastIndexOf(" and "));

        return sql;
    }

    private boolean testOpt(int srcopt, TypeOpt destopt) {
        return ((srcopt & destopt.get()) == destopt.get());
    }
    
    @Override
    public void destroy() throws SQLException {
        conn.close();
    }
}
