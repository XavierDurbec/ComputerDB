package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.util.List;
import java.sql.*;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;

public class CompanyDAO implements EntityDAO<Company> {
		private static CompanyDAO companyDAO;
		
		private  ConnectionManager cm;

		private CompanyDAO(ConnectionManager cm) {
			this.cm = cm;
		}
		
		public static CompanyDAO getCompanyDAO(String url, String user, String passeWord) {
			if(companyDAO == null) {
				companyDAO = new CompanyDAO(ConnectionManager.getCM());
			}
			return companyDAO;
			
		}

		
		
		
		@Override
		public Company get(int id) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeQuery("SELECT id, name FROM company WHERE id = "+id+";"); 
			ResultSet rs = stat.getResultSet();
			rs.next();
			Company c = new Company( rs.getString("name") );
			c.setId(rs.getInt("id"));
			con.close();
			return c;
		}

		@Override
		public List<Company> getAll()  throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void create(Company entity)  throws SQLException{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void set(Company entity)  throws SQLException{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void delete(Company entity)  throws SQLException{
			// TODO Auto-generated method stub
			
		}
		
		
		
}
