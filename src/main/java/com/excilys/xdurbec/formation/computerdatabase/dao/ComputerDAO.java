package com.excilys.xdurbec.formation.computerdatabase.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.xdurbec.formation.computerdatabase.model.Company;
import com.excilys.xdurbec.formation.computerdatabase.model.Computer;



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


	@PersistenceContext
	private EntityManager em;
	private CriteriaBuilder cb;


	private JdbcTemplate jdbcTemplate;


	public ComputerDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate =  jdbcTemplate;
	}


	@PostConstruct
	public void init() {
		this.cb = em.getCriteriaBuilder();
	}


	public Computer getById(int id) throws ExceptionDAO {
		try {
			CriteriaQuery<Computer> criteriaQuery = cb.createQuery(Computer.class);
			Root<Computer> model = criteriaQuery.from(Computer.class);
			criteriaQuery.where(cb.equal(model.get("id"), id));
			TypedQuery<Computer> query = em.createQuery(criteriaQuery);
			return query.getSingleResult();
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		}
	}

	@Override
	public List<Computer> getAll() throws ExceptionDAO {
		try {
			CriteriaQuery<Computer> criteriaQuery = cb.createQuery(Computer.class);
			Root<Computer> model = criteriaQuery.from(Computer.class);
			TypedQuery<Computer> query = em.createQuery(criteriaQuery);
			return query.getResultList();
		}  catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		}
	}

	@Transactional
	public void create(Computer computer) throws ExceptionDAO {
		try {
			if (computer.getCompany() != null && computer.getCompany().getId() == 0) {
				computer.setCompany(null);
			}
			em.persist(computer);
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.CREATE_ERROR);
		}
	}

	@Transactional
	public void update(Computer computer) throws ExceptionDAO {
		try {
			CriteriaUpdate<Computer> update = cb.createCriteriaUpdate(Computer.class);
			Root<Computer> model = update.from(Computer.class);
			update.set("name", computer.getName());
			update.set("introduced", computer.getIntroduced());
			update.set("discontinued", computer.getDiscontinued());
			update.set("company", computer.getCompany());
			update.where(cb.equal(model.get("id"), computer.getId()));
			em.createQuery(update).executeUpdate();
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.CREATE_ERROR);
		}

	}

	public void deleteById(int id) throws ExceptionDAO {
		try {
			CriteriaDelete<Computer> delete = cb.createCriteriaDelete(Computer.class);
			Root<Computer> model = delete.from(Computer.class);
			delete.where(cb.equal(model.get("id"), id));
			em.createQuery(delete).executeUpdate();
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		}
	}

	public List<Computer> getAllPage(int pageNumber, int nbEntityPerPage, String filter, ComputerAttributes orderBy, Boolean ascendingOrder) {
		CriteriaQuery<Computer> criteriaQuery = cb.createQuery(Computer.class);
		Root<Computer> model = criteriaQuery.from(Computer.class);
		if (ascendingOrder) {
			if (orderBy.sqlName.equals("company")) {
				criteriaQuery.orderBy(cb.asc(model.get(orderBy.sqlName).get("name")));
			} else {
				criteriaQuery.orderBy(cb.asc(model.get(orderBy.sqlName)));
			}
		} else {				
			if (orderBy.sqlName.equals("company")) {
				criteriaQuery.orderBy(cb.desc(model.get(orderBy.sqlName).get("name")));
			} else {
				criteriaQuery.orderBy(cb.desc(model.get(orderBy.sqlName)));
			}
		}
		criteriaQuery.where(cb.like(model.get(ComputerAttributes.NAME.sqlName), "%" + filter + "%"));
		TypedQuery<Computer> query = em.createQuery(criteriaQuery);
		int firstPage = (pageNumber - 1) * nbEntityPerPage;
		firstPage = firstPage < 0 ? 0 : firstPage;
		query.setFirstResult(firstPage);
		query.setMaxResults(nbEntityPerPage);
		return query.getResultList();

	}

	public int getComputerNumber(String filter) {
		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
		Root<Computer> model = criteriaQuery.from(Computer.class);
		criteriaQuery.select(cb.count(model));
		criteriaQuery.where(cb.like(model.get(ComputerAttributes.NAME.sqlName), "%" + filter + "%"));
		TypedQuery<Long> query2 = em.createQuery(criteriaQuery);
		return query2.getSingleResult().intValue();
		
	}

	@Transactional
	public void deleteByCompany(int companyId) throws ExceptionDAO {
		try {
			this.jdbcTemplate.update(DELETE_BY_COMPANY, Long.valueOf(companyId));
		} catch (DataAccessException e) {
			log.error(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		}
	}

}
