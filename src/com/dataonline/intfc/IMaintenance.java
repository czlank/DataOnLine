package com.dataonline.intfc;

import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.pojo.User;
import com.dataonline.pojo.Type;

public interface IMaintenance {
    public ErrorCode userAdd(User user);
    
    public ErrorCode userModify(User user);
    
    public ErrorCode userRemove(User user);
    
    public Vector<User> userQuery(User user);
    
    public ErrorCode typeAdd(Type type);

    public ErrorCode typeModify(Type type);

    public ErrorCode typeRemove(Type type);
    
    public ErrorCode typeRemove(User user);
    
    public Vector<Type> projectQuery(Type type);
}
