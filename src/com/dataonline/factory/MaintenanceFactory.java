package com.fota.factory.business;

import com.fota.intfc.business.IMaintenance;
import com.fota.service.business.MaintenanceService;

public class MaintenanceFactory {
    private static final MaintenanceFactory maintenanceFactory = new MaintenanceFactory();

    private MaintenanceFactory() {

    }

    public static MaintenanceFactory getInstance() {
        return maintenanceFactory;
    }

    public IMaintenance getMaintenance() {
        return new MaintenanceService();
    }
}
