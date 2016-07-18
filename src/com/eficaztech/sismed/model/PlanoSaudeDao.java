package com.eficaztech.sismed.model;

import java.util.List;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class PlanoSaudeDao {

	public void save(PlanoSaude ps) throws ValidationException {
		Validations.validate(ps);
		if (ps.getId() == null) {
			SismedServer.ebean.save(ps);
		} else {
			SismedServer.ebean.update(ps);
		}
	}

	public PlanoSaude find(Long id) {
		return SismedServer.ebean.find(PlanoSaude.class, id);
	}

	public void delete(PlanoSaude ps) {
		SismedServer.ebean.delete(ps);
	}

	public List<PlanoSaude> all() {
		Query<PlanoSaude> query = SismedServer.ebean.createQuery(PlanoSaude.class).orderBy("nome");
		return query.findList();
	}

	public List<PlanoSaude> find(String filtro) {
		String sql = "find planosaude where nome like :filtro order by nome";
		Query<PlanoSaude> query = SismedServer.ebean.createQuery(PlanoSaude.class, sql).setParameter("filtro", "%" + filtro + "%");
		return query.findList();
	}

}
