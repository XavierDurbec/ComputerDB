package com.excilys.xdurbec.formation.computerDataBase.dao;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;



@Repository
public class ComputerDAO extends EntityDAO implements EntityDAOComportment<Computer> { 

	private static final String GET_BY_ID_REQUEST = "SELECT computer.id, computer.name, computer.introduced,"
			+ " computer.discontinued, computer.company_id, company.id, company.name "
			+ "FROM computer LEFT JOIN company "
			+ "ON computer.company_id = company.id "
			+ "WHERE computer.id = ? ;";

	private static final String GET_ALL_REQUEST = "SELECT computer.id, computer.name, computer.introduced, "
			+ "computer.discontinued, computer.company_id, company.id, company.name "
			+ "FROM computer LEFT JOIN company "
			+ "ON computer.company_id = company.id;";

	private static final String CREATE_REQUEST = "INSERT INTO computer (name, introduced, discontinued, company_id) "
			+ "VALUES(?,?,?,?);";

	private static final String UPDATE_REQUEST = "UPDATE computer SET "
			+ "name =?, "
			+ "introduced =?, "
			+ "discontinued=?, "
			+ "company_id=? "
			+ "WHERE id =?;";

	private static final String GET_PAGE_REQUEST = "SELECT computer.id, computer.name, computer.introduced, "
			+ "computer.discontinued, computer.company_id, company.id, company.name "
			+ "FROM computer LEFT JOIN company "
			+ "ON computer.company_id = company.id "
			+ "LIMIT ? "
			+ "OFFSET ?;";

	private static final String DELETE_REQUEST = "DELETE FROM computer WHERE id =?;";

	private static final String NUMBER_REQUEST = "SELECT count(*) FROM computer LEFT JOIN company ON computer.company_id = company.id"
			+ " WHERE computer.name LIKE ? OR company.name LIKE ?;";

	private static final String GET_PAGE_WITH_FILTRE_REQUEST = "SELECT computer.id, computer.name, computer.introduced, "
			+ "computer.discontinued, computer.company_id, company.id, company.name "
			+ "FROM computer LEFT JOIN company "
			+ "ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? "
			+ "ORDER BY %s %s "
			+ "LIMIT ? "
			+ "OFFSET ?;";

	private static final String DELETE_BY_COMPANY = "DELETE FROM computer WHERE company.id = ?;";


	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ComputerDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate =  jdbcTemplate;
	}




	public Computer getById(int id) throws ExceptionDAO {
		try {
			return this.jdbcTemplate.queryForObject(GET_BY_ID_REQUEST,
					new Object[]{id},
					new RowMapper<Computer>() {
				public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
					Computer computer = new  Computer();
					computer.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER));
					computer.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPUTER));
					computer.setIntroduced(rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
					computer.setDiscontinued(rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
					Company company = new Company();
					company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
					company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
					computer.setCompany(company);
					return computer;
				}
			});
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		}
	}

	@Override
	public List<Computer> getAll() throws ExceptionDAO {
		return this.jdbcTemplate.query(GET_ALL_REQUEST,
				new RowMapper<Computer>() {
					public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
						Computer computer = new  Computer();
						computer.setId(Integer.valueOf(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER)));
						computer.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPUTER));
						computer.setIntroduced(rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
						computer.setDiscontinued(rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
						Company company = new Company();
						company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
						company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
						computer.setCompany(company);
						return computer;
					}
				});

	}

	public void create(Computer entity) throws ExceptionDAO {
		try {
			System.out.println("entity : " + entity);
			Integer companyId = entity.getCompany() != null ? entity.getCompany().getId() : null;
			this.jdbcTemplate.update(CREATE_REQUEST, entity.getName(), entity.getIntroduced(), entity.getDiscontinued(), companyId);	
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.CREATE_ERROR);
		}
	}


	public void update(Computer entity) throws ExceptionDAO {
		try {
			Integer companyId = entity.getCompany() != null ? entity.getCompany().getId() : null;
			this.jdbcTemplate.update(UPDATE_REQUEST, entity.getName(), entity.getIntroduced(), entity.getDiscontinued(), companyId, entity.getId());	
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.CREATE_ERROR);
		}
			
	}

	public void deleteById(int id) throws ExceptionDAO {
		try {
			this.jdbcTemplate.update(DELETE_REQUEST, Long.valueOf(id));
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		}
	}

	public List<Computer> getAllPage(int pageNumber, int nbEntityPerPage, String filter, ComputerAttributes orderBy, Boolean ascendingOrder) throws ExceptionDAO {
		String orderByDirection = ascendingOrder ? "ASC" : "DESC";
		int firstPage = (pageNumber - 1) * nbEntityPerPage;
		firstPage = firstPage < 0 ? 0 : firstPage;
		return this.jdbcTemplate.query(String.format(GET_PAGE_WITH_FILTRE_REQUEST, orderBy.sqlName, orderByDirection), 
				new Object[]{"%" + filter + "%", "%" + filter + "%", nbEntityPerPage, firstPage},
				new RowMapper<Computer>() {
					public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
						Computer computer = new  Computer();
						computer.setId(Integer.valueOf(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER)));
						computer.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPUTER));
						computer.setIntroduced(rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
						computer.setDiscontinued(rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
						Company company = new Company();
						company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
						company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
						computer.setCompany(company);
						return computer;
					}
				});
	}

	public int getComputerNumber(String filter) throws ExceptionDAO {
		return this.jdbcTemplate.queryForObject(NUMBER_REQUEST, Integer.class, "%" + filter + "%", "%" + filter + "%");
	}

	
	public void deleteByCompany(int companyId) throws ExceptionDAO {
		try {
			this.jdbcTemplate.update(DELETE_BY_COMPANY, Long.valueOf(companyId));
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		}
	}

}
