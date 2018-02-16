package com.excilys.xdurbec.formation.computerDataBase.dao;


import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

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

	private static final String DELETE_REQUEST = "DELETE FROM computer WHERE id =?;";

	private static ComputerDAO computerDAO;

	private ConnectionManager cm;

	private ComputerDAO(ConnectionManager cm) {
		this.cm = cm;
	}

	public static ComputerDAO getComputerDAO() {
		if(computerDAO == null) {
			computerDAO = new ComputerDAO(ConnectionManager.getCM());
		}
		return computerDAO;
	}


	public Computer getById(int id) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try(PreparedStatement stat = con.prepareStatement(GET_BY_ID_REQUEST)){
			stat.setInt(1, id);
			stat.executeQuery(); 
			ResultSet rs = stat.getResultSet();
			rs.next();
			Company company = new Company();
			if(rs.getInt(ConstantStringDAO.ID_OF_COMPANY) != 0) {
				company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
				company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
			}	
			Computer computer = new Computer(rs.getString(ConstantStringDAO.ID_OF_COMPUTER), rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER), rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER),company);
			computer.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER));
			return computer;
		}
		catch(SQLException e) {
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.ID_COMPUTER_ERROR);
		}
		finally {
			try {
				con.close();
			}
			catch(SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}



	@Override
	public List<Computer> getAll() throws ExceptionDAO {
		Connection con = cm.getConnection();
		try(Statement stat = con.createStatement()){

			stat.executeQuery(GET_ALL_REQUEST); 

			ResultSet rs = stat.getResultSet();

			List<Computer> listComputer = new ArrayList<Computer>();

			while(rs.next()) {
				Company company = new Company();
				Computer computer =  new Computer();
				if(rs.getInt(ConstantStringDAO.ID_OF_COMPANY) == 0) {
					company.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPANY));
					company.setName(rs.getString(ConstantStringDAO.NAME_OF_COMPANY));
				}
				else {
					company.setId(0);
					company.setName(null);
				}

				computer.setId(rs.getInt(ConstantStringDAO.ID_OF_COMPUTER));
				computer.setIntroduced(rs.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
				computer.setDiscontinued(rs.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
				computer.setCompany(company);

				listComputer.add(computer);
			}

			return listComputer;
		}
		catch(SQLException e) {
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.GET_ALL_ERROR);
		}
		finally {
			try {
				con.close();
			}
			catch(SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}


	public void create(Computer entity) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try(PreparedStatement stat = con.prepareStatement(CREATE_REQUEST)){
			stat.setString(1, entity.getName());
			stat.setDate(2, entity.getIntroduced());
			stat.setDate(3, entity.getDiscontinued());
			stat.setInt(4, entity.getId());
			stat.executeQuery();
		}
		catch(SQLException e) {
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		}
		finally {
			try {
				con.close();
			}
			catch(SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}


	public void update(Computer entity) throws ExceptionDAO {
		Connection con = cm.getConnection();

		try(PreparedStatement stat = con.prepareStatement(UPDATE_REQUEST)){
			stat.setString(1,entity.getName());
			stat.setDate(2,entity.getIntroduced());
			stat.setDate(3, entity.getDiscontinued());
			stat.setInt(4, entity.getCompany().getId());
			stat.setInt(5, entity.getId());
			stat.executeQuery();
		}
		catch(SQLException e) {
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.UPDATE_ERROR);
		}
		finally {
			try {
				con.close();
			}
			catch(SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

	public void deleteById(int id) throws ExceptionDAO {
		Connection con = cm.getConnection();
		try{
			PreparedStatement stat = con.prepareStatement(DELETE_REQUEST);
			stat.setInt(1, id);
			stat.executeUpdate();
		}
		catch(SQLException e) {
			showLogSQLException(e);
			throw new ExceptionDAO(ExceptionDAO.DELETE_ERROR);
		}
		finally {
			try {
				con.close();
			}
			catch(SQLException e) {
				showLogSQLException(e);
				throw new ExceptionDAO(ExceptionDAO.CONNECTION_ERROR);
			}
		}
	}

}
