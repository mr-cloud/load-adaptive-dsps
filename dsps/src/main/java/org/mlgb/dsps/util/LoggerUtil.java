package org.mlgb.dsps.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import uni.akilis.helper.LoggerX;


public class LoggerUtil {
	private static Logger logger = null;
	private static final String TAG = LoggerUtil.class.getName();
	
	public static Logger getLogger(){
		if(logger == null){
			logger = Logger.getLogger(Consts.LOGGER_NAME);
			File logDir = new File(Consts.LOG_DIR);
			if(!logDir.exists()){
				LoggerX.println(TAG, "log dir not exists! now create it!");
				if (!logDir.mkdirs()) {
					LoggerX.println(TAG, "cannot makedirs for logging!");
					System.exit(1);
				}
			}
			FileHandler handler;
			try {
				handler = new FileHandler(Consts.LOG_DIR + Consts.LOG_FILE_NAME, 10000000, 10, true);
				logger.addHandler(handler);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
				LoggerX.println(TAG, "cannot apply a file handler for logger");
				System.exit(1);
			}
		}
		return logger;
		
	}

}
