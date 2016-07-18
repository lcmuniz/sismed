package com.eficaztech.sismed.model;

import java.util.List;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class CidDao {

	public void save(Cid cid) throws ValidationException {
		Validations.validate(cid);
		if (cid.getId() == null) {
			SismedServer.ebean.save(cid);
		} else {
			SismedServer.ebean.update(cid);
		}
	}

	public void delete(Cid cid) {
		SismedServer.ebean.delete(cid);
	}

	public Cid find(Long id) {
		return SismedServer.ebean.find(Cid.class, id);
	}

	public List<Cid> all() {
		Query<Cid> query = SismedServer.ebean.createQuery(Cid.class).orderBy("cid");
		return query.findList();
	}

	public List<Cid> find(String filtro) {
		String sql = "find cid where cid like :filtro or descricao like :filtro order by cid";
		Query<Cid> query = SismedServer.ebean.createQuery(Cid.class, sql).setParameter("filtro", "%" + filtro + "%");
		return query.findList();
	}

}
