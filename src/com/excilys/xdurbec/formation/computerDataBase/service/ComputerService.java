package com.excilys.xdurbec.formation.computerDataBase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;
import org.apache.logging.log4j.Logger;
public class ComputerService extends EntityService implements EntityServiceComportment<Computer>{

	private static ComputerService computerService;
	private ComputerDAO computerDAO;
	private CompanyDAO companyDAO;
	private CompanyService companyService;
	
	private ComputerService() {
		this.computerDAO = ComputerDAO.getComputerDAO();
		this.companyDAO = CompanyDAO.getCompanyDAO();
		this.companyService = CompanyService.getCompanyService();
	}

	public static ComputerService getComputerService() {
		if(computerService == null) {
			computerService = new ComputerService();
		}
		return computerService;
	}

	public Computer getById(int id) throws ExceptionService {
		try {
			return computerDAO.getById(id);
		}
		catch(ExceptionDAO e){
			throw new ExceptionService(ExceptionService.ID_COMPUTER_ERROR);
		}
	}

	@Override
	public List<Computer> getAll() throws  ExceptionService {
		try {
			return computerDAO.getAll();
		}
		catch(ExceptionDAO e){
			throw new ExceptionService(ExceptionService.GET_ALL_ERROR);
		}
	}


	public void create(Computer entity) throws  ExceptionService {
		
		if(entity.getCompany() == null || companyService.companyExistenceVerification(entity.getCompany().getId()) ) {
		try{
			computerDAO.create(entity);
		}
		catch(ExceptionDAO e){
			throw new ExceptionService(ExceptionService.CREATE_ERROR);
		}
		}
		else {
			throw new ExceptionService(ExceptionService.DOES_EXIST_ERROR);
		}
	}


	public void update(Computer entity) throws  ExceptionService {
		try{
			computerDAO.update(entity);
		}
		catch(ExceptionDAO e){
			throw new ExceptionService(ExceptionService.UPDATE_ERROR);
		}
	}


	public void deleteById(int id) throws  ExceptionService {
		try {
			computerDAO.deleteById(id);
		}
		catch(ExceptionDAO e){
			throw new ExceptionService(ExceptionService.DELETE_ERROR);
		}
	}
	
	public ComputerPage getComputerPage(int pageNumber,int nbComputerByPage) throws ExceptionService {
		return new ComputerPage( pageNumber, nbComputerByPage);
	}
}
