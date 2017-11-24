package com.fjflPic;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PlatUtil {

	private static PlatUtil instance;
	private Properties SixInOneProperties;

	private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>();

	// 重启加载配制
	public static void reLoad() {
		PlatUtil.instance = new PlatUtil();
	}

	/* 私有构造方法-读取配置信息 */
	private PlatUtil() {

		// 加载6in1.properties
		try {
			this.SixInOneProperties = new Properties();
			InputStream in = PlatUtil.class
					.getResourceAsStream("/6in1.properties");
			System.out.println("in="+in);
			this.SixInOneProperties.load(in);
			in.close();
//			LogUtil.info("已加载6in1.properties");
		} catch (Exception e) {
//			LogUtil.error("加载6in1.properties出错", e);
		}
	}

	public static void setContext(String key, Object value) {
		Map<String, Object> contextMap = PlatUtil.context.get();
		if (contextMap == null) {
			contextMap = new HashMap<String, Object>();
			PlatUtil.context.set(contextMap);
		}
		contextMap.put(key, value);
	}

	public static Object getContext(String key) {
		Map<String, Object> contextMap = PlatUtil.context.get();
		if (contextMap == null) {
			contextMap = new HashMap<String, Object>();
		}
		return contextMap.get(key);
	}

	public static void clearContext() {
		PlatUtil.context.remove();
	}

	public static String get6in1Value(String key) {

		if (PlatUtil.instance == null) {
			PlatUtil.instance = new PlatUtil();
		}
		return PlatUtil.instance.SixInOneProperties.getProperty(key);
	}

}
