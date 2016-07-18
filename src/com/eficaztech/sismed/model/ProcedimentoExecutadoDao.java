package com.eficaztech.sismed.model;

import com.eficaztech.sismed.util.SismedServer;

public class ProcedimentoExecutadoDao {

	public ProcedimentoExecutado find(Long id) {
		return SismedServer.ebean.find(ProcedimentoExecutado.class, id);
	}

}
