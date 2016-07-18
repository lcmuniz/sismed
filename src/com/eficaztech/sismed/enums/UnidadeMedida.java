package com.eficaztech.sismed.enums;

public enum UnidadeMedida {

	C_001("001", "001 - Ampola"), C_002("002", "002 - Bilhões de Uni"), C_003("003", "003 - Bisnaga"), C_004("004", "004 - Bolsa"), C_005("005", "005 - Caixa"), C_006("006", "006 - Cápsula"), C_007("007", "007 - Carpule"), C_008("008", "008 - Comprimido"), C_009("009", "008 - Dose"), C_010("010", "010 - Drágea"), C_011("011", "011 - Envelope"), C_012("012", "012 - Flaconete"), C_013("013", "013 - Frasco"), C_014("014", "014 - Frasco Ampola"), C_015("015", "015 - Galão"), C_016("016",
			"016 - Glóbulo"), C_017("017", "017 - Gotas"), C_018("018", "018 - Grama"), C_019("019", "019 - Litro"), C_020("020", "020 - Microgramas"), C_021("021", "021 - Milhões de Uni"), C_022("022", "022 - Miligrama"), C_023("023", "023 - Milímetro"), C_024("024", "024 - Óvulo"), C_025("025", "025 - Pastilha"), C_026("026", "026 - Lata"), C_027("027", "027 - Pérola"), C_028("028", "028 - Pílula"), C_029("029", "029 - Pote"), C_030("030", "030 - Quilograma"), C_031("031", "031 - Seringa"), C_032(
			"032", "032 - Supositório"), C_033("033", "033 - Tablete"), C_034("034", "034 - Tubete"), C_035("035", "035 - Tubo"), C_036("036", "036 - Unidade"), C_037("037", "037 - Unidade Intern"), C_038("038", "038 - Centímetro"), C_039("039", "039 - Conjunto"), C_040("040", "040 - Kit"), C_041("041", "041 - Maço"), C_042("042", "042 - Metro"), C_043("043", "043 - Pacote"), C_044("044", "044 - Peça"), C_045("045", "045 - Rolo"), C_046("046", "046 - Gray"), C_047("047", "047 - Centgray"), C_048(
			"048", "048 - Par"), C_049("049", "049 - Adesivo Transd"), C_050("050", "050 - Comprimido Efe"), C_051("051", "051 - Comprimido Mas"), C_052("052", "052 - Sache");

	private String codigo;
	private String nome;

	UnidadeMedida(String codigo, String nome) {
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
