package com.dataonline.factory;

import com.dataonline.intfc.IMaintenance;
import com.dataonline.service.MaintenanceService;

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
