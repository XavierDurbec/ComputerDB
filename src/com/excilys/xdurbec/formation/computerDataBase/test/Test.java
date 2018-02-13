package com.excilys.xdurbec.formation.computerDataBase.test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;
import com.excilys.xdurbec.formation.computerDataBase.model.Computer;

public class Test {
		public static void main(String args[]) throws SQLException {
			/*
			Company cFU = new Company("GameWorkshop");
			cFU.setId(44);
			
			CompanyDAO.getCompanyDAO().set(cFU);
			
			Company company = CompanyDAO.getCompanyDAO().get(44);
			System.out.println(company);
			
			
			CompanyDAO.getCompanyDAO().delete(44);
			
			List<Company> lc = CompanyDAO.getCompanyDAO().getAll();
			
			for( Company c : lc) {
				System.out.println(c);
			}
			
		
			Company companyForCreation = new Company("Umbrela");
			CompanyDAO.getCompanyDAO().create(companyForCreation);
		*/	

			//ComputerDAO.getComputerDAO().create(new Computer("Dell4852",Date.valueOf("1991-06-30"),Date.valueOf("2012-08-13"), CompanyDAO.getCompanyDAO().getById(25)));
			
			System.out.println(ComputerDAO.getComputerDAO().getById(576));
			Computer cp = new Computer("Loool",Date.valueOf("1991-07-30"),Date.valueOf("2012-08-13"),CompanyDAO.getCompanyDAO().getById(22));
					cp.setId(576);
			ComputerDAO.getComputerDAO().update(cp);
			System.out.println(ComputerDAO.getComputerDAO().getById(576));
			ComputerDAO.getComputerDAO().deleteById(576);
		
		}
}
