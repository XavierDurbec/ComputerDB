package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;


/**
 * Crude gestion of Company.
 * @author excilys
 *
 */
public class CompanyDAO implements EntityDAO<Company> {
		private static CompanyDAO companyDAO;
		
		private  ConnectionManager cm;

		private CompanyDAO(ConnectionManager cm) {
			this.cm = cm;
		}
		
		public static CompanyDAO getCompanyDAO() {
			if(companyDAO == null) {
				companyDAO = new CompanyDAO(ConnectionManager.getCM());
			}
			return companyDAO;
			
		}
		
		
		
		
		
		@Override
		public Company getById(int id) throws SQLException {
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeQuery("SELECT id, name FROM company WHERE id = "+id+";"); 
			ResultSet rs = stat.getResultSet();
			rs.next();
			Company company = new Company( rs.getString("name") );
			company.setId(rs.getInt("id"));
			con.close();
			return company;
		}

		@Override
		public List<Company> getAll()  throws SQLException {
			
			List<Company> companyList = new ArrayList<Company>();
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeQuery("SELECT id, name FROM company;"); 
			ResultSet rs = stat.getResultSet();
			
			
			while(rs.next()) {
				Company c = new Company(rs.getString("name"));
				c.setId(rs.getInt("id"));
				companyList.add(c);
			}
						
			con.close();
			return companyList;
		}

		@Override
		public void create(Company entity)  throws SQLException{
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeUpdate("INSERT INTO company (name) VALUES(\""+entity.getName()+"\");");
			con.close();
		}

		@Override
		public void set(Company entity)  throws SQLException{
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeUpdate("UPDATE company SET name =\""+entity.getName()+"\" WHERE id ="+entity.getId()+";");
			con.close();			
		}

		@Override
		public void deleteById(int id)  throws SQLException{
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeUpdate("DELETE FROM company WHERE id ="+id+";");
			con.close();
			
		}
		
		
		
}
