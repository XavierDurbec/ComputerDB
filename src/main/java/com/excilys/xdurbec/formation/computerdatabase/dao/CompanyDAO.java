package com.excilys.xdurbec.formation.computerdatabase.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.xdurbec.formation.computerdatabase.model.Company;
import com.excilys.xdurbec.formation.computerdatabase.model.Computer;

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


	private ComputerDAO computerDAO;
	private JdbcTemplate jdbcTemplate;

	@PersistenceContext
	private EntityManager em;
	private CriteriaBuilder cb;

	public CompanyDAO(JdbcTemplate jdbcTemplate, ComputerDAO computerDAO) {
		this.computerDAO = computerDAO;
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		this.cb = em.getCriteriaBuilder();
	}


	@Override
	public List<Company>  getAll()  throws  ExceptionDAO {
		try {
			CriteriaQuery<Company> criteriaQuery = cb.createQuery(Company.class);
			Root<Company> model = criteriaQuery.from(Company.class);
			TypedQuery<Company> query = em.createQuery(criteriaQuery);
			return query.getResultList();
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.GET_ALL_ERROR);
		}
	}


	public Boolean doesExist(int id) {
		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
		Root<Company> model = criteriaQuery.from(Company.class);
		criteriaQuery.select(cb.count(model));
		criteriaQuery.where(cb.like(model.get("id"), String.valueOf(id)));
		TypedQuery<Long> query = em.createQuery(criteriaQuery);
		return 0 < query.getSingleResult().intValue();
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

			CriteriaQuery<Company> criteriaQuery = cb.createQuery(Company.class);
			Root<Company> model = criteriaQuery.from(Company.class);
			criteriaQuery.where(cb.equal(model.get("id"), id));
			TypedQuery<Company> query = em.createQuery(criteriaQuery);
			return query.getSingleResult();
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		}
	}

	@Transactional
	public void delete(int id) {
		CriteriaDelete<Company> delete = cb.createCriteriaDelete(Company.class);
		Root<Company> model = delete.from(Company.class);
		delete.where(cb.equal(model.get("id"), id));
		em.createQuery(delete).executeUpdate();
		

	}

}
