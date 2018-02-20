package com.excilys.xdurbec.formation.computerDataBase.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;

public class CompanyPage {
		private int pageNumber;
		private int nbCompanyPerPage;
		private List<Company> companyList = new ArrayList<Company>();

		
		public List<Company> getCompanyList() {
			return companyList;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public int getNbCompanyPerPage() {
			return nbCompanyPerPage;
		}

		public void setNbCompanyPerPage(int nbCompanyPerPage) {
			this.nbCompanyPerPage = nbCompanyPerPage;
		}

		public CompanyPage(int pageNulber, int nbCompanyPerPage) throws ExceptionService {
			this.pageNumber = pageNulber;
			this.nbCompanyPerPage = nbCompanyPerPage;
			this.refresh();
		}
		
		
		
		public void refresh() throws ExceptionService {
			try {
				this.companyList = CompanyDAO.getCompanyDAO().getAllPage(pageNumber, nbCompanyPerPage);
			} catch (ExceptionDAO e) {
					throw new ExceptionService(ExceptionService.GET_ALL_ERROR_PAGE);
			}
		}

		@Override
		public String toString() {
			return "CompanyPage [companyList=" + companyList + "]";
		}
		
		
}
