package com.fota.intfc.base;

import java.sql.SQLException;
import java.util.Vector;

import com.fota.util.error.ErrorCode;
import com.fota.pojo.base.User;

public interface IUser {
    public ErrorCode create() throws SQLException;

    public ErrorCode drop() throws SQLException;

    public ErrorCode add(User user) throws SQLException;

    public ErrorCode update(User user) throws SQLException;

    public ErrorCode remove(User user) throws SQLException;

    public Vector<User> query(User user) throws SQLException;

    public String getLastError();
    
    public void destroy() throws SQLException;
}
