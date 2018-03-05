package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.xdurbec.formation.computerDataBase.service.CompanyService;
import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyMapperDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;



@WebServlet("/addcomputer")
public class ComputerAddServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ComputerService computerService = ComputerService.getComputerService();
	private CompanyService companyService = CompanyService.getCompanyService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		System.out.println("computerDTO");
		try {
			request.setAttribute("companyList", CompanyMapperDTO.toCompanyDTOList(companyService.getAll()));
		} catch (ExceptionService e) {
			System.out.println(e.getMessage());
		}
		this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
	}	



	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		ComputerDTO computerDTO = new ComputerDTO();
		try {
			computerDTO.setName(request.getParameter("computerName"));

			computerDTO.setIntroduced(request.getParameter("introduced"));
			computerDTO.setDiscontinued(request.getParameter("discontinued"));
			computerDTO.setCompany(getCompanyById(Integer.valueOf(request.getParameter("companyId"))));
			computerService.create(ComputerMapperDTO.toComputer(computerDTO));
			response.sendRedirect("dashboard");
		} catch (ExceptionService | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private CompanyDTO getCompanyById(int id) {
		try {
			return CompanyMapperDTO.toCompanyDTO(companyService.getCompanyById(id));
		} catch (ExceptionService e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}
