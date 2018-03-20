package com.excilys.xdurbec.formation.computerDataBase.servlet;



import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.xdurbec.formation.computerDataBase.dao.ComputerAttributes;
import com.excilys.xdurbec.formation.computerDataBase.model.ComputerPage;
import com.excilys.xdurbec.formation.computerDataBase.service.ComputerService;
import com.excilys.xdurbec.formation.computerDataBase.service.ExceptionService;
import com.excilys.xdurbec.formation.computerDataBase.servlet.dto.ComputerMapperDTO;

@Controller
public class DashboardController {
	@Autowired
	private ComputerService computerService;

	private  Logger log = LogManager.getLogger(this.getClass());

	@GetMapping("dashboard")
	public String getDashboardPage(ModelMap model, @RequestParam Map<String, String> params) {

		int nbComputerByPage  = Integer.valueOf(params.getOrDefault(ServletString.NB_COMPUTER_BY_PAGE, "20"));
		String filter = params.getOrDefault(ServletString.NAME_SEARCHED, "");
		int nbComputerPage = getNbComputerPage(filter, nbComputerByPage);
		int pageNb = Integer.valueOf(params.getOrDefault(ServletString.PAGE, "1"));
		pageNb = pageNb < nbComputerPage ? pageNb : nbComputerPage;
		ComputerAttributes orderBy = orderBySet(params.getOrDefault(ServletString.ORDER_TYPE, ""));
		String oldOrderByString = params.getOrDefault(ServletString.OLD_ORDER_TYPE, "");
		String ascendingOrderString = params.getOrDefault("orderDirection", "ASC");
		boolean ascendingOrder = orderDirectionSet(orderBy, orderBySet(oldOrderByString), ascendingOrderString);

		try {
			ComputerPage computerPage = computerService.getComputerPage(pageNb, nbComputerByPage, filter, orderBy, ascendingOrder);
			ascendingOrderString = ascendingOrder ? "ASC" : "DESC";
			model.addAttribute("orderDirection", ascendingOrderString);
			model.addAttribute("computerByPage", nbComputerByPage);
			model.addAttribute(ServletString.JSP_ORDER_VALUE, orderBy.sqlName);
			model.addAttribute(ServletString.JSP_COMPUTER_LIST, ComputerMapperDTO.toComputerDTOList(computerService.refresh(computerPage).getComputerList()));
			model.addAttribute(ServletString.JSP_SEARCH_VALUE, filter);
			model.addAttribute(ServletString.JSP_COMPUTER_COUNT, computerService.getComputerNumber(filter));
			model.addAttribute(ServletString.JSP_MAX_PAGE, nbComputerPage);
			model.addAttribute(ServletString.JSP_PAGE_NB, pageNb);
		} catch (ExceptionService e) {
			log.error(e);
		}

		return "dashboard";
	}

	@PostMapping("dashboard")
	public String deleteComputer(ModelMap model, @RequestParam Map<String, String> params) {
		String computerListToDelete = params.get(ServletString.COMPUTER_SELECTED);
		if (!computerListToDelete.equals(ServletString.VOID_STRING)) {
			for (String computerIdString : computerListToDelete.split(",")) {
				try {
					computerService.deleteById(Integer.valueOf(computerIdString));
				} catch (NumberFormatException | ExceptionService e) {
					log.error(e);
				}
			}
		}
		return "dashboard";
}
	
	
	private int getNbComputerPage(String filter, int nbComputerByPage) {
		int nbPage;
		try {
			nbPage = computerService.getComputerNumber(filter) / nbComputerByPage;
			if (computerService.getComputerNumber(filter) % nbComputerByPage != 0) {
				nbPage++;
			}
			return nbPage;  
		} catch (ExceptionService e) {
			log.error(e);
			return 0;
		}
	}

	private ComputerAttributes orderBySet(String orderByString) {
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
		return orderByTmp;
	}
	
	private boolean orderDirectionSet(ComputerAttributes orderBy, ComputerAttributes oldOrderBy, String ascendingString) {
		boolean orderDirection = ascendingString != null ? ascendingString.equals("DESC") ? false : true : true;
		orderDirection = orderBy.equals(oldOrderBy) ? !orderDirection : orderDirection;
		return orderDirection;
	}
}
