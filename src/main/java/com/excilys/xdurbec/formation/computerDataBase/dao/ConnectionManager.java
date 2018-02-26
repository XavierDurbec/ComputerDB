package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static ConnectionManager cm;

	private String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db?useSSL=false";
	private String user = "admincdb";
	private String passeWord = "qwerty1234";


	private ConnectionManager() { }

	public static ConnectionManager getCM() {
		if (cm == null) {
			cm = new ConnectionManager();
		}
		return cm;

	}

	public Connection getConnection() throws ExceptionDAO {
		try {
			//TODO : Class.forName()
			Connection con = DriverManager.getConnection(cm.url, cm.user, cm.passeWord); 
			return con;
		} catch (SQLException e) {
			throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
		}
	}



}
