package com.dataonline.intfc;

import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.pojo.*;

public interface IMaintenance {
    public ErrorCode userAdd(User user);
    
    public ErrorCode userModify(User user);
    
    public ErrorCode userRemove(User user);
    
    public Vector<User> userQuery(User user);
    
    public ErrorCode typeAdd(Type type);

    public ErrorCode typeModify(Type type);

    public ErrorCode typeRemove(Type type);
    
    public Vector<Type> typeQuery(Type type);
    
    public Vector<Value> valueQuery(int userID, Value value);
}
