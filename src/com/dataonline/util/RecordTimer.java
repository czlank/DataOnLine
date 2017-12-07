package com.fota.util.common;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.fota.factory.business.MaintenanceFactory;
import com.fota.util.error.ErrorCode;
import com.fota.util.error.GetLastError;

public class RecordTimer extends TimerTask {
    private Logger log = Logger.getLogger(RecordTimer.class);
    
    @Override
    public void run() {
        ErrorCode rc = MaintenanceFactory.getInstance().getMaintenance().recordRemove();
        
        if (rc != ErrorCode.E_OK) {
            log.error(LineNo.getFileName() + ":L" + LineNo.getLineNumber() + " - " + GetLastError.instance().getErrorMsg(rc));
        }
    }

}
