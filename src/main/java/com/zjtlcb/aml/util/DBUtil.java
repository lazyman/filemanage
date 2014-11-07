package com.zjtlcb.aml.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据库工具类
 * @author dch
 *
 */
public class DBUtil {
	private static Log logger = LogFactory.getLog(DBUtil.class);
	private static DBUtil util;
	
	// 数据库连接信息
	/**
	 * 数据库驱动
	 */
	private String driver;
	/**
	 * 数据库URL
	 */
	private String url;
	/**
	 * 数据库用户名
	 */
	private String username;
	/**
	 * 数据库密码
	 */
	private String password;

	public static DBUtil getInstance() {
		if(util == null) {
			util = new DBUtil();
		}
		
		return util;
	}
	public static DBUtil getInstance(String dbdriver, String dburl, String dbusername, String dbpassword) {
		if(util == null) {
			util = new DBUtil();
		}
		
		util.setDriver(dbdriver);
		util.setUrl(dburl);
		util.setUsername(dbusername);
		
		return util;
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.warn("载入数据库驱动失败", e);
			util = null;
		}
		Connection connection = DriverManager.getConnection(url, username, password);
		
		return connection;
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn 数据库连接
	 * @param prestmt SQL语句
	 */
	public void closeConnection(Connection conn, PreparedStatement prestmt) {
		try {
			if(prestmt != null) {
				prestmt.close();
			}
			
			if(conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			// 含数据库异常和NullPoint异常
			logger.error("关闭连接错误", e);
		}
	}
	
	/**
	 * 执行sql，只能传入string参数，不限个数
	 * @param sql 要执行的sql语句
	 * @param params 参数列表，类型为string，不限个数
	 * @throws SQLException 
	 */
	public void executeByStrParams(Connection conn, String sql, List<String> params) throws SQLException {
		PreparedStatement prestmt = conn.prepareStatement(sql);
		int size = params.size();
		for(int parameterIndex=1; parameterIndex< size; parameterIndex++) {
			prestmt.setString(parameterIndex, params.get(parameterIndex));
		}
		prestmt.execute();
		
		prestmt.close();
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
