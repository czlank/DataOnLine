package com.dataonline.manager;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BusinessSyncManager {
    private static BusinessSyncManager manager = new BusinessSyncManager();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();
    
    private BusinessSyncManager() {
        
    }

    public static BusinessSyncManager getInstance() {
        return manager;
    }
    
    public void rLock() {
        readLock.lock();
    }
    
    public void rUnLock() {
        readLock.unlock();
    }
    
    public void wLock() {
        writeLock.lock();
    }
    
    public void wUnLock() {
        writeLock.unlock();
    }
    
    public void rwLock() {
        writeLock.lock();
        readLock.lock();
    }
    
    public void rwUnLock() {
        writeLock.unlock();
        readLock.unlock();
    }
}
