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
	private static final String GET_ALL = "SELECT id, name FROM company;";
	private static final String DOES_COMPANY_EXIST = "SELECT count(*) FROM company WHERE id = ?;";
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
	public List<Company>  getAll()  throws  ExceptionDAO {
		List<Company> companyList = new ArrayList<Company>();
		Connection con = cm.getConnection();

		try(Statement stat = con.createStatement()){
			stat.executeQuery(GET_ALL); 
			ResultSet rs = stat.getResultSet();

			while(rs.next()) {
				Company c = new Company(rs.getString(ConstantString.NAME));
				c.setId(rs.getInt(ConstantString.ID));
				companyList.add(c);
			}

			return companyList;
		}
		catch(SQLException e) {
			throw new ExceptionDAO(ExceptionDAO.STATEMENT_ERROR);
		}
		finally {
			try {
				con.close();
			}
			catch(SQLException e) {
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}


	public Boolean doesExist(int id) throws  ExceptionDAO {
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(DOES_COMPANY_EXIST)){
			stat.setInt(1, id);
			stat.executeQuery(); 
			ResultSet rs = stat.getResultSet();
			rs.next();
			if(rs.getInt("count(*)") == 0) {
				return false;
			}
			else {
				return true;
			}
		}
		catch(SQLException e) {
			throw new ExceptionDAO(ExceptionDAO.DOES_EXIST_ERROR);
		}
		finally{
			try {
				con.close();
			}
			catch(SQLException e) {
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}

	}
}
