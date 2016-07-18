package com.eficaztech.sismed.enums;

public enum CD {

	C_01("01", "Gases Medicinais"), C_02("02", "Medicamento"), C_03("03", "Materiais"), C_07("07", "Taxas e aluguéis"), C_08("08", "OPME");

	private String codigo;
	private String nome;

	CD(String codigo, String nome) {
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
