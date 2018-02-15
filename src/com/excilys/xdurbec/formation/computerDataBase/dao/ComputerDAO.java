package com.excilys.xdurbec.formation.computerDataBase.dao;


import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

public class ComputerDAO implements EntityDAO<Computer>{
	private static final String GET_BY_ID_REQUEST = "SELECT computer.id, computer.name, computer.introduced,"
			+ " computer.discontinued, computer.company_id, company.id, company.name "
			+ "FROM computer LEFT JOIN company "
			+ "ON computer.company_id = company.id;"
			+ "WHERE computer.id = ? ;";
	private static final String GET_ALL_REQUEST = "SELECT computer.id, computer.name, computer.introduced, "
			+ "computer.discontinued, computer.company_id, company.id, company.name "
			+ "FROM computer LEFT JOIN company "
			+ "ON computer.company_id = company.id;";
	private static final String DELETE_REQUEST = "DELETE FROM computer WHERE id =?;";
			
	private static ComputerDAO computerDAO;

	private ConnectionManager cm;

	private ComputerDAO(ConnectionManager cm) {
		this.cm = cm;
	}

	public static ComputerDAO getComputerDAO() {
		if(computerDAO == null) {
			computerDAO = new ComputerDAO(ConnectionManager.getCM());
		}
		return computerDAO;
	}


	public Computer getById(int id) throws SQLException {
		Connection con = cm.getConnection();
		PreparedStatement stat = con.prepareStatement(GET_BY_ID_REQUEST);
		stat.setInt(1, id);
		stat.executeQuery(); 
		
		ResultSet rs = stat.getResultSet();
		rs.next();
		Company company = new Company();

		if(rs.getInt("company.id") != 0) {
			company.setName(rs.getString("company.name"));
			company.setId(rs.getInt("company.id"));
		}	

		Computer computer = new Computer(rs.getString("computer.name"), rs.getDate("computer.introduced"), rs.getDate("computer.discontinued"),company);
		computer.setId(rs.getInt("computer.id"));

		con.close();

		return computer;
	}



	@Override
	public List<Computer> getAll() throws SQLException {
		Connection con = cm.getConnection();
		Statement stat = con.createStatement();

		stat.executeQuery("SELECT computer.id, computer.name, computer.introduced, "
				+ "computer.discontinued, computer.company_id, company.id, company.name "
				+ "FROM computer LEFT JOIN company "
				+ "ON computer.company_id = company.id;"); 

		ResultSet rs = stat.getResultSet();

		List<Computer> listComputer = new ArrayList<Computer>();

		while(rs.next()) {
			Company company = new Company();
			Computer computer =  new Computer();
			if(rs.getInt("computer.company_id") == 0) {
				company.setId(rs.getInt("company.id"));
				company.setName(rs.getString("company.name"));
			}
			else {
				company.setId(0);
				company.setName(null);
			}

			computer.setId(rs.getInt("computer.id"));
			computer.setIntroduced(rs.getDate("computer.introduced"));
			computer.setDiscontinued(rs.getDate("computer.discontinued"));
			computer.setCompany(company);

			listComputer.add(computer);
		}

		return listComputer;
	}

	
	public void create(Computer entity) throws SQLException {
		Connection con = cm.getConnection();
		Statement stat = con.createStatement();
		stat.executeUpdate("INSERT INTO computer (name, introduced, discontinued, company_id) "
				+ "VALUES(\""+entity.getName()+"\",\""
				+entity.getIntroduced()+"\",\""
				+entity.getDiscontinued()+"\",\""
				+entity.getCompany().getId()+"\");");
		con.close();			
	}

	
	public void update(Computer entity) throws SQLException {
		Connection con = cm.getConnection();
		Statement stat = con.createStatement();
		stat.executeUpdate("UPDATE computer SET name =\""+entity.getName()+"\","
				+ " introduced =\""+entity.getIntroduced()+"\","
				+ " discontinued=\""+entity.getDiscontinued() +"\","
				+ " company_id=\""+entity.getCompany().getId() +"\" "
				+ "WHERE id ="+entity.getId()+";");
		con.close();

	}
	
	public void deleteById(int id) throws SQLException {
		Connection con = cm.getConnection();
		PreparedStatement stat = con.prepareStatement(DELETE_REQUEST);
		stat.setInt(1, id);
		stat.executeUpdate();
		con.close();

	}

}
