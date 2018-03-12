package com.excilys.xdurbec.formation.computerDataBase.dao;


import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

import java.sql.*;

public class ComputerDAO extends EntityDAO implements EntityDAOComportment<Computer>{
	
	private static ComputerDAO computerDAO;
	
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
	

	
	private ConnectionManager cm;

	private ComputerDAO(ConnectionManager cm) {
		this.cm = cm;
	}

	public static ComputerDAO getComputerDAO() {
		if (computerDAO == null) {
			computerDAO = new ComputerDAO(ConnectionManager.getCM());
		}
		return computerDAO;
	}


	public Computer getById(int id) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(GET_BY_ID_REQUEST)) {
			stat.setInt(1, id);
			stat.executeQuery(); 
			ResultSet rs = stat.getResultSet();
			rs.next();
			Company company = new Company();
			if (rs.getInt(ConstantStringDAO.ID_OF_COMPANY) != 0) {
				company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
				company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
			}	
			Computer computer = new Computer(rs.getString(ConstantStringDAO.NAME_OF_COMPUTER), rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER), rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER),company);
			computer.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER));
			return computer;
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

	@Override
	public List<Computer> getAll() throws ExceptionDAO {
		Connection con = cm.getConnection();
		try (Statement stat = con.createStatement()) {
			stat.executeQuery(GET_ALL_REQUEST); 
			ResultSet res = stat.getResultSet();
			List<Computer> listComputer = new ArrayList<Computer>();
			while (res.next()) {
				Company company = new Company();
				Computer computer =  new Computer();
				if (res.getInt(ConstantStringDAO.ID_OF_COMPANY) != 0) {
					company.setId(res.getInt(ConstantStringDAO.ID_OF_COMPANY));
					company.setName(res.getString(ConstantStringDAO.NAME_OF_COMPANY));
				} else {
					company.setId(0);
					company.setName(null);
				}
				computer.setId(res.getInt(ConstantStringDAO.ID_OF_COMPUTER));
				computer.setName(res.getString(ConstantStringDAO.NAME_OF_COMPUTER));
				computer.setIntroduced(res.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
				computer.setDiscontinued(res.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
				computer.setCompany(company);
				listComputer.add(computer);
			}
			return listComputer;
		} catch (SQLException e) {
			System.out.println("sqlException: " + e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.GET_ALL_ERROR);
		} finally {
			try {
				con.close();
			}
			catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}


	public void create(Computer entity) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(CREATE_REQUEST);) {
		stat.setString(1, entity.getName());
		stat.setDate(2, entity.getIntroduced());
		stat.setDate(3, entity.getDiscontinued());

		if (entity.getCompany() != null) {
			stat.setInt(4, entity.getCompany().getId());
		} else {
			stat.setNull(4, Types.INTEGER);
		}

		stat.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}



	public void update(Computer entity) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try (PreparedStatement stat = con.prepareStatement(UPDATE_REQUEST)) {
			stat.setString(1, entity.getName());
			stat.setDate(2, entity.getIntroduced());
			stat.setDate(3, entity.getDiscontinued());
			if (entity.getCompany() != null && entity.getCompany().getId() != 0) {
				stat.setInt(4, entity.getCompany().getId());
			} else {
				stat.setNull(4, Types.INTEGER);
			}
			stat.setInt(5, entity.getId());
			stat.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.UPDATE_ERROR);
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
		try {
			PreparedStatement stat = con.prepareStatement(DELETE_REQUEST);
			stat.setInt(1, id);
			stat.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

	public List<Computer> getAllPage(int pageNumber, int nbEntityPerPage, String filter, ComputerAttributes orderBy, Boolean ascendingOrder) throws ExceptionDAO {
			Connection con = cm.getConnection();
			List<Computer> computerList = new ArrayList<>();
			String direction = ascendingOrder ? "ASC" : "DESC";
			try (PreparedStatement stat = con.prepareStatement(String.format(GET_PAGE_WITH_FILTRE_REQUEST, orderBy.sqlName, direction))) {
				stat.setString(1, "%" + filter + "%");
				stat.setString(2, "%" + filter + "%");
				stat.setInt(3, nbEntityPerPage);
				int firstPage = (pageNumber - 1) * nbEntityPerPage;
				if (firstPage < 0) {
					firstPage = 0;
				}
				stat.setInt(4, firstPage);
				System.out.println(stat.toString());
				stat.executeQuery();
				ResultSet res = stat.getResultSet();
				 
				while (res.next()) {
					Company company = new Company();
					Computer computer =  new Computer();
					if (res.getInt(ConstantStringDAO.ID_OF_COMPANY) != 0) {
						company.setId(res.getInt(ConstantStringDAO.ID_OF_COMPANY));
						company.setName(res.getString(ConstantStringDAO.NAME_OF_COMPANY));
					} else {
						company.setId(0);
						company.setName(null);
					}

					computer.setId(res.getInt(ConstantStringDAO.ID_OF_COMPUTER));
					computer.setName(res.getString(ConstantStringDAO.NAME_OF_COMPUTER));
					computer.setIntroduced(res.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
					computer.setDiscontinued(res.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
					computer.setCompany(company);

					computerList.add(computer);
				}
				return computerList;
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.GET_ALL_ERROR);
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
					throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
				}
			}			
	}
	
	public int getComputerNumber(String filter) throws ExceptionDAO {
		Connection con = cm.getConnection();
		
		try (PreparedStatement stat = con.prepareStatement(NUMBER_REQUEST)) {
			stat.setString(1, "%" + filter + "%");
			stat.setString(2, "%" + filter + "%");
			stat.executeQuery();
			ResultSet res = stat.getResultSet();
			res.next();
			return res.getInt("count(*)");
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.COMPUTER_NUMBER_ERROR);
			
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}
	
	public void deleteByCompany(int companyId, Connection con) throws SQLException {
		try (PreparedStatement stat = con.prepareStatement(DELETE_BY_COMPANY)) {
			stat.setInt(1, companyId);
			stat.executeQuery();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	
}
