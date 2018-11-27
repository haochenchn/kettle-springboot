package com.ch.dataclean.common.kettle.environment;

import org.apache.log4j.Logger;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.util.EnvUtil;

/**
 * Kettle环境初始化.
 */
public class KettleEnv {
	public static Logger logger = Logger.getLogger(KettleEnv.class);
	public static void init(){
		try {
			KettleEnvironment.init();
			EnvUtil.environmentInit();
			logger.info("Kettle环境初始化成功");
		}catch (Exception e){
			e.printStackTrace();
			logger.error("Kettle环境初始化失败");
		}

	}
}