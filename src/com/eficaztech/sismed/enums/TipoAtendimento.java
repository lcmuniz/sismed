package com.eficaztech.sismed.enums;

public enum TipoAtendimento {

	C_01("01", "Remoção"), C_02("02", "Pequena Cirurgia"), C_03("03", "Terapias"), C_04("04", "Consulta"), C_05("05", "Exames (englobando exame radiológico)"), C_06("06", "Atendimento Domiciliar"), C_07("07", "Internação"), C_08("08", "Quimioterapia"), C_09("09", "Radioterapia"), C_10("10", "Terapia Renal Substitutiva (TRS)"), C_11("11", "Pronto Socorro"), C_13("13", "Pequenos atendimentos"), C_14("14", "Saúde Ocupacional - Admissional"), C_15("15", "Saúde Ocupacional - Demissional"), C_16(
			"16", "Saúde Ocupacional - Periódico"), C_17("17", "Saúde Ocupacional - Retorno ao trabalho"), C_18("18", "Saúde Ocupacional - Mudança de função"), C_19("19", "Saúde Ocupacional - Promoção a saúde"), C_20("20", "Saúde Ocupacional - Beneficiário novo"), C_21("21", "Saúde Ocupacional - Assistência a demitidos");

	private String codigo;
	private String nome;

	TipoAtendimento(String codigo, String nome) {
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
