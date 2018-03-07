package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.xdurbec.formation.computerDataBase.service.CompanyService;
import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyMapperDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;


@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet{
	private ComputerService computerService = ComputerService.getComputerService();
	private CompanyService companyService = CompanyService.getCompanyService();
	private int computerId;
	private ComputerDTO computerDTO;

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		String computerIdString = request.getParameter("id");
		if (computerIdString != null) {
			computerId = Integer.valueOf(computerIdString);
		}
		try {
			computerDTO = ComputerMapperDTO.toComputerDTO(computerService.getById(computerId));
			request.setAttribute("computer", computerDTO);
			request.setAttribute("companyList", CompanyMapperDTO.toCompanyDTOList(companyService.getAll()));
		} catch (ExceptionService e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request, response);
	}	



	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(computerId);
		computerDTO.setName(request.getParameter("computerName"));
		computerDTO.setIntroduced(request.getParameter("introduced"));
		computerDTO.setDiscontinued(request.getParameter("discontinued"));
		String computerCompanyIdString = request.getParameter("companyId");
		try {
			if (!"0".equals(computerCompanyIdString)) {
				computerDTO.setCompany(CompanyMapperDTO.toCompanyDTO(companyService.getCompanyById(Integer.valueOf(computerCompanyIdString))));
			}
			computerService.update(ComputerMapperDTO.toComputer(computerDTO));
			response.sendRedirect(ServletString.DASHBOARD);
		} catch (NumberFormatException | ExceptionService | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}


