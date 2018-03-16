package com.excilys.xdurbec.formation.computerDataBase.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.excilys.xdurbec.formation.computerDataBase.service.CompanyService;
import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.CompanyMapperDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerDTO;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;



@WebServlet("/addcomputer")
public class ComputerAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private ComputerService computerService;
	@Autowired
	private CompanyService companyService;

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	    AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
	    autowireCapableBeanFactory.autowireBean(this);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			request.setAttribute(ServletString.COMPANY_LIST, CompanyMapperDTO.toCompanyDTOList(companyService.getAll()));
		} catch (ExceptionService e) {
			log.error(e.getMessage());
		}
		this.getServletContext().getRequestDispatcher(ServletString.CONTEXT_ADDCOMPUTER).forward(request, response);
	}	



	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> errors = ComputerValidator.validator(request);
			if (errors.isEmpty()) {
				ComputerDTO computerDTO = new ComputerDTO();
				computerDTO.setName(request.getParameter(ServletString.COMPUTER_NAME));
				computerDTO.setIntroduced(request.getParameter(ServletString.COMPUTER_INTRODUCED));
				computerDTO.setDiscontinued(request.getParameter(ServletString.COMPUTER_DISCONTINUED));
				computerDTO.setCompany(getCompanyById(Integer.valueOf(request.getParameter(ServletString.COMPUTER_COMPANY_ID))));
				computerService.create(ComputerMapperDTO.toComputer(computerDTO));
				response.sendRedirect(ServletString.DASHBOARD);

			} else {
				request.setAttribute(ServletString.ERRORS, errors);
				doGet(request, response);
			}
		} catch (ExceptionService | IOException | ServletException e) {
			log.error(e.getMessage());
		}
	}

	private CompanyDTO getCompanyById(int id) {
		if (id != 0) {
			try {
				return CompanyMapperDTO.toCompanyDTO(companyService.getCompanyById(id));
			} catch (ExceptionService e) {
				log.error(e.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}
}
