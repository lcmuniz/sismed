package com.eficaztech.sismed.enums;

public enum StatusConsulta {

	MAR("Marcada"), CON("Confirmada"), CAN("Cancelada");

	private String nome;

	StatusConsulta(String nome) {
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
