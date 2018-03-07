package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariProxyConnection;

public class ConnectionManager {

	private static ConnectionManager cm;

	private String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db?useSSL=false";
	private String user = "admincdb";
	private String passeWord = "qwerty1234";
	private HikariDataSource ds;
	
	private ConnectionManager() {
		
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(user);
		config.setPassword(passeWord);
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.setMaximumPoolSize(250);
		config.setMinimumIdle(5);
		ds = new HikariDataSource(config);
	}

	public static ConnectionManager getCM() {
		if (cm == null) {
			cm = new ConnectionManager();
			
		}
		return cm;

	}

	public Connection getConnection() throws ExceptionDAO {
		try {
			HikariProxyConnection con = (HikariProxyConnection) ds.getConnection();
			return con;
		} catch (SQLException e) {
			throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
		}
	}



}
