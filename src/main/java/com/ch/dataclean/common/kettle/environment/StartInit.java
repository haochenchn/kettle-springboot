package com.whhx.dataclean.common.kettle.environment;

import org.springframework.beans.factory.InitializingBean;

/**
 * 初始化环境
 */
public class StartInit implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		KettleEnv.init();

	}

}
