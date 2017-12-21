package com.dataonline.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.dataonline.util.DataTimeCvt;
import com.dataonline.util.ErrorCode;
import com.dataonline.util.GetLastError;
import com.dataonline.intfc.IValue;
import com.dataonline.intfc.ValueOpt;
import com.dataonline.pojo.Value;
import com.dataonline.util.LineNo;

import org.apache.log4j.Logger;

public class ValueImpl implements IValue {
    private String tableName = new String("");

    private Logger log = Logger.getLogger(ValueImpl.class);
    private Connection conn = null;
    private String databaseName = null;
    private ErrorCode lastError = ErrorCode.E_FAIL;

    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    public ValueImpl(int userID, Connection conn, String databaseName) {
        this.conn = conn;
        this.databaseName = databaseName;
        
        this.tableName = "value_" + userID;
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
                + "time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                + "value VARCHAR(2048) NOT NULL,"
                + "PRIMARY KEY (time)"
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
    public ErrorCode add(Value value) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");
            
            return lastError;
        }

        String sql = "insert into "
                + tableName
                + " (value) values ("
                + value.getValue() + ""
                + ")";

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public ErrorCode update(Value value) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return lastError;
        }

        String sql = getUpdateSql(value);
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
    public ErrorCode remove(Value value) throws SQLException {
        int result = 0;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return lastError;
        }

        String sql = "delete from "
                + tableName
                + " where time="
                + DataTimeCvt.DateToStr(value.getDate());

        pstmt = conn.prepareStatement(sql);
        result = pstmt.executeUpdate(sql);

        lastError = 1 == result ? ErrorCode.E_OK : ErrorCode.E_FAIL;

        return lastError;
    }

    @Override
    public Vector<Value> query(Value value) throws SQLException {
        Vector<Value> vecValue = null;

        lastError = ErrorCode.E_FAIL;

        if (!isTableExist()) {
            lastError = ErrorCode.E_TABLE_NOT_EXIST;
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + "表" + tableName + "不存在");

            return vecValue;
        }

        String sql = getQuerySql(value);
        if (null == sql) {
            lastError = ErrorCode.E_OK;
            return vecValue;
        }

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery(sql);

        vecValue = new Vector<Value>();

        while (rs.next()) {
        	Value valueRS = new Value();

        	valueRS.setDate(DataTimeCvt.StrToDate(rs.getString(1)));
        	valueRS.setValue(rs.getString(2));

        	vecValue.add(valueRS);
        }

        lastError = vecValue.isEmpty() ? ErrorCode.E_FAIL : ErrorCode.E_OK;

        return vecValue;
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

    private String getUpdateSql(Value value) {
    	return "update " + tableName + " value=" + value.getValue() + " where time=" + value.getDate();
    }

    private String getQuerySql(Value value) {
    	if (ValueOpt.O_NULL.get() == value.getOpt()) {
            return null;
        }
    	
    	if (ValueOpt.O_LASTREC.get() == value.getOpt()) {
            return "select * from " + tableName + " where time=(select max(time) from " + tableName + ")";
        }
    	
    	if (ValueOpt.O_ONEDAYRECS.get() == value.getOpt()) {
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        	String dateString = formatter.format(value.getDate());

            return "select * from " + tableName + " where date_format(time, '%Y-%m-%d')='" + dateString + "'";
        }
    	
    	return null;
    }

    @Override
    public void destroy() throws SQLException {
        conn.close();
    }
}
