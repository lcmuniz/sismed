package com.eficaztech.sismed.enums;

public enum TabelaConsulta {

	C_18("18", "18 - TUSS - Taxas hospitalares, diárias e gases medicinais"), C_19("19", "19 - TUSS - Materiais"), C_20("20", "20 - TUSS - Medicamentos"), C_22("22", "22 - TUSS - Procedimentos e eventos em saúde (medicina, odonto e demais áreas de saúde)"), C_90("90", "90 - Tabela Própria Pacote Odontológico"), C_98("98", "98 - Tabela Própria de Pacotes"), C_00("00", "00 - Tabela Própria das Operadoras");

	private String codigo;
	private String nome;

	TabelaConsulta(String codigo, String nome) {
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
