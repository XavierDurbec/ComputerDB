package com.excilys.xdurbec.formation.computerDataBase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerAttributes;
import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;
import com.excilys.xdurbec.formation.computerDataBase.model.ComputerPage;


@Service
public class ComputerService extends EntityService implements EntityServiceComportment<Computer> {
	

	private ComputerDAO computerDAO;
	private CompanyDAO companyDAO;
	private CompanyService companyService;
	
	@Autowired
	public ComputerService(ComputerDAO computerDAO) {
		this.computerDAO = computerDAO;
		this.companyDAO = CompanyDAO.getCompanyDAO();
		this.companyService = CompanyService.getCompanyService();
	}

	public Computer getById(int id) throws ExceptionService {
		try {
			return computerDAO.getById(id);
		} catch (ExceptionDAO e) {
			throw new ExceptionService(ExceptionService.ID_COMPUTER_ERROR);
		}
	}

	@Override
	public List<Computer> getAll() throws ExceptionService {
		try {
			return computerDAO.getAll();
		} catch (ExceptionDAO e) {
			log.error("Dao Exception : " + e.getMessage());
			throw new ExceptionService(ExceptionService.GET_ALL_ERROR);
		}
	}


	public void create(Computer entity) throws  ExceptionService {

		if (entity.getCompany() == null || companyService.companyExistenceVerification(entity.getCompany().getId())) {
			if (computerDateValidator(entity)) {
				try {
					computerDAO.create(entity);
				} catch (ExceptionDAO e) {
					throw new ExceptionService(ExceptionService.CREATE_ERROR);
				}
			} else {
				throw new ExceptionService(ExceptionService.DATE_ERROR);
			}
		} else {
			throw new ExceptionService(ExceptionService.DOES_EXIST_ERROR);
		}
	}


	public void update(Computer entity) throws  ExceptionService {
		if (computerDateValidator(entity)) {
			try {
				computerDAO.update(entity);
			} catch (ExceptionDAO e) {
				throw new ExceptionService(ExceptionService.UPDATE_ERROR);
			}
		} else {
			throw new ExceptionService(ExceptionService.DATE_ERROR);
		}
	}


	public void deleteById(int id) throws  ExceptionService {
		try {
			computerDAO.deleteById(id);
		} catch (ExceptionDAO e) {
			throw new ExceptionService(ExceptionService.DELETE_ERROR);
		}
	}

	public ComputerPage getComputerPage(int pageNumber, int nbComputerByPage, String filter, ComputerAttributes orderBy, Boolean ascendingOrder) throws ExceptionService {
		return new ComputerPage(pageNumber, nbComputerByPage, filter, orderBy, ascendingOrder);
	}

	public int getComputerNumber(String filter) throws ExceptionService {
		try {
			if (filter == null) {
				filter = "";
			}
			return computerDAO.getComputerNumber(filter);
		} catch (ExceptionDAO e) {
			log.error(e.getMessage());
			throw new ExceptionService(ExceptionService.COMPUTER_NUMBER_ERROR);
		}
	}


	public static Boolean computerDateValidator(Computer computer) {
		return computer.getIntroduced() == null || computer.getDiscontinued() == null 
				|| computer.getIntroduced().before(computer.getDiscontinued());
	}

	
	public ComputerPage refresh(ComputerPage computerPage) throws ExceptionService {
		try {
			ComputerPage newComputerPage = new ComputerPage(computerPage.getPageNumber(), computerPage.getNbComputerPerPage(), computerPage.getFilter(), computerPage.getOrderBy(), computerPage.getAscendingOrder());
			newComputerPage.setComputerList(computerDAO.getAllPage(newComputerPage.getPageNumber(), newComputerPage.getNbComputerPerPage(), 
					newComputerPage.getFilter(), newComputerPage.getOrderBy(), newComputerPage.getAscendingOrder()));
			return newComputerPage;
		} catch (ExceptionDAO e) {
			throw new ExceptionService(ExceptionService.GET_ALL_ERROR_PAGE);
		}
	}
}
