package com.whhx.dataclean.common.util;

import org.pentaho.di.core.Const;

/**
 * 
 * @ClassName Constant 
 * @Description 常量类
 */
public class Constant extends Const {

	/** Public */
	public static final String VERSION = "7.0.0.0-25";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String SESSION_ID = "SESSION_ID";


	/*public static Properties props;

	static {
		props = readProperties();
		KETTLE_HOME = uKettle() + props.getProperty("kettle.home");
		KETTLE_PLUGIN = KETTLE_HOME + FILE_SEPARATOR
				+ props.getProperty("kettle.plugin");
		KETTLE_SCRIPT = uKettle()
				+ props.getProperty("kettle.script");
		KETTLE_LOGLEVEL = logger(props
				.getProperty("kettle.loglevel"));

	}

	public static String get(String key) {
		return props.getProperty(key);
	}

	public static void set(Properties p) {
		props = p;
	}

	public static Properties readProperties() {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(Constant.class.getResource("/")
					.getPath().replace("%20", " ")
					+ UKETTLE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}*/



}