package com.excilys.xdurbec.formation.computerDataBase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

public class ComputerService implements EntityService<Computer>{
		private static ComputerService computerService;

		private ComputerDAO computerDAO;
		
		private ComputerService() {
			this.computerDAO = ComputerDAO.getComputerDAO();
		}
		
		private static ComputerService getComputerService() {
			if(computerService == null) {
			 computerService = new ComputerService();
			}
				return computerService;
		}
		
		
		@Override
		public Computer getById(int id) throws SQLException {
			return computerDAO.getById(id);
		}

		@Override
		public List<Computer> getAll() throws SQLException {
			return computerDAO.getAll();
		}

		@Override
		public void create(Computer entity) throws SQLException {
			computerDAO.create(entity);
		}

		@Override
		public void update(Computer entity) throws SQLException {
			computerDAO.update(entity);
		}

		@Override
		public void deleteById(int id) throws SQLException {
			computerDAO.deleteById(id);
		}
}
