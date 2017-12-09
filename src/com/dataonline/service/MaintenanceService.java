package com.dataonline.service;

import java.util.Vector;

import com.dataonline.util.ErrorCode;
import com.dataonline.intfc.IMaintenance;
import com.dataonline.impl.MaintenanceImpl;
import com.dataonline.manager.BusinessSyncManager;
import com.dataonline.pojo.*;

public class MaintenanceService implements IMaintenance {
    private MaintenanceImpl maintenance = null;

    public MaintenanceService() {
        maintenance = new MaintenanceImpl();
    }
    
    @Override
    public ErrorCode userAdd(User user) {
        BusinessSyncManager.getInstance().rwLock();
        
        ErrorCode rc = maintenance.userAdd(user);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return rc;
    }
    
    @Override
    public ErrorCode userModify(User user) {
        BusinessSyncManager.getInstance().rwLock();
        
        ErrorCode rc = maintenance.userModify(user);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return rc;
    }
    
    @Override
    public ErrorCode userRemove(User user) {
        BusinessSyncManager.getInstance().rwLock();
        
        ErrorCode rc = maintenance.userRemove(user);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return rc;
    }
    
    @Override
    public Vector<User> userQuery(User user) {
        BusinessSyncManager.getInstance().rwLock();
        
        Vector<User> vecUser = maintenance.userQuery(user);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return vecUser;
    }

    @Override
    public ErrorCode typeAdd(Type type) {
        BusinessSyncManager.getInstance().rwLock();
        ErrorCode rc = maintenance.typeAdd(type);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return rc;
    }

    @Override
    public ErrorCode typeModify(Type type) {
        BusinessSyncManager.getInstance().rwLock();
        
        ErrorCode rc = maintenance.typeModify(type);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return rc;
    }

    @Override
    public ErrorCode typeRemove(Type type) {
        BusinessSyncManager.getInstance().rwLock();
        
        ErrorCode rc = maintenance.typeRemove(type);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return rc;
    }
    
    @Override
    public Vector<Type> typeQuery(Type type) {
        BusinessSyncManager.getInstance().rwLock();
        
        Vector<Type> vecType = maintenance.typeQuery(type);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return vecType;
    }
    
    @Override
    public Vector<Value> valueQuery(int userID, Value value) {
    	BusinessSyncManager.getInstance().rwLock();
        
        Vector<Value> vecValue = maintenance.valueQuery(userID, value);
        
        BusinessSyncManager.getInstance().rwUnLock();
        
        return vecValue;
    }
}
