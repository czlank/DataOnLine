package com.dataonline.intfc;

import java.sql.SQLException;
import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.pojo.Node;

public interface INode {
    public ErrorCode create() throws SQLException;

    public ErrorCode drop() throws SQLException;

    public ErrorCode add(Node node) throws SQLException;

    public ErrorCode update(Node node) throws SQLException;

    public ErrorCode remove(Node node) throws SQLException;

    public Vector<Node> query(Node node) throws SQLException;

    public String getLastError();
    
    public void destroy() throws SQLException;
}
