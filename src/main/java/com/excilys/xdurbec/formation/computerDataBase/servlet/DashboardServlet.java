package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		String ui = "iho";
		request.setAttribute("a",ui);
		this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}	




	//	private CompanyDTO companyToDTO(Company company) {
	//		CompanyDTO companyDTO = new CompanyDTO();
	//		companyDTO.setId(company.getId());
	//		companyDTO.setName(company.getName());
	//		return companyDTO;
	//	}
	//
	//	private Company dtoToCompany(CompanyDTO companyDTO) {
	//		Company company =  new Company();
	//		company.setId(companyDTO.getId());
	//		company.setName(companyDTO.getName());
	//		return company;
	//	}
}
