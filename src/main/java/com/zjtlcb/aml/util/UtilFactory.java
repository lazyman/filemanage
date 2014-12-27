package com.zjtlcb.aml.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 实例工厂，存放各种工具类，及系统配置信息。
 * @author dch
 *
 */
public class UtilFactory {
	private static Log logger = LogFactory.getLog(UtilFactory.class);
	
	private static DBUtil dbUtil;
	private static UtilFactory factory;
	private static Properties config = new Properties();
	private static String propfile = "/config.properties";
	
	/**
	 * 工厂实例
	 * @return
	 */
	public static UtilFactory getInstance() {
		if(factory == null) {
			factory = new UtilFactory();
		}
		
		return factory;
	}
	
	private UtilFactory() {
		try {
			logger.debug("加载配置参数");
			config.load(this.getClass().getResourceAsStream( propfile ));
			
			logger.debug("测试数据库工具类");
			getDbUtil();
			
		} catch (IOException e) {
			logger.error("测试失败，请检查配置参数");
			logger.error(propfile, e);
		}
	}
	
	/**
	 * 获取数据库工具类(单实例)
	 * @return
	 */
	public DBUtil getDbUtil() {
		logger.debug("获取数据库工具类");
		
		if(dbUtil == null) {
			logger.debug("首次获取");
			String dbdriver = config.getProperty("db.driver");
			String dburl = config.getProperty("db.url");
			String dbusername = config.getProperty("db.username");
			String dbpassword = config.getProperty("db.password");
			
			logger.debug(dbdriver);
			logger.debug(dburl);
			logger.debug(dbusername);
			logger.debug(dbpassword);
			
			dbUtil = getDbUtil(dbdriver, dburl, dbusername, dbpassword);
		}
		
		return dbUtil;
	}
	
	/**
	 * 获取数据库工具类（带参数）
	 * @param dbdriver
	 * @param dburl
	 * @param dbusername
	 * @param dbpassword
	 * @return
	 */
	public DBUtil getDbUtil(String dbdriver, String dburl, String dbusername, String dbpassword) {
		logger.debug("获取数据库工具类（带参数）");
		dbUtil = new DBUtil();
		
		dbUtil.setDriver(dbdriver);
		dbUtil.setUrl(dburl);
		dbUtil.setUsername(dbusername);
		dbUtil.setPassword(dbpassword);
		
		return dbUtil;
	}

	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		UtilFactory.config = config;
	}

	/**
	 * 获取数据库连接
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		logger.debug("获取数据库连接");
		return getDbUtil().getConnection();
	}
}
