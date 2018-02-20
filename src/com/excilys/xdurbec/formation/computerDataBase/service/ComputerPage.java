package com.excilys.xdurbec.formation.computerDataBase.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

public class ComputerPage {

	private int pageNumber;
	private int nbComputerPerPage;
	private List<Computer> computerList = new ArrayList<Computer>();
	
	public ComputerPage(int pageNumber, int nbComputerPerPage) throws ExceptionService {
		this.pageNumber = pageNumber;
		this.nbComputerPerPage = nbComputerPerPage;
		this.refresh();
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getNbComputerPerPage() {
		return nbComputerPerPage;
	}
	public void setNbComputerPerPage(int nbComputerPerPage) {
		this.nbComputerPerPage = nbComputerPerPage;
	}
	public List<Computer> getComputerList() {
		return computerList;
	}

	public void refresh() throws ExceptionService {
		try {
			this.computerList = ComputerDAO.getComputerDAO().getAllPage(pageNumber, nbComputerPerPage);
		} catch (ExceptionDAO e) {
			throw new ExceptionService(ExceptionService.GET_ALL_ERROR_PAGE);
		}
	}
}	
