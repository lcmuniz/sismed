package com.eficaztech.sismed.model;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class EmpresaDao {

	public void save(Empresa empresa) throws ValidationException {
		Validations.validate(empresa);
		if (empresa.getId() == null) {
			SismedServer.ebean.save(empresa);
		} else {
			SismedServer.ebean.update(empresa);
		}
	}

	public Empresa find(Long id) {
		return SismedServer.ebean.find(Empresa.class, id);
	}

	public Empresa findFirst() {
		String sql = "find empresa order by id";
		Query<Empresa> query = SismedServer.ebean.createQuery(Empresa.class, sql);
		return query.findList().get(0);
	}

}
