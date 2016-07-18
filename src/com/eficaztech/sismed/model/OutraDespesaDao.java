package com.eficaztech.sismed.model;

import com.eficaztech.sismed.util.SismedServer;

public class OutraDespesaDao {

	public OutraDespesa find(Long id) {
		return SismedServer.ebean.find(OutraDespesa.class, id);
	}

}
