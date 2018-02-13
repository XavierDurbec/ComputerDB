package com.excilys.xdurbec.formation.computerDataBase.dao;
import java.sql.*;

public class ConnectionManager {
	
	private static ConnectionManager cm;
	
	private String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db";
	private String user= "admincdb";
	private String passeWord ="qwerty1234";
	
	
	private ConnectionManager() {}
	
	public static ConnectionManager getCM() {
		if(cm == null) {
			cm = new ConnectionManager();
		}
		return cm;

	}
	
	public Connection getConnection() throws SQLException {
		
		Connection con = DriverManager.getConnection(cm.url, cm.user, cm.passeWord); 
		return con;
	}
	
	
	
}
