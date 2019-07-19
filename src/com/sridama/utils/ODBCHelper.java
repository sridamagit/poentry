package com.sridama.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class ODBCHelper {
	
	private static Properties prop = null;
	private static  String dbUrl ="";
	private static final String username = "";
	private static final String password = "";
	private static final String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
	public static String salesLedger = "";
	

	public static Connection getConnection() throws Exception {
		Class.forName(driver);
		Connection conn = null;

					prop = new Properties();
			InputStream inputStream = ODBCHelper.class.getClassLoader()
					.getResourceAsStream("ini.properties");
			prop.load(inputStream);
			salesLedger = prop.getProperty("salesledger");
			dbUrl = prop.getProperty("odbcdriver");
			System.out.println(salesLedger + " && "+dbUrl);
			conn = DriverManager.getConnection(dbUrl, username, password);
		
		return conn;
	}
	public static void main(String args[]) throws Exception{
		Connection conn = getConnection();
	}
}
