package com.eficaztech.sismed.enums;

public enum MotivoEncerramentoAtendimento {

	C_41("41", "Óbito com declaração de óbito fornecida pelo médico assistente"), C_42("42", "Óbito com declaração de Óbito fornecida pelo Instituto Médico Legal - IML/Emergência"), C_43("43", "Óbito com declaração de Óbito fornecida pelo Serviço de Verificação de Óbito - SVO");

	private String codigo;
	private String nome;

	MotivoEncerramentoAtendimento(String codigo, String nome) {
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
