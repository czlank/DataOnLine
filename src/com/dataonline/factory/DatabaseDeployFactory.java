package com.fota.factory.business;

import com.fota.intfc.business.IDatabaseDeploy;
import com.fota.service.business.DatabaseDeployService;
import org.apache.log4j.Logger;
import com.fota.util.common.LineNo;

public class DatabaseDeployFactory {
	private static Logger log = Logger.getLogger(DatabaseDeployFactory.class);
	
	public static IDatabaseDeploy getDatabaseConfig(String databaseName, String userName, String password) {
	    IDatabaseDeploy deploy = null;
		
		try {
		    deploy = new DatabaseDeployService(databaseName, userName, password);
		} catch (Exception e) {
			log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + e.getMessage());
		}
		
		return deploy;
	}
}
