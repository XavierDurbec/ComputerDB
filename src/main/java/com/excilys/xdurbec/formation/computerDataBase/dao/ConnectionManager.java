package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionManager {

	private static ConnectionManager cm;

	/*private String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db?useSSL=false";
	private String user = "admincdb";
	private String passeWord = "qwerty1234";
	*/
	private String url ;
	private String user;
	private String passeWord ;


	private ConnectionManager() {
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		this.url = bundle.getString("sgbd.url");
		this.user = bundle.getString("sgbd.user");
		this.passeWord = bundle.getString("sgbd.pass");
	}

	public static ConnectionManager getCM() {
		if (cm == null) {
			cm = new ConnectionManager();
		}
		return cm;

	}

	public Connection getConnection() throws ExceptionDAO {
		try {
			Connection con = DriverManager.getConnection(cm.url, cm.user, cm.passeWord); 
			return con;
		} catch (SQLException e) {
			throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
		}
	}

	@Override
	public String toString() {
		return "ConnectionManager [url=" + url + ", user=" + user + ", passeWord=" + passeWord + "]";
	}
	
	



}
