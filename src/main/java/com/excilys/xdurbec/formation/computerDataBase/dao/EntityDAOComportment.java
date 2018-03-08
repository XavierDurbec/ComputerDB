package com.excilys.xdurbec.formation.computerDataBase.dao;

import java.util.List;
public interface EntityDAOComportment<T>  {
	List<T> getAll() throws ExceptionDAO;
	//List<T> getAllPage(int pageNumber, int nbEntityPerPage) throws ExceptionDAO;
}
