package com.excilys.xdurbec.formation.computerDataBase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;

public class CompanyService implements EntityService<Company>{
	
	private static CompanyService companyService;
	
	private CompanyDAO companyDAO;
	
	
	public CompanyService() {
		this.companyDAO = CompanyDAO.getCompanyDAO();
	}
	
	public static CompanyService getCompanyService() {
		if (companyService == null) {
			return new CompanyService();
		}
		else {
			return companyService;
		}
	}
	
	

	@Override
	public Company getById(int id) throws SQLException{
		return companyDAO.getById(id);
	}

	@Override
	public List<Company> getAll() throws SQLException{
		return companyDAO.getAll();
	}

	@Override
	public void create(Company entity) throws SQLException{
		companyDAO.create(entity);
		}

	@Override
	public void update(Company entity) throws SQLException {
		companyDAO.update(entity);
	}

	@Override
	public void deleteById(int id) throws SQLException {
		companyDAO.deleteById(id);
	}
	

}
