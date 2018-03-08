package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet{
	private static ComputerService computerService = ComputerService.getComputerService();

	private int nbComputerByPage = 20;
	private int pageNb = 1; 
	private Logger log = LogManager.getLogger(this.getClass());
	private String filter = ""; 

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			String pageString = request.getParameter(ServletString.PAGE);
			if (pageString != null) {
				pageNb = Integer.valueOf(pageString);
			}
			String nbComputerByPageString = request.getParameter(ServletString.NB_COMPUTER_BY_PAGE);
			if (nbComputerByPageString != null) {
				nbComputerByPage = Integer.valueOf(nbComputerByPageString);
			}
			String filterTmp = request.getParameter("search");
			if (filterTmp != null) {
				filter = filterTmp;
			}
			int nbComputerPage = getNbComputerPage(filter);
			request.setAttribute("searchValue", filter);
			request.setAttribute(ServletString.COMPUTER_COUNT, computerService.getComputerNumber(filter));
			request.setAttribute(ServletString.COMPUTER_LIST, ComputerMapperDTO
					.toComputerDTOList(computerService.getComputerPage(pageNb, nbComputerByPage, filter).getComputerList()));
			request.setAttribute(ServletString.MAX_PAGE, nbComputerPage);
			if (pageNb > nbComputerPage) {
				pageNb = 1;
			}
			request.setAttribute(ServletString.PAGE_NB, this.pageNb);
		} catch (ExceptionService e) {
			log.error(e.getMessage());
		}

		this.getServletContext().getRequestDispatcher(ServletString.CONTEXT_DASHBOARD).forward(request, response);
	}	

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String computerListToDelete = request.getParameter("selection");
		if (!computerListToDelete.equals("")) {
			for (String computerIdString : computerListToDelete.split(",")) {
				try {
					computerService.deleteById(Integer.valueOf(computerIdString));
				} catch (NumberFormatException | ExceptionService e) {
					e.printStackTrace();
				}
			}
		}
		try {
			response.sendRedirect(ServletString.DASHBOARD);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private int getNbComputerPage(String filter) {
		int nbPage;
		try {
			nbPage = computerService.getComputerNumber(filter) / this.nbComputerByPage;

			if (computerService.getComputerNumber(filter) % this.nbComputerByPage != 0) {
				nbPage++;
			}
			return nbPage;  
		} catch (ExceptionService e) {
			log.error(e.getMessage());
			return 0;
		}
	}
}

