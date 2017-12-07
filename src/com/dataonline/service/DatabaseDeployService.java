package com.fota.service.business;

import com.fota.db.Connector;
import com.fota.impl.business.DatabaseDeployImpl;
import com.fota.intfc.business.IDatabaseDeploy;

public class DatabaseDeployService implements IDatabaseDeploy {
	private Connector dbc = null;
	private IDatabaseDeploy intfc = null;
	
	public DatabaseDeployService(String databaseName, String user, String password) throws Exception {
		dbc = new Connector("mysql", user, password);
		intfc = new DatabaseDeployImpl(dbc.getConnection(), databaseName);
	}
	
	@Override
	public boolean config(String userName, String password, String dbName) {
		return intfc.config(userName, password, dbName);
	}
}
