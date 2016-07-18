package com.eficaztech.sismed.enums;

public enum IndicacaoAcidente {

	C_0("0", "Trabalho"), C_1("1", "Trânsito"), C_2("2", "Outros Acidentes"), C_9("9", "Não Acidente");

	private String codigo;
	private String nome;

	IndicacaoAcidente(String codigo, String nome) {
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
