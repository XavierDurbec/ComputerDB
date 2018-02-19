package com.excilys.xdurbec.formation.computerDataBase.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;
import com.excilys.xdurbec.formation.computerDataBase.dao.ExceptionDAO;
import com.excilys.xdurbec.formation.computerDataBase.model.Company;

public class CompanyPage {
		final private int PAGE_NUMBER;
		final private int NB_COMPANY_PER_PAGE;
		private List<Company> companyList = new ArrayList<Company>();
		
		public CompanyPage(int pageNulber, int nbCompanyPerPage) throws ExceptionService {
			this.PAGE_NUMBER = pageNulber;
			this.NB_COMPANY_PER_PAGE = nbCompanyPerPage;
			refresh();
		}
		
		public void refresh() throws ExceptionService {
			try {
				this.companyList = CompanyDAO.getCompanyDAO().getAllPage(PAGE_NUMBER, NB_COMPANY_PER_PAGE);
			} catch (ExceptionDAO e) {
					throw new ExceptionService(ExceptionService.GET_ALL_ERROR_PAGE);
			}
		}

		@Override
		public String toString() {
			return "CompanyPage [companyList=" + companyList + "]";
		}
		
		
}
