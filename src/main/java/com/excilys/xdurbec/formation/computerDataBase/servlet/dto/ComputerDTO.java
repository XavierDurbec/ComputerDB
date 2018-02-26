package com.excilys.xdurbec.formation.computerDataBase.servlet.dto;

import com.excilys.xdurbec.formation.computerDataBase.dao.CompanyDAO;

public class ComputerDTO {
			private int id;
			private String name;
			private String introduced;
			private String discoutinued;
			private CompanyDAO company;
			
			
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getIntroduced() {
				return introduced;
			}
			public void setIntroduced(String introduced) {
				this.introduced = introduced;
			}
			public String getDiscoutinued() {
				return discoutinued;
			}
			public void setDiscoutinued(String discoutinued) {
				this.discoutinued = discoutinued;
			}
			public CompanyDAO getCompany() {
				return company;
			}
			public void setCompany(CompanyDAO company) {
				this.company = company;
			}
			
			
}
