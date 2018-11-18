package com.whhx.dataclean.common.kettle.environment;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.util.EnvUtil;

/**
 * Kettle环境初始化.
 */
public class KettleEnv {

	public static void init(){
		try {
			KettleEnvironment.init();
			EnvUtil.environmentInit();

		}catch (Exception e){
			e.printStackTrace();
		}

	}
}