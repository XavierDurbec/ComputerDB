package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import  com.excilys.xdurbec.formation.computerDataBase.model.Company;

/**
 * Crude gestion of Company.
 * @author excilys
 *
 */
@Repository
public class CompanyDAO extends EntityDAO implements EntityDAOComportment<Company> {

	private static final String GET_ALL = "SELECT company.id, company.name FROM company;";
	private static final String GET_ALL_PAGE = "SELECT company.id, company.name FROM company LIMIT ? OFFSET ?;";
	private static final String DOES_COMPANY_EXIST = "SELECT count(*) FROM company WHERE id = ?;";
	private static final String GET_BY_ID = "SELECT company.id, company.name FROM company WHERE id = ?;";
	private static final String DELETE_BY_ID = "DELETE FROM company WHERE id = ?;";



	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CompanyDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	


	@Override
	public List<Company>  getAll()  throws  ExceptionDAO {
		try {
		return this.jdbcTemplate.query(GET_ALL,
				new RowMapper<Company>() {
					public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
						Company company = new Company();
						company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
						company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
						return company;
					}
				});
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.GET_ALL_ERROR);
		}
	}


	public Boolean doesExist(int id) throws  ExceptionDAO {
		return 0 < this.jdbcTemplate.queryForObject(DOES_COMPANY_EXIST, Integer.class, id);
	}


	public List<Company> getAllPage(int pageNumber, int nbCompanyPerPage) throws ExceptionDAO {
		int pageNumberTmp = pageNumber > 0 ? pageNumber : 0;
		try {
			return this.jdbcTemplate.query(GET_ALL_PAGE,
					new Object[]{nbCompanyPerPage, (pageNumberTmp - 1) * nbCompanyPerPage},
					new RowMapper<Company>() {
						public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
							Company company = new Company();
							company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
							company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
							return company;
						}
					});
			} catch (DataAccessException e) {
				log.error(e);
				throw new ExceptionDAO(ExceptionDAO.GET_ALL_ERROR);
			}
	}

	public Company getById(int id) throws ExceptionDAO {
		try {
			return this.jdbcTemplate.queryForObject(GET_BY_ID,
					new Object[]{id},
					new RowMapper<Company>() {
						public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
							Company company = new Company();
							company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
							company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
							return company;
						}
			});
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		}
	}

	

}
