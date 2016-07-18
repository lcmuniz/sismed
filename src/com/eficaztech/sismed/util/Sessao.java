package com.eficaztech.sismed.util;

import org.zkoss.zk.ui.Executions;

public class Sessao {

	public static final String USUARIO = "USUARIO";
	public static final String FUNCAO = "FUNCAO";

	public static void set(String attribute, Object value) {
		Executions.getCurrent().getSession().setAttribute(attribute, value);
	}

	public static Object get(String attribute) {
		return Executions.getCurrent().getSession().getAttribute(attribute);
	}

}
