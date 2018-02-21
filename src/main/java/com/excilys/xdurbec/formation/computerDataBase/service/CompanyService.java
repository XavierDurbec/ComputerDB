package com.excilys.xdurbec.formation.computerDataBase.service;

import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;

public class CompanyService extends EntityService implements EntityServiceComportment<Company>{

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
		} catch (ExceptionDAO e) {
			throw new ExceptionService(ExceptionService.GET_ALL_ERROR);
		}
	}

	public CompanyPage getCompanyPage(int pageNumber, int nbCompanyByPage) throws ExceptionService {
		return new CompanyPage(pageNumber, nbCompanyByPage);
	}

	public Boolean companyExistenceVerification(int id) {
		try {
			return companyDAO.doesExist(id);
		} catch (ExceptionDAO e) {
			log.error(ExceptionService.STATEMENT_ERROR);		
			return false;
		}
	}
}
