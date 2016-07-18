package com.eficaztech.sismed.enums;

public enum TipoConsulta {

	C_1("1", "Primeira"), C_2("2", "Seguimento"), C_3("3", "Pré-Natal"), C_4("4", "Por encaminhamento");

	private String codigo;
	private String nome;

	TipoConsulta(String codigo, String nome) {
		this.setCodigo(codigo);
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return nome;
	}

}
