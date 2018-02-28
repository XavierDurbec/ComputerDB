package com.excilys.xdurbec.formation.computerDataBase.servlet.dto;

import com.excilys.xdurbec.formation.computerDataBase.model.Company;

public class CompanyMapperDTO {
	
	public static Company toCompany(CompanyDTO companyDTO) {
		Company company = new Company(companyDTO.getName());
		company.setId(companyDTO.getId());
		return company;
	}

	public static CompanyDTO toCompanyDTO(Company company) {
		
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
		return companyDTO;
	}
}
