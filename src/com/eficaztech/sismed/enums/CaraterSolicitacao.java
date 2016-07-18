package com.eficaztech.sismed.enums;

public enum CaraterSolicitacao {

	C_1("1", "Eletiva"), C_2("2", "Urgência/Emergência");

	private String codigo;
	private String nome;

	CaraterSolicitacao(String codigo, String nome) {
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
