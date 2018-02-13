package com.excilys.xdurbec.formation.computerDataBase.dao;


import java.util.List;
import java.sql.*;

import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

public class ComputerDAO implements EntityDAO<Computer>{
		private static ComputerDAO computerDAO;
		
		private ConnectionManager cm;
		
		private ComputerDAO(ConnectionManager cm) {
			this.cm = cm;
		}
		
		public static ComputerDAO getComputerDAO() {
			if(computerDAO == null) {
				computerDAO = new ComputerDAO(ConnectionManager.getCM());
			}
			return computerDAO;
		}
		
		
		
		@Override
		public Computer get(int id) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public List<Computer> getAll() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void create(Computer entity) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void set(Computer entity) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void delete(int id) {
			// TODO Auto-generated method stub
			
		}
		
}
