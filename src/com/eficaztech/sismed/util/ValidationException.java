package com.eficaztech.sismed.util;

import java.util.HashMap;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	public String mensagem;
	public HashMap<String, String> erros;

	public ValidationException(String mensagem, HashMap<String, String> erros) {
		this.mensagem = mensagem;
		this.erros = erros;
	}

	public HashMap<String, String> getErros() {
		return erros;
	}

}
