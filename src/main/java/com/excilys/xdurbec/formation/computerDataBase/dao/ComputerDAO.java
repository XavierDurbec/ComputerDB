package com.excilys.xdurbec.formation.computerDataBase.dao;


import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

import java.sql.*;

public class ComputerDAO extends EntityDAO implements EntityDAOComportment<Computer>{

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
	
	private static final String NUMBER_REQUEST = "SELECT count(*) FROM computer;";

	private static ComputerDAO computerDAO;

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
			Computer computer = new Computer(rs.getString(ConstantStringDAO.ID_OF_COMPUTER), rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER), rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER),company);
			computer.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER));
			return computer;
		} catch (SQLException e) {
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}



	@Override
	public List<Computer> getAll() throws ExceptionDAO {
		Connection con = cm.getConnection();
		try (Statement stat = con.createStatement()) {
			stat.executeQuery(GET_ALL_REQUEST); 

			ResultSet rs = stat.getResultSet();

			List<Computer> listComputer = new ArrayList<Computer>();

			while (rs.next()) {
				Company company = new Company();
				Computer computer =  new Computer();
				if (rs.getInt(ConstantStringDAO.ID_OF_COMPANY) != 0) {
					company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
					company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
				} else {
					company.setId(0);
					company.setName(null);
				}

				computer.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER));
				computer.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPUTER));
				computer.setIntroduced(rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
				computer.setDiscontinued(rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
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
				showLogSQLException(e);
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
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				showLogSQLException(e);
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
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.UPDATE_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				showLogSQLException(e);
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
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

	@Override
	public List<Computer> getAllPage(int pageNumber, int nbEntityPerPage) throws ExceptionDAO {
			Connection con = cm.getConnection();
			List<Computer> computerList = new ArrayList<>();
			
			try (PreparedStatement stat = con.prepareStatement(GET_PAGE_REQUEST)) {
				stat.setInt(1, nbEntityPerPage);
				stat.setInt(2, (pageNumber - 1) * nbEntityPerPage);
				stat.executeQuery();
				ResultSet rs = stat.getResultSet();
				 
				while (rs.next()) {
					Company company = new Company();
					Computer computer =  new Computer();
					if (rs.getInt(ConstantStringDAO.ID_OF_COMPANY) != 0) {
						company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
						company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
					} else {
						company.setId(0);
						company.setName(null);
					}

					computer.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER));
					computer.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPUTER));
					computer.setIntroduced(rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
					computer.setDiscontinued(rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
					computer.setCompany(company);

					computerList.add(computer);
				}
				return computerList;
			} catch (SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.GET_ALL_ERROR);
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					showLogSQLException(e);
					throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
				}
			}			
	}
	
	
	public int getComputerNumber() throws ExceptionDAO {
		Connection con = cm.getConnection();
		
		try (Statement stat = con.createStatement()) {
			stat.executeQuery(NUMBER_REQUEST);
			ResultSet res = stat.getResultSet();
			res.next();
			return res.getInt("count(*)");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new ExceptionDAO(ExceptionDAO.COMPUTER_NUMBER_ERROR);
			
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("dao sql: " + e.getMessage());
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}
}
