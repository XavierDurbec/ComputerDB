package com.excilys.xdurbec.formation.computerDataBase.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;

public class CompanyService implements EntityServiceComportment<Company>{

	private static CompanyService companyService;

	private CompanyDAO companyDAO;


	public CompanyService() {
		this.companyDAO = CompanyDAO.getCompanyDAO();
	}

	public static CompanyService getCompanyService() {
		if (companyService == null) {
			companyService = new CompanyService(); 
		}
		return companyService;
	}


	@Override
	public List<Company> getAll() throws ExceptionService{
		try {
			return companyDAO.getAll();
		}
		catch(ExceptionDAO e) {
			throw new ExceptionService(ExceptionService.GET_ALL_ERROR);
		}
	}


}
