package com.excilys.xdurbec.formation.computerDataBase.service;

import java.util.List;

public interface EntityServiceComportment<Entity> {
	List<Entity> getAll() throws  ExceptionService;
	
}
