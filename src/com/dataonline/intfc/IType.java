package com.dataonline.intfc;

import java.sql.SQLException;
import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.pojo.Type;

public interface IType {
    public ErrorCode create() throws SQLException;

    public ErrorCode drop() throws SQLException;

    public ErrorCode add(Type type) throws SQLException;

    public ErrorCode update(Type type) throws SQLException;

    public ErrorCode remove(Type type) throws SQLException;

    public Vector<Type> query(Type type) throws SQLException;

    public String getLastError();
    
    public void destroy() throws SQLException;
}
