package com.excilys.xdurbec.formation.computerDataBase.dao;


import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

public class ComputerDAO implements EntityDAO<Computer>{
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
		
		

		// TODO: Add gestion of null company
		@Override
		public Computer getById(int id) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeQuery("SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id = "+id+";"); 
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
	
			stat.executeQuery("SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id;"); 

			ResultSet rs = stat.getResultSet();
			
			List<Computer> lcp = new ArrayList<Computer>();


			
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
				
				lcp.add(computer);
			}
			
			return lcp;
		}
		
		@Override
		public void create(Computer entity) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeUpdate("INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(\""+entity.getName()+"\",\""+entity.getIntroduced()+"\",\""+entity.getDiscontinued()+"\",\""+entity.getCompany().getId()+"\");");
			con.close();			
		}
		
		@Override
		public void update(Computer entity) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeUpdate("UPDATE computer SET name =\""+entity.getName()+"\", introduced =\""+entity.getIntroduced()+"\", discontinued=\""+entity.getDiscontinued() +"\", company_id=\""+entity.getCompany().getId() +"\" WHERE id ="+entity.getId()+";");
			con.close();
			
		}
		@Override
		public void deleteById(int id) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeUpdate("DELETE FROM computer WHERE id ="+id+";");
			con.close();
			
		}
		
}
