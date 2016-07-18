package com.eficaztech.sismed.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class ConsultaDao {

	public void save(Consulta consulta) throws ValidationException {
		Validations.validate(consulta);
		if (consulta.getId() == null) {
			SismedServer.ebean.save(consulta);
		} else {
			SismedServer.ebean.update(consulta);
		}
	}

	public Consulta find(Long id) {
		return SismedServer.ebean.find(Consulta.class, id);
	}

	public void delete(Consulta consulta) {
		SismedServer.ebean.delete(consulta);
	}

	public List<Consulta> all() {
		Query<Consulta> query = SismedServer.ebean.createQuery(Consulta.class).orderBy("data desc, hora desc");
		return query.findList();
	}

	public List<Consulta> find(String cliente, Date inicio, Date fim, Boolean mostrarRetornos) {

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

		String whereRetornos = "and (1=1)";
		if (!mostrarRetornos) {
			whereRetornos = "and (retorno = 'N')";
		}

		String sql = "find consulta where (cliente.nome like :cliente) and (data between :inicio and :fim) " + whereRetornos + " order by data desc, hora desc";

		Query<Consulta> query = SismedServer.ebean.createQuery(Consulta.class, sql).setParameter("cliente", "%" + cliente.trim() + "%").setParameter("inicio", ci.getTime()).setParameter("fim", cf.getTime());

		return query.findList();
	}

	public List<Consulta> findConsultasAnteriores(Consulta consulta) {
		Cliente cliente = consulta.getCliente();

		if (cliente == null) return new ArrayList<Consulta>();

		String sql = "find consulta where cliente.id = :cliente_id and data < :data order by data desc, hora desc";
		Query<Consulta> query = SismedServer.ebean.createQuery(Consulta.class, sql).setParameter("cliente_id", cliente.getId()).setParameter("data", consulta.getData());
		return query.findList();
	}

	public Consulta findUltimaConsulta(Cliente cliente, PlanoSaude plano) {
		String sql = "find consulta where cliente.id = :cliente and planoSaude.id = :plano order by data desc, hora desc";
		Query<Consulta> query = SismedServer.ebean.createQuery(Consulta.class, sql).setParameter("cliente", cliente.getId()).setParameter("plano", plano.getId());
		List<Consulta> consultas = query.findList();
		if (consultas.size() == 0) return null;
		return consultas.get(0);
	}

}
