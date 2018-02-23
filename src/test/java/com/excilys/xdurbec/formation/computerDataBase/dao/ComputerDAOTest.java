package com.excilys.xdurbec.formation.computerDataBase.dao;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

@PowerMockIgnore({"javax.management.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(ComputerDAO.class)
public class ComputerDAOTest {

	private static ComputerDAO computerDAO;

	

	@BeforeClass
	public static void init() throws SQLException, ClassNotFoundException, IOException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		initDatabase();
		computerDAO = ComputerDAO.getComputerDAO();
	}
	

	@AfterClass
	public static void destroy() throws SQLException, ClassNotFoundException, IOException {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			statement.executeUpdate("DROP TABLE computer");
			connection.commit();
		}
	}

	/**
	 * Database initialization for testing i.e.
	 * <ul>
	 * <li>Creating Table</li>
	 * <li>Inserting record</li>
	 * </ul>
	 * 
	 * @throws SQLException
	 */
	private static void initDatabase() throws SQLException {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			statement.execute("create table company (id  bigint not null , name varchar(255), constraint pk_company primary key (id));");
			connection.commit();
			statement.execute("create table computer (id bigint not null , name varchar(255), introduced date NULL, discontinued date NULL,"
					+ " company_id bigint NULL, "
					+ "constraint pk_computer primary key (id), constraint fk_computer_company_1 foreign key (company_id) references company (id));");			
					
			connection.commit();
			statement.executeUpdate("INSERT INTO company (id,name) VALUES (1, 'Umbrela')");
			connection.commit();
			statement.executeUpdate("INSERT INTO computer (id,name,introduced,discontinued,company_id) VALUES (1,'Xbox', '1991-06-30','2000-04-15',1)");
			connection.commit();
			statement.executeUpdate("INSERT INTO computer (id,name,introduced,discontinued,company_id) VALUES (20,'PS2',null,null,null)");
			connection.commit();
		}
	}

	/**
	 * Create a connection
	 * 
	 * @return connection object
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
	//	return DriverManager.getConnection("jdbc:hsqldb:mem:employees", "vinod", "vinod");
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		//return DriverManager.getConnection("jdbc:hsqldb:mem:cdb", "admincdb", "qwerty1234");	
		return DriverManager.getConnection(bundle.getString("sgbd.url"), bundle.getString("sgbd.user"), bundle.getString("sgbd.pass"));
	}

	/**
	 * Get total records in table
	 * 
	 * @return total number of records. In case of exception 0 is returned
	 */
	private int getTotalRecords() {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			ResultSet result = statement.executeQuery("SELECT count(*) as total FROM computer LEFT JOIN company ON computer.company_id = company.id");
			if (result.next()) {
				return result.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		}
	
	
	
	private int getRecords() {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			ResultSet result = statement.executeQuery("SELECT computer.id, computer.name, computer.introduced, "
					+ "computer.discontinued, computer.company_id, company.id, company.name "
					+ "FROM computer LEFT JOIN company "
					+ "ON computer.company_id = company.id;");
			List<Computer> listComputer = new ArrayList<Computer>();
			while (result.next()) {
				Company company = new Company();
				Computer computer =  new Computer();
				if (result.getInt(ConstantStringDAO.ID_OF_COMPANY) == 0) {
					company.setId(result.getInt(ConstantStringDAO.ID_OF_COMPANY));
					company.setName(result.getString(ConstantStringDAO.NAME_OF_COMPANY));
				} else {
					company.setId(0);
					company.setName(null);
				}
				computer.setId(result.getInt(ConstantStringDAO.ID_OF_COMPUTER));
				computer.setIntroduced(result.getDate(ConstantStringDAO.INTRODUCED_OF_COMPUTER));
				computer.setDiscontinued(result.getDate(ConstantStringDAO.DISCONTINUED_OF_COMPUTER));
				computer.setCompany(company);

				listComputer.add(computer);
			}
			System.out.println("getRec = " + listComputer);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		}
	
	

	@Test
	public void chekGetAll() throws ExceptionDAO {
		List<Computer> computers = computerDAO.getAll();
		System.out.println("Total : " + getTotalRecords());
		System.out.println("computers : " + computers);
		getRecords();
		assertEquals(2, computers.size());
	}
	
	
	
	
	@Test
	public void checkGetById() {
		
		try {
			Computer computer = computerDAO.getById(1);
		System.out.println("Computer = " + computer);
		assertEquals("Xbox", computer.getName());
		} catch (ExceptionDAO e) {
			System.out.println(e);
		}
	}

	@Test
	public void checkNameExistsTest() {
		try (Connection connection = getConnection();
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {
			ResultSet result = statement.executeQuery("SELECT name FROM computer");
			if (result.first()) {
				assertEquals("Xbox", result.getString("name"));
			}

			if (result.last()) {
				assertEquals("PS2", result.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
