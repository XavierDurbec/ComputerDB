package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.ConstantString;

/**
 * Crude gestion of Company.
 * @author excilys
 *
 */
public class CompanyDAO implements EntityDAO<Company> {
		private static final String GET_ALL=("SELECT id, name FROM company;");
		
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
		public List<Company> getAll()  throws SQLException {
			
			List<Company> companyList = new ArrayList<Company>();
			Connection con = cm.getConnection();
			Statement stat = con.createStatement();
			stat.executeQuery(GET_ALL); 
			ResultSet rs = stat.getResultSet();
			
			
			while(rs.next()) {
				Company c = new Company(rs.getString(ConstantString.NAME));
				c.setId(rs.getInt(ConstantString.ID));
				companyList.add(c);
			}
						
			con.close();
			return companyList;
		}

		
		
		
}
