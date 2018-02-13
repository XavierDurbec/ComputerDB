package com.excilys.xdurbec.formation.computerDataBase.dao;


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
		
		
		//TODO: A faire avec une requête SQL
		@Override
		public Computer get(int id) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeQuery("SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = "+id+";"); 
			ResultSet rs = stat.getResultSet();
			rs.next();
			String name = rs.getString("name");
			Date introduced = rs.getDate("introduced");
			Date discontinued = rs.getDate("discontinued");
			int idT = rs.getInt("id");
			int idComp= rs.getInt("company_id");

			System.out.println("IdCOM :"+idComp);
			con.close();
			
			Company company = null;
			
			if(idComp != 0) {
				company = CompanyDAO.getCompanyDAO().get(idComp);
			}
			
			Computer computer = new Computer(name, introduced, discontinued, company);
			computer.setId(idT);
			return computer;
		}
		
		
		
		@Override
		public List<Computer> getAll() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void create(Computer entity) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) VALUES(\""+entity.getName()+"\",);");
			con.close();			
		}
		
		@Override
		public void set(Computer entity) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void delete(int id) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		
}
