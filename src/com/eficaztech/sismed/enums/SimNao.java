package com.eficaztech.sismed.enums;

public enum SimNao {

	S("Sim"), N("Não");

	private String nome;

	SimNao(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}

}
