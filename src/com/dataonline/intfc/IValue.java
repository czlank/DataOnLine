package com.dataonline.intfc;

import java.sql.SQLException;
import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.pojo.Value;

public interface IValue {
    public ErrorCode create() throws SQLException;

    public ErrorCode drop() throws SQLException;

    public ErrorCode add(Value value) throws SQLException;

    public ErrorCode update(Value value) throws SQLException;

    public ErrorCode remove(Value value) throws SQLException;

    public Vector<Value> query(Value value) throws SQLException;

    public String getLastError();
    
    public void destroy() throws SQLException;
}
