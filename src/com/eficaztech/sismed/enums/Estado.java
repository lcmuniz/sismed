package com.eficaztech.sismed.enums;

public enum Estado {

	RO("11", "RO", "Rondônia"), AC("12", "AC", "Acre"), AM("13", "AM", "Amazonas"), RR("14", "RR", "Roraima"), PA("15", "PA", "Pará"), AP("16", "AP", "Amapá"), TO("17", "TO", "Tocantinos"), MA("21", "MA", "Maranhão"), PI("22", "PI", "Piauí"), CE("23", "CE", "Ceará"), RN("24", "RN", "Rio Grande do Norte"), PB("25", "PB", "Paraíba"), PE("26", "PE", "Pernanbuco"), AL("27", "AL", "Alagoas"), SE("28", "SE", "Sergipe"), BA("29", "BA", "Bahia"), MG("31", "MG", "Minas Gerais"), ES("32", "ES",
			"Espírito Santo"), RJ("33", "RJ", "Rio de Janeiro"), SP("35", "SP", "São Paulo"), PR("41", "PR", "Paraná"), SC("42", "SC", "Santa Catarina"), RS("43", "RS", "Rio Grande do Sul"), MS("50", "MS", "Mato Grosso do Sul"), MT("51", "MT", "Mato Grosso"), GO("52", "GO", "Goiás"), DF("53", "DF", "Distrito Federal"), EX("98", "EX", "Países Estrangeiros");

	private String codigo;
	private String sigla;
	private String nome;

	Estado(String codigo, String sigla, String nome) {
		this.setCodigo(codigo);
		this.setSigla(sigla);
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
