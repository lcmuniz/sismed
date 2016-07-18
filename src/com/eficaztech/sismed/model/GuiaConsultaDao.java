package com.eficaztech.sismed.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class GuiaConsultaDao {

	public void save(GuiaConsulta guia) throws ValidationException {
		Validations.validate(guia);
		if (guia.getId() == null) {
			SismedServer.ebean.save(guia);
		} else {
			SismedServer.ebean.update(guia);
		}
	}

	public GuiaConsulta find(Long id) {
		return SismedServer.ebean.find(GuiaConsulta.class, id);
	}

	public void delete(GuiaConsulta guia) {
		SismedServer.ebean.delete(guia);
	}

	public List<GuiaConsulta> all() {
		Query<GuiaConsulta> query = SismedServer.ebean.createQuery(GuiaConsulta.class).orderBy("dataEmissaoGuia");
		return query.findList();
	}

	public List<GuiaConsulta> find(String cliente, Date inicio, Date fim) {

		Calendar ci = Calendar.getInstance();
		if (inicio == null) {
			ci.set(0001, 01, 01);
		} else {
			ci.setTime(inicio);
		}

		Calendar cf = Calendar.getInstance();
		if (fim == null) {
			cf.set(5000, 12, 31);
		} else {
			cf.setTime(fim);
		}

		ci.set(Calendar.HOUR_OF_DAY, 0);
		ci.set(Calendar.MINUTE, 0);
		ci.set(Calendar.SECOND, 0);

		cf.set(Calendar.HOUR_OF_DAY, 23);
		cf.set(Calendar.MINUTE, 59);
		cf.set(Calendar.SECOND, 59);

		String sql = "find guiaconsulta where (cliente.nome like :cliente) and (dataEmissaoGuia between :inicio and :fim) order by dataEmissaoGuia";

		Query<GuiaConsulta> query = SismedServer.ebean.createQuery(GuiaConsulta.class, sql).setParameter("cliente", "%" + cliente.trim() + "%").setParameter("inicio", ci.getTime()).setParameter("fim", cf.getTime());

		return query.findList();
	}

	public void removerGuiasDoLote(Lote loteSelecionado) {
		String dml = "update guia_consulta set lote_id = null where lote_id = :lote_id";
		SqlUpdate update = SismedServer.ebean.createSqlUpdate(dml).setParameter("lote_id", loteSelecionado.getId());
		update.execute();
	}

	public void colocarGuiasNoLote(Lote loteSelecionado, List<GuiaConsulta> guiasConsulta) {

		if (guiasConsulta.isEmpty()) {
			return;
		}

		// formatar uma strind de ids: 1,2,3,4
		String ids = "";
		for (GuiaConsulta guiaConsulta : guiasConsulta) {
			ids = ids + guiaConsulta.getId() + ",";
		}
		ids = ids + "#";
		ids = ids.replace(",#", "");

		String dml = "update guia_consulta set lote_id = " + loteSelecionado.getId() + " where id in (" + ids + ")";
		SqlUpdate update = SismedServer.ebean.createSqlUpdate(dml);
		update.execute();
	}

	// encontra todas as guias que nao possuem lote ou o lote eh igual ao
	// parametro lote
	// e que atendem as criterios informados
	public List<GuiaConsulta> find(Lote lote, Medico medico, PlanoSaude planoSaude, Date inicio, Date fim) {

		Calendar ci = Calendar.getInstance();
		if (inicio == null) {
			ci.set(0001, 01, 01);
		} else {
			ci.setTime(inicio);
		}

		Calendar cf = Calendar.getInstance();
		if (fim == null) {
			cf.set(5000, 12, 31);
		} else {
			cf.setTime(fim);
		}

		ci.set(Calendar.HOUR_OF_DAY, 0);
		ci.set(Calendar.MINUTE, 0);
		ci.set(Calendar.SECOND, 0);

		cf.set(Calendar.HOUR_OF_DAY, 23);
		cf.set(Calendar.MINUTE, 59);
		cf.set(Calendar.SECOND, 59);

		// primeira condicao = lote igual ao passado ou nulo
		String primeira_condicao = "( lote_id is null or lote_id = 0 or lote_id = " + lote.getId() + " )";
		// segunda condicao = parametros passados
		String segunda_condicao = "( (planoSaude.id = :planoSaude) and (dataEmissaoGuia between :inicio and :fim) )";

		String sql = "find guiaconsulta where  " + primeira_condicao + " and " + segunda_condicao + " order by dataEmissaoGuia";
		Query<GuiaConsulta> query = SismedServer.ebean.createQuery(GuiaConsulta.class, sql).setParameter("medico", medico.getId()).setParameter("planoSaude", planoSaude.getId()).setParameter("inicio", ci.getTime()).setParameter("fim", cf.getTime());

		return query.findList();
	}

}
