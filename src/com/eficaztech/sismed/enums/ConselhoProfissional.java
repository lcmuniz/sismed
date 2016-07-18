package com.eficaztech.sismed.enums;

public enum ConselhoProfissional {

	CRAS("1", "Conselho Regional de Assistência Social"), COREN("2", "Conselho Regional de Enfermagem"), CRF("3", "Conselho Regional de Farmácia"), CRFA("4", "Conselho Regional de Fonoaudiologia"), CREFITO("5", "Conselho Regional de Fsioterapia e Terapia Ocupacional"), CRM("6", "Conselho Regional de Medina"), CRN("7", "Conselho Regional de Nutrição"), CRO("8", "Conselho Regional de Odontologia"), CRP("9", "Conselho Regional de Psicologia"), OUT("10", "Outros Conselhos");

	private String codigo;
	private String nome;

	ConselhoProfissional(String codigo, String nome) {
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
