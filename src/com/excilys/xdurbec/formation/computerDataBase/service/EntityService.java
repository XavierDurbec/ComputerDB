package com.excilys.xdurbec.formation.computerDataBase.service;

import java.sql.SQLException;
import java.util.List;

public interface EntityService<Entity> {
	public Entity getById(int id) throws SQLException;
	public List<Entity> getAll() throws SQLException;
	public void create(Entity entity) throws SQLException;
	public void update(Entity entity) throws SQLException;
	public void deleteById(int id) throws SQLException;
}
