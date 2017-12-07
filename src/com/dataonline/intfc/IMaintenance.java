package com.fota.intfc.business;

import java.util.Vector;

import com.fota.util.error.ErrorCode;
import com.fota.pojo.base.Project;
import com.fota.pojo.base.Record;
import com.fota.pojo.base.User;
import com.fota.pojo.base.Version;

public interface IMaintenance {
    public ErrorCode projectAdd(Project project);

    public ErrorCode projectModify(Project project);

    public ErrorCode projectRemove(Project project);
    
    public ErrorCode projectRemove(User user);
    
    public Vector<Project> projectQuery(Project project);
    
    public ErrorCode versionAdd(Version version);

    public ErrorCode versionModify(Version version);

    public ErrorCode versionRemove(Version version);
    
    public ErrorCode versionRemove(User user);
    
    public Vector<Version> versionQuery(Version version);
    
    public boolean versionIsUnique(Version version);
    
    public ErrorCode userAdd(User user);
    
    public ErrorCode userModify(User user);
    
    public ErrorCode userRemove(User user);
    
    public Vector<User> userQuery(User user);
    
    public Vector<Record> recordQuery(Record record, String beginTime, String endTime);
    
    public ErrorCode recordRemove();
}
