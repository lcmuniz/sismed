package com.eficaztech.sismed.model;

import java.util.List;

import com.avaje.ebean.Query;
import com.avaje.ebean.SqlRow;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class LoteDao {

	public void save(Lote lote) throws ValidationException {
		Validations.validate(lote);
		if (lote.getId() == null) {
			SismedServer.ebean.save(lote);
		} else {
			SismedServer.ebean.update(lote);
		}
	}

	public Lote find(Long id) {
		return SismedServer.ebean.find(Lote.class, id);
	}

	public void delete(Lote guia) {
		SismedServer.ebean.delete(guia);
	}

	public List<Lote> all() {
		Query<Lote> query = SismedServer.ebean.createQuery(Lote.class).orderBy("dataEnvio");
		return query.findList();
	}

	public List<Lote> find(String tipoLote, String medico, String planoSaude) {
		String sql = "find lote where tipoLote = :tipoLote and medico.nome = :medico and planoSaude.nome = :planoSaude";
		Query<Lote> query = SismedServer.ebean.createQuery(Lote.class, sql).setParameter("tipoLote", tipoLote).setParameter("medico", medico).setParameter("planoSaude", planoSaude);
		return query.findList();
	}

	public int nextNumeroLote() {
		String sql = "select max(numero_lote) as maximo from lote";
		List<SqlRow> listaSqlRow = SismedServer.ebean.createSqlQuery(sql).findList();
		Integer max = listaSqlRow.get(0).getInteger("maximo");
		return 1 + (max != null ? max : 0);
	}

}