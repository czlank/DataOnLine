package com.dataonline.intfc;

import java.sql.SQLException;
import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.pojo.User;

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
