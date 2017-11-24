package com.fjflPic;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OracleJdbcUtil {

	private static OracleJdbcUtil instance;
	private Properties JdbcProperties;

	private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>();

	// 重启加载配制
	public static void reLoad() {
		OracleJdbcUtil.instance = new OracleJdbcUtil();
	}

	/* 私有构造方法-读取配置信息 */
	private OracleJdbcUtil() {

		// 加载6in1.properties
		try {
			this.JdbcProperties = new Properties();
			InputStream in = OracleJdbcUtil.class
					.getResourceAsStream("/jdbc.properties");
			System.out.println("in="+in);
			this.JdbcProperties.load(in);
			in.close();
//			LogUtil.info("已加载6in1.properties");
		} catch (Exception e) {
//			LogUtil.error("加载6in1.properties出错", e);
		}
	}

	public static void setContext(String key, Object value) {
		Map<String, Object> contextMap = OracleJdbcUtil.context.get();
		if (contextMap == null) {
			contextMap = new HashMap<String, Object>();
			OracleJdbcUtil.context.set(contextMap);
		}
		contextMap.put(key, value);
	}

	public static Object getContext(String key) {
		Map<String, Object> contextMap = OracleJdbcUtil.context.get();
		if (contextMap == null) {
			contextMap = new HashMap<String, Object>();
		}
		return contextMap.get(key);
	}

	public static void clearContext() {
		OracleJdbcUtil.context.remove();
	}

	public static String getJdbcValue(String key) {

		if (OracleJdbcUtil.instance == null) {
			OracleJdbcUtil.instance = new OracleJdbcUtil();
		}
		return OracleJdbcUtil.instance.JdbcProperties.getProperty(key);
	}

}
