package com.dataonline.intfc;

import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.pojo.*;

public interface IMaintenance {
    public ErrorCode userAdd(User user);
    
    public ErrorCode userModify(User user);
    
    public ErrorCode userRemove(User user);
    
    public Vector<User> userQuery(User user);
    
    public ErrorCode nodeAdd(int userID, Node node);

    public ErrorCode nodeModify(int userID, Node node);

    public ErrorCode nodeRemove(int userID, Node node);
    
    public Vector<Node> nodeQuery(int userID, Node node);
    
    public ErrorCode typeAdd(Type type);

    public ErrorCode typeModify(Type type);

    public ErrorCode typeRemove(Type type);
    
    public Vector<Type> typeQuery(Type type);
    
    public Vector<Value> valueQuery(int userID, Value value);
}
