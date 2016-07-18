package com.eficaztech.sismed.model;

import java.util.List;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class ClienteDao {

	public void save(Cliente cliente) throws ValidationException {
		Validations.validate(cliente);
		if (cliente.getId() == null) {
			SismedServer.ebean.save(cliente);
		} else {
			SismedServer.ebean.update(cliente);
		}
	}

	public Cliente find(Long id) {
		return SismedServer.ebean.find(Cliente.class, id);
	}

	public void delete(Cliente cliente) {
		SismedServer.ebean.delete(cliente);
	}

	public List<Cliente> all() {
		Query<Cliente> query = SismedServer.ebean.createQuery(Cliente.class).orderBy("nome");
		return query.findList();
	}

	public List<Cliente> find(String filtro) {
		String sql = "find cliente where nome like :filtro order by nome";
		Query<Cliente> query = SismedServer.ebean.createQuery(Cliente.class, sql).setParameter("filtro", "%" + filtro.trim() + "%");
		return query.findList();
	}
	
}
