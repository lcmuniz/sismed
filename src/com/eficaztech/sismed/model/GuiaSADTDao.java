package com.eficaztech.sismed.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class GuiaSADTDao {

	public void save(GuiaSADT guia) throws ValidationException {
		Validations.validate(guia);
		if (guia.getId() == null) {
			SismedServer.ebean.save(guia);
		} else {
			SismedServer.ebean.update(guia);
		}
	}

	public GuiaSADT find(Long id) {
		return SismedServer.ebean.find(GuiaSADT.class, id);
	}

	public void delete(GuiaSADT guia) {
		SismedServer.ebean.delete(guia);
	}

	public List<GuiaSADT> all() {
		Query<GuiaSADT> query = SismedServer.ebean.createQuery(GuiaSADT.class).orderBy("dataEmissaoGuia");
		return query.findList();
	}

	public List<GuiaSADT> find(String cliente, Date inicio, Date fim) {

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

		String sql = "find guiasadt where (cliente.nome like :cliente) and (dataEmissaoGuia between :inicio and :fim) order by dataEmissaoGuia";

		Query<GuiaSADT> query = SismedServer.ebean.createQuery(GuiaSADT.class, sql).setParameter("cliente", "%" + cliente.trim() + "%").setParameter("inicio", ci.getTime()).setParameter("fim", cf.getTime());

		return query.findList();
	}

	// encontra todas as guias que nao possuem lote ou o lote eh igual ao
	// parametro lote e que atendem as criterios informados
	public List<GuiaSADT> find(Lote lote, Medico medico, PlanoSaude planoSaude, Date inicio, Date fim) {

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

		String sql = "find guiasadt where  " + primeira_condicao + " and " + segunda_condicao + " order by dataEmissaoGuia";

		Query<GuiaSADT> query = SismedServer.ebean.createQuery(GuiaSADT.class, sql).setParameter("medico", medico.getId()).setParameter("planoSaude", planoSaude.getId()).setParameter("inicio", ci.getTime()).setParameter("fim", cf.getTime());

		return query.findList();
	}

	public void removerGuiasDoLote(Lote loteSelecionado) {
		String dml = "update guia_sadt set lote_id = null where lote_id = :lote_id";
		SqlUpdate update = SismedServer.ebean.createSqlUpdate(dml).setParameter("lote_id", loteSelecionado.getId());
		update.execute();
	}

	public void colocarGuiasNoLote(Lote loteSelecionado, List<GuiaSADT> guiasSADT) {

		if (guiasSADT.isEmpty()) {
			return;
		}

		// formatar uma strind de ids: 1,2,3,4
		String ids = "";
		for (GuiaSADT guiaSADT : guiasSADT) {
			ids = ids + guiaSADT.getId() + ",";
		}
		ids = ids + "#";
		ids = ids.replace(",#", "");

		String dml = "update guia_sadt set lote_id = " + loteSelecionado.getId() + " where id in (" + ids + ")";
		SqlUpdate update = SismedServer.ebean.createSqlUpdate(dml);
		update.execute();
	}

}
