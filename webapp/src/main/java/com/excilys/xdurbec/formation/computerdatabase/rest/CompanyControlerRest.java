package com.excilys.xdurbec.formation.computerdatabase.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.xdurbec.formation.computerdatabase.dto.CompanyDTO;
import com.excilys.xdurbec.formation.computerdatabase.dto.CompanyMapperDTO;
import com.excilys.xdurbec.formation.computerdatabase.model.Company;
import com.excilys.xdurbec.formation.computerdatabase.model.Computer;
import com.excilys.xdurbec.formation.computerdatabase.service.CompanyService;
import com.excilys.xdurbec.formation.computerdatabase.service.ComputerService;
import com.excilys.xdurbec.formation.computerdatabase.service.ExceptionService;

@RestController
public class CompanyControlerRest {

	private  Logger log = LogManager.getLogger(this.getClass());

	private CompanyService companyService;

	public CompanyControlerRest(CompanyService companyService) {
		this.companyService = companyService;
	}

	@GetMapping(value="/companies")
	public  List<CompanyDTO> findAllCompanies() {
		List<CompanyDTO> companiesDTO = new ArrayList<>();
		try {
			companiesDTO = CompanyMapperDTO.toCompanyDTOList(companyService.getAll());
		} catch (ExceptionService e) {
			log.error(e);
		}
		return companiesDTO;
	}

	@GetMapping(value="/company/{id}")
	public  CompanyDTO findCompanieById(@PathVariable("id") int id) {
		try {
			return CompanyMapperDTO.toCompanyDTO(companyService.getCompanyById(id));
		} catch (ExceptionService e) {
			log.error(e);
		}
		return null;
	}

	@DeleteMapping(value="/company/delete/{id}")
	public void  deleteCompany(@PathVariable("id") int id) {
			companyService.deleteCompany(id);
	}


	@PostMapping("/company/create")
	public HttpStatus createCompany(@RequestBody CompanyDTO company) {
		companyService.createCompany(CompanyMapperDTO.toCompany(company));
		return HttpStatus.OK;
	}

	@PutMapping("/company/update")
	public HttpStatus updateCompany(@RequestBody CompanyDTO company) {
		companyService.updateCompany(CompanyMapperDTO.toCompany(company));
		return HttpStatus.OK;
	}
}
