package com.excilys.xdurbec.formation.computerDataBase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;

@Service
public class CompanyService extends EntityService implements EntityServiceComportment<Company> {

	private CompanyDAO companyDAO;

	@Autowired
	public CompanyService(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}


	@Override
	public List<Company> getAll() throws ExceptionService {
		try {
			return companyDAO.getAll();
		} catch (ExceptionDAO e) {
			throw new ExceptionService(ExceptionService.GET_ALL_ERROR);
		}
	}

	public Boolean companyExistenceVerification(int id) {
		try {
			return companyDAO.doesExist(id);
		} catch (ExceptionDAO e) {
			log.error(ExceptionService.STATEMENT_ERROR);		
			return false;
		}
	}

	public  Company getCompanyById(int id) throws ExceptionService {
		if (id == 0) {
			return null;
		} else {
			try {
				return companyDAO.getById(id);
			} catch (ExceptionDAO e) {
				log.error(e.getMessage());
				throw new ExceptionService(ExceptionService.GET_COMPANY_BY_ID);
			}
		}
	}
	/*
	public void deleteCompanyById(int id) throws ExceptionService {
		try {
			companyDAO.deleteById(id);
		} catch (ExceptionDAO e) {
			log.error(e.getMessage());
			throw new ExceptionService(ExceptionService.DELETE_COMPANY_ERROR);
		}
	}
	*/
}
