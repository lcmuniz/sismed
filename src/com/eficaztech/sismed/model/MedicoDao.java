package com.eficaztech.sismed.model;

import java.util.List;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class MedicoDao {

	public void save(Medico medico) throws ValidationException {
		Validations.validate(medico);
		if (medico.getId() == null) {
			SismedServer.ebean.save(medico);
		} else {
			SismedServer.ebean.update(medico);
		}
	}

	public Medico find(Long id) {
		return SismedServer.ebean.find(Medico.class, id);
	}

	public void delete(Medico medico) {
		SismedServer.ebean.delete(medico);
	}

	public List<Medico> all() {
		Query<Medico> query = SismedServer.ebean.createQuery(Medico.class).orderBy("nome");
		return query.findList();
	}

	public List<Medico> find(String filtro) {
		String sql = "find medico where nome like :filtro order by nome";
		Query<Medico> query = SismedServer.ebean.createQuery(Medico.class, sql).setParameter("filtro", "%" + filtro + "%");
		return query.findList();
	}

}
