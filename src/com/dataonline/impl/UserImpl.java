package com.dataonline.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;
import com.dataonline.intfc.IUser;
import com.dataonline.intfc.UserOpt;
import com.dataonline.pojo.User;
import com.dataonline.util.LineNo;

import org.apache.log4j.Logger;

public class UserImpl implements IUser {
    private static String tableName = new String("user");

    private Logger log = Logger.getLogger(UserImpl.class);
    private Connection conn = null;
    private String databaseName = null;
    private ErrorCode lastError = ErrorCode.E_FAIL;

    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    public UserImpl(Connection conn, String databaseName) {
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
                + "type INT NOT NULL,"
                + "name VARCHAR(256) NOT NULL UNIQUE,"
                + "password VARCHAR(256),"
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
    public ErrorCode add(User user) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");
            
            return lastError;
        }

        String sql = "insert into "
                + tableName
                + " (type, name, password) values ("
                + String.valueOf(user.getType()) + ", "
                + "BINARY '" + user.getName() + "', "
                + "'" + user.getPassword() + "'"
                + ")";

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public ErrorCode update(User user) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return lastError;
        }

        String sql = getUpdateSql(user);
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
    public ErrorCode remove(User user) throws SQLException {
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
                + String.valueOf(user.getID());

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public Vector<User> query(User user) throws SQLException {
        Vector<User> vecUser = null;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return vecUser;
        }

        String sql = getQuerySql(user);
        if (null == sql) {
            lastError = ErrorCode.E_OK;
            return vecUser;
        }

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery(sql);

        vecUser = new Vector<User>();

        while (rs.next()) {
            User userRS = new User();

            userRS.setID(rs.getInt(1));
            userRS.setType(rs.getInt(2));
            userRS.setName(rs.getString(3));
            userRS.setPassword(rs.getString(4));

            vecUser.add(userRS);
        }

        lastError = vecUser.isEmpty() ? ErrorCode.E_FAIL : ErrorCode.E_OK;

        return vecUser;
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

    private String getUpdateSql(User user) {
        if (UserOpt.O_NULL.get() == user.getOpt()) {
            return null;
        }

        if (UserOpt.O_ALL.get() == user.getOpt()) {
            String sql = "update "
                    + tableName
                    
                    + "type="
                    + String.valueOf(user.getType()) + ", "
                    
                    + " set name=BINARY "
                    + "'" + user.getName() + "', "
                    
                    + "password="
                    + "'" + user.getPassword() + "' "
                    
                    + "where id="
                    + String.valueOf(user.getID());

            return sql;
        }

        String sql = "update " + tableName + " set ";

        if (testOpt(user.getOpt(), UserOpt.O_NAME)) {
            sql += "name=BINARY '" + user.getName() + "', ";
        }

        if (testOpt(user.getOpt(), UserOpt.O_PASSWORD)) {
            sql += "password='" + user.getPassword() + "', ";
        }

        sql = sql.substring(0, sql.lastIndexOf(", "));
        sql += " where id=" + String.valueOf(user.getID());

        return sql;
    }

    private String getQuerySql(User user) {
        if (UserOpt.O_NULL.get() == user.getOpt()) {
            return null;
        }
        
        String sql = "select * from " + tableName + " where ";
        
        if (UserOpt.O_ALL.get() == user.getOpt()) {
            sql = "select * from " + tableName;
            return sql;
        }

        if (testOpt(user.getOpt(), UserOpt.O_ID)) {
            sql += "id=" + user.getID() + " and ";
        }
        
        if (testOpt(user.getOpt(), UserOpt.O_TYPE)) {
            sql += "type=" + user.getType() + " and ";
        }

        if (testOpt(user.getOpt(), UserOpt.O_NAME)) {
            sql += "name= BINARY '" + user.getName() + "' and ";
        }
        
        sql = sql.substring(0, sql.lastIndexOf(" and "));

        return sql;
    }

    private boolean testOpt(int srcopt, UserOpt destopt) {
        return ((srcopt & destopt.get()) == destopt.get());
    }
    
    @Override
    public void destroy() throws SQLException {
        conn.close();
    }
}
