package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet{
	private static ComputerService computerService = ComputerService.getComputerService();
	private int nbComputerByPage = 20;
	private int pageNb = 1;

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		try {
			request.setAttribute("computerCount", computerService.getAll().size());

			request.setAttribute("computerList", ComputerMapperDTO
					.toComputerDTOList(computerService.getComputerPage(pageNb, nbComputerByPage).getComputerList()));
			
			//request.setAttribute("computerList", computerService.getComputerPage(pageNb, nbComputerByPage).getComputerList());
		} catch (ExceptionService e) {
			e.printStackTrace();
			System.out.println("Salut 0: " + e.getMessage());
		}

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
