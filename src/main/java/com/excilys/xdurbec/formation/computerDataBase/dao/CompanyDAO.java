package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import  com.excilys.xdurbec.formation.computerDataBase.model.Company;
import  com.excilys.xdurbec.formation.computerDataBase.model.ConstantString;

/**
 * Crude gestion of Company.
 * @author excilys
 *
 */
public class CompanyDAO extends EntityDAO implements EntityDAOComportment<Company> {

	private static final String GET_ALL = "SELECT id, name FROM company;";
	private static final String GET_ALL_PAGE = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
	private static final String DOES_COMPANY_EXIST = "SELECT count(*) FROM company WHERE id = ?;";
	private static final String GET_BY_ID = "SELECT id, name FROM company WHERE id = ?;";
	private static final String DELETE_BY_ID = "DELETE FROM company WHERE id = ?;";

	private static CompanyDAO companyDAO;

	private  ConnectionManager cm;

	private CompanyDAO(ConnectionManager cm) {
		this.cm = cm;
	}

	public static CompanyDAO getCompanyDAO() {
		if (companyDAO == null) {
			companyDAO = new CompanyDAO(ConnectionManager.getCM());
		}
		return companyDAO;

	}


	@Override
	public List<Company>  getAll()  throws  ExceptionDAO {
		List<Company> companyList = new ArrayList<Company>();
		Connection con = cm.getConnection();

		try (Statement stat = con.createStatement()) {
			stat.executeQuery(GET_ALL); 
			ResultSet rs = stat.getResultSet();

			while (rs.next()) {
				Company c = new Company(rs.getString(ConstantString.NAME));
				c.setId(rs.getInt(ConstantString.ID));
				companyList.add(c);
			}
			return companyList;
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.STATEMENT_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}


	public Boolean doesExist(int id) throws  ExceptionDAO {
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(DOES_COMPANY_EXIST)) {
			stat.setInt(1, id);
			stat.executeQuery(); 
			ResultSet rs = stat.getResultSet();
			rs.next();
			return !(rs.getInt("count(*)") == 0);
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.STATEMENT_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}

	}


	public List<Company> getAllPage(int pageNumber, int nbCompanyPerPage) throws ExceptionDAO {
		List<Company> companyList = new ArrayList();
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(GET_ALL_PAGE)) {
			stat.setInt(1, nbCompanyPerPage);
			stat.setInt(2, (pageNumber - 1) * nbCompanyPerPage);
			stat.executeQuery();
			ResultSet rs = stat.getResultSet();
			while (rs.next()) {
				Company company = new Company(rs.getString(2));
				company.setId(rs.getInt(1));
				companyList.add(company);
			}
			return companyList;
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.STATEMENT_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

	public Company getById(int id) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(GET_BY_ID)) {
			stat.setInt(1, id);
			stat.executeQuery();
			ResultSet res = stat.getResultSet();
			res.next();
			Company company = new Company(res.getString(2));
			company.setId(res.getInt(1));
			return company;
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.GET_BY_ID_COMPANY);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

	public void deleteById(int id) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(DELETE_BY_ID)) {
			con.setAutoCommit(false);
			ComputerDAO.getComputerDAO().deleteByCompany(id, con);
			stat.setInt(1, id);
			stat.executeQuery();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				log.error(e.getMessage());
			}
			log.error(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

}
