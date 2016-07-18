package com.eficaztech.sismed.model;

import java.util.List;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class MedicamentoDao {

	public void save(Medicamento medicamento) throws ValidationException {
		Validations.validate(medicamento);
		if (medicamento.getId() == null) {
			SismedServer.ebean.save(medicamento);
		} else {
			SismedServer.ebean.update(medicamento);
		}
	}

	public Medicamento find(Long id) {
		return SismedServer.ebean.find(Medicamento.class, id);
	}

	public void delete(Medicamento medicamento) {
		SismedServer.ebean.delete(medicamento);
	}

	public List<Medicamento> all() {
		Query<Medicamento> query = SismedServer.ebean.createQuery(Medicamento.class).orderBy("codigo");
		return query.findList();
	}

	public List<Medicamento> find(String filtro) {
		String sql = "find medicamento where codigo like :filtro or descricao like :filtro order by codigo";
		Query<Medicamento> query = SismedServer.ebean.createQuery(Medicamento.class, sql).setParameter("filtro", "%" + filtro + "%");
		return query.findList();
	}

}
