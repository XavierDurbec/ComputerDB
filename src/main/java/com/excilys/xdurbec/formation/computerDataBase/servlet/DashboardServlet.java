package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerAttributes;
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
	private ComputerAttributes orderBy =  ComputerAttributes.ID;
	private Boolean ascendingOrder = true;

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			String nbComputerByPageString = request.getParameter(ServletString.NB_COMPUTER_BY_PAGE);
			if (nbComputerByPageString != null) {
				nbComputerByPage = Integer.valueOf(nbComputerByPageString);
			}
			String pageString = request.getParameter(ServletString.PAGE);
			if (pageString != null) {
				pageNb = Integer.valueOf(pageString);
			}
			String filterTmp = request.getParameter(ServletString.NAME_SEARCHED);
			if (filterTmp != null) {
				filter = filterTmp;
			}
			int nbComputerPage = getNbComputerPage(filter);
			if (pageNb > nbComputerPage) {
				pageNb = nbComputerPage;
			}			
			String orderByString = request.getParameter(ServletString.ORDER_TYPE);
			if (orderByString != null) {
				this.orderBySet(orderByString);
			}
			request.setAttribute(ServletString.JSP_COMPUTER_LIST, ComputerMapperDTO
					.toComputerDTOList(computerService.getComputerPage(pageNb, nbComputerByPage, filter, orderBy, ascendingOrder).getComputerList()));
			request.setAttribute(ServletString.JSP_SEARCH_VALUE, filter);
			request.setAttribute(ServletString.JSP_COMPUTER_COUNT, computerService.getComputerNumber(filter));
			request.setAttribute(ServletString.JSP_MAX_PAGE, nbComputerPage);
			request.setAttribute(ServletString.JSP_PAGE_NB, this.pageNb);
		} catch (ExceptionService e) {
			log.error(e.getMessage());
		}
		this.getServletContext().getRequestDispatcher(ServletString.CONTEXT_DASHBOARD).forward(request, response);
		System.out.println(this);
	}	


	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String computerListToDelete = request.getParameter(ServletString.COMPUTER_SELECTED);
		if (!computerListToDelete.equals(ServletString.VOID_STRING)) {
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

	private void orderBySet(String orderByString) {
		ComputerAttributes orderByTmp;
		switch (orderByString) {
		case "name":
			orderByTmp = ComputerAttributes.NAME;
			break;
		case "introduced":
			orderByTmp = ComputerAttributes.INTRODUCED;
			break;
		case "discontinued":
			orderByTmp = ComputerAttributes.DISCONTINUED;
			break;
		case "company":
			orderByTmp = ComputerAttributes.COMPANY_NAME;
			break;
		default :
			orderByTmp = ComputerAttributes.ID;
		}

		if (orderByTmp == this.orderBy) {
			this.ascendingOrder = !this.ascendingOrder;
		} else {
			this.orderBy = orderByTmp;
			this.ascendingOrder = true;
		}
	}


	@Override
	public String toString() {
		return "DashboardServlet [nbComputerByPage=" + nbComputerByPage + ", pageNb=" + pageNb + ", filter=" + filter + ", orderBy=" + orderBy + ", ascendingOrder=" + ascendingOrder + "]";
	}


}

