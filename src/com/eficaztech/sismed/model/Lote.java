package com.eficaztech.sismed.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Lote {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull(message = "O número do lote deve ser informado.")
	@Column(nullable = false)
	private Integer numeroLote;

	@NotNull(message = "O plano de saúde deve ser informado.")
	@ManyToOne
	private PlanoSaude planoSaude;

	@NotNull(message = "O médico deve ser informado.")
	@ManyToOne
	private Medico medico;

	@NotNull(message = "A data de envio deve ser informada.")
	@Column
	private Date dataEnvio;

	@NotNull(message = "O tipo do lote ser informado.")
	@Column(length = 9, nullable = false)
	private String tipoLote;

	@OneToMany(mappedBy = "lote")
	private List<GuiaConsulta> guiasConsulta;

	@OneToMany(mappedBy = "lote")
	private List<GuiaSADT> guiasSADT;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<GuiaConsulta> getGuiasConsulta() {
		return guiasConsulta;
	}

	public void setGuiasConsulta(List<GuiaConsulta> guiasConsulta) {
		this.guiasConsulta = guiasConsulta;
	}

	public List<GuiaSADT> getGuiasSADT() {
		return guiasSADT;
	}

	public void setGuiasSADT(List<GuiaSADT> guiasSADT) {
		this.guiasSADT = guiasSADT;
	}

	public String getDataEnvioBR() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(getDataEnvio());
	}

	public String getDataEnvioYMD() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(getDataEnvio());
	}

	public String getDataEnvioHMS() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(getDataEnvio());
	}

	public PlanoSaude getPlanoSaude() {
		return planoSaude;
	}

	public void setPlanoSaude(PlanoSaude planoSaude) {
		this.planoSaude = planoSaude;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getTipoLote() {
		return tipoLote;
	}

	public void setTipoLote(String tipoLote) {
		this.tipoLote = tipoLote;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Lote other = (Lote) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

	public Integer getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(Integer numeroLote) {
		this.numeroLote = numeroLote;
	}

	public String getCaptionGuiasConsulta() {
		return "Guias de Consulta: " + guiasConsulta.size();
	}

	public String getCaptionGuiasSADT() {
		return "Guias SADT: " + guiasSADT.size();
	}

	public String xml() {

		String xml = "";

		xml += "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		xml += "<ans:mensagemTISS xsi:schemaLocation=\"http://www.ans.gov.br/padroes/tiss/schemas tissV3_02_00.xsd\" xmlns:ans=\"http://www.ans.gov.br/padroes/tiss/schemas\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
		xml += "<ans:cabecalho>";
		xml += "<ans:identificacaoTransacao>";
		xml += "<ans:tipoTransacao>ENVIO_LOTE_GUIAS</ans:tipoTransacao>";
		xml += "<ans:sequencialTransacao>" + this.numeroLote + "</ans:sequencialTransacao>";
		xml += "<ans:dataRegistroTransacao>" + this.getDataEnvioYMD() + "</ans:dataRegistroTransacao>";
		xml += "<ans:horaRegistroTransacao>" + this.getDataEnvioHMS() + "</ans:horaRegistroTransacao>";
		xml += "</ans:identificacaoTransacao>";
		xml += "<ans:origem>";
		xml += "<ans:identificacaoPrestador>";
		xml += "<ans:CPF>" + this.medico.getCpf() + "</ans:CPF>";
		xml += "</ans:identificacaoPrestador>";
		xml += "</ans:origem>";
		xml += "<ans:destino>";
		xml += "<ans:registroANS>" + this.planoSaude.getRegistroANS() + "</ans:registroANS>";
		xml += "</ans:destino>";
		xml += "<ans:versaoPadrao>3.02.00</ans:versaoPadrao>";
		xml += "</ans:cabecalho>";
		xml += "<ans:prestadorParaOperadora>";
		xml += "<ans:loteGuias>";
		xml += "<ans:numeroLote>" + this.numeroLote + "</ans:numeroLote>";
		xml += "<ans:guiasTISS>";

		if (this.tipoLote.equals("CONSULTA")) {

			// para cada guia
			for (GuiaConsulta guia : guiasConsulta) {

				xml += "<ans:guiaConsulta>";
				xml += "<ans:cabecalhoConsulta>";
				xml += "<ans:registroANS>" + this.planoSaude.getRegistroANS() + "</ans:registroANS>";
				xml += "<ans:numeroGuiaPrestador>" + guia.getNumeroGuia() + "</ans:numeroGuiaPrestador>";
				xml += "</ans:cabecalhoConsulta>";
				xml += "<ans:numeroGuiaOperadora>" + guia.getNumeroGuia() + "</ans:numeroGuiaOperadora>";
				xml += "<ans:dadosBeneficiario>";
				xml += "<ans:numeroCarteira>" + guia.getCarteira() + "</ans:numeroCarteira>";
				xml += "<ans:atendimentoRN>" + guia.getAtendimentoRN().name() + "</ans:atendimentoRN>";
				xml += "<ans:nomeBeneficiario>" + guia.getCliente().getNome() + "</ans:nomeBeneficiario>";
				xml += "</ans:dadosBeneficiario>";
				xml += "<ans:contratadoExecutante>";
				xml += "<ans:cpfContratado>" + guia.getMedico().getCpf() + "</ans:cpfContratado>";
				xml += "<ans:nomeContratado>" + guia.getMedico().getNome() + "</ans:nomeContratado>";
				xml += "<ans:CNES>" + guia.getMedico().getNumeroCNES() + "</ans:CNES>";
				xml += "</ans:contratadoExecutante>";
				xml += "<ans:profissionalExecutante>";
				xml += "<ans:nomeProfissional>" + guia.getMedico().getNome() + "</ans:nomeProfissional>";
				xml += "<ans:conselhoProfissional>" + guia.getMedico().getConselhoProfissional().getCodigo() + "</ans:conselhoProfissional>";
				xml += "<ans:numeroConselhoProfissional>" + guia.getMedico().getNumeroConselho() + "</ans:numeroConselhoProfissional>";
				xml += "<ans:UF>" + guia.getMedico().getEstadoConselho().getCodigo() + "</ans:UF>";
				xml += "<ans:CBOS>" + guia.getMedico().getCbos() + "</ans:CBOS>";
				xml += "</ans:profissionalExecutante>";
				xml += "<ans:indicacaoAcidente>" + guia.getIndicacaoAcidente().getCodigo() + "</ans:indicacaoAcidente>";
				xml += "<ans:dadosAtendimento>";
				xml += "<ans:dataAtendimento>" + guia.getDataAtendimentoYMD() + "</ans:dataAtendimento>";
				xml += "<ans:tipoConsulta>" + guia.getTipoConsulta().getCodigo() + "</ans:tipoConsulta>";
				xml += "<ans:procedimento>";
				xml += "<ans:codigoTabela>" + guia.getTabelaConsulta().getCodigo() + "</ans:codigoTabela>";
				xml += "<ans:codigoProcedimento>" + guia.getCodigoProcedimento() + "</ans:codigoProcedimento>";
				xml += "<ans:valorProcedimento>" + guia.getValorProcedimento() + "</ans:valorProcedimento>";
				xml += "</ans:procedimento>";
				xml += "</ans:dadosAtendimento>";
				if (guia.getObservacoes() != null) {
					xml += "<ans:observacao>" + guia.getObservacoes() + "</ans:observacao>";
				}
				xml += "</ans:guiaConsulta>";

			}

		} else {

			// para cada guia
			for (GuiaSADT guia : guiasSADT) {

				xml += "<ans:guiaSP-SADT>";
				xml += "<ans:cabecalhoGuia>";
				xml += "<ans:registroANS>" + this.planoSaude.getRegistroANS() + "</ans:registroANS>";
				xml += "<ans:numeroGuiaPrestador>" + guia.getNumeroGuia() + "</ans:numeroGuiaPrestador>";
				xml += "</ans:cabecalhoGuia>";
				
				xml += "<ans:dadosAutorizacao>";
				xml += "<ans:dataAutorizacao>" + guia.getDataAutorizacaoBR() + "</ans:dataAutorizacao>";
				xml += "<ans:senha>" + guia.getSenha() + "</ans:senha>";
				xml += "<ans:dataValidadeSenha>" + guia.getDataValidadeSenhaBR() + "</ans:dataValidadeSenha>";
				xml += "</ans:dadosAutorizacao>";
				
				xml += "<ans:dadosBeneficiario>";
				xml += "<ans:numeroCarteira>" + guia.getCarteira() + "</ans:numeroCarteira>";
				xml += "<ans:atendimentoRN>" + guia.getAtendimentoRN().name() + "</ans:atendimentoRN>";
				xml += "<ans:nomeBeneficiario>" + guia.getCliente().getNome() + "</ans:nomeBeneficiario>";
				xml += "</ans:dadosBeneficiario>";
				xml += "<ans:dadosSolicitante>";
				xml += "<ans:contratadoSolicitante>";
				xml += "<ans:cpfContratado>" + guia.getMedico().getCpf() + "</ans:cpfContratado>";
				xml += "<ans:nomeContratado>" + guia.getMedico().getNome() + "</ans:nomeContratado>";
				xml += "</ans:contratadoSolicitante>";
				xml += "<ans:profissionalSolicitante>";
				xml += "<ans:nomeProfissional>" + guia.getMedico().getNome() + "</ans:nomeProfissional>";
				xml += "<ans:conselhoProfissional>" + guia.getMedico().getConselhoProfissional().getCodigo() + "</ans:conselhoProfissional>";
				xml += "<ans:numeroConselhoProfissional>" + guia.getMedico().getNumeroConselho() + "</ans:numeroConselhoProfissional>";
				xml += "<ans:UF>" + guia.getMedico().getEstadoConselho().getCodigo() + "</ans:UF>";
				xml += "<ans:CBOS>" + guia.getMedico().getCbos() + "</ans:CBOS>";
				xml += "</ans:profissionalSolicitante>";
				xml += "</ans:dadosSolicitante>";
				xml += "<ans:dadosSolicitacao>";
				xml += "<ans:dataSolicitacao>" + guia.getDataSolicitacaoYMD() + "</ans:dataSolicitacao>";
				xml += "<ans:caraterAtendimento>" + guia.getCaraterSolicitacao().getCodigo() + "</ans:caraterAtendimento>";
				xml += "</ans:dadosSolicitacao>";
				xml += "<ans:dadosExecutante>";
				xml += "<ans:contratadoExecutante>";
				xml += "<ans:cpfContratado>" + guia.getMedico().getCpf() + "</ans:cpfContratado>";
				xml += "<ans:nomeContratado>" + guia.getMedico().getNome() + "</ans:nomeContratado>";
				xml += "</ans:contratadoExecutante>";
				xml += "<ans:CNES>" + guia.getMedico().getNumeroCNES() + "</ans:CNES>";
				xml += "</ans:dadosExecutante>";
				xml += "<ans:dadosAtendimento>";
				xml += "<ans:tipoAtendimento>" + guia.getTipoAtendimento().getCodigo() + "</ans:tipoAtendimento>";
				xml += "<ans:indicacaoAcidente>" + guia.getIndicacaoAcidente().getCodigo() + "</ans:indicacaoAcidente>";
				xml += "</ans:dadosAtendimento>";
				xml += "<ans:procedimentosExecutados>";

				for (ProcedimentoExecutado proc : guia.getProcedimentosExecutados()) {
					xml += "<ans:procedimentoExecutado>";
					xml += "<ans:dataExecucao>" + proc.getDataYMD() + "</ans:dataExecucao>";
					xml += "<ans:procedimento>";
					xml += "<ans:codigoTabela>" + proc.getTabelaConsulta().getCodigo() + "</ans:codigoTabela>";
					xml += "<ans:codigoProcedimento>" + proc.getCodigoProcedimento() + "</ans:codigoProcedimento>";
					xml += "<ans:descricaoProcedimento>" + proc.getDescricao() + "</ans:descricaoProcedimento>";
					xml += "</ans:procedimento>";
					xml += "<ans:quantidadeExecutada>" + proc.getQuantidade() + "</ans:quantidadeExecutada>";
					xml += "<ans:reducaoAcrescimo>" + proc.getFatorReducaoAcrescimo() + "</ans:reducaoAcrescimo>";
					xml += "<ans:valorUnitario>" + proc.getValorUnitario() + "</ans:valorUnitario>";
					xml += "<ans:valorTotal>" + proc.getValorTotal() + "</ans:valorTotal>";
					xml += "<ans:equipeSadt>";
					xml += "<ans:codProfissional>";
					xml += "<ans:codigoPrestadorNaOperadora>" + guia.getMedico().getCpf() + "</ans:codigoPrestadorNaOperadora>";
					xml += "</ans:codProfissional>";
					xml += "<ans:nomeProf>" + guia.getMedico().getNome() + "</ans:nomeProf>";
					xml += "<ans:conselho>" + guia.getMedico().getConselhoProfissional().getCodigo() + "</ans:conselho>";
					xml += "<ans:numeroConselhoProfissional>" + guia.getMedico().getNumeroConselho() + "</ans:numeroConselhoProfissional>";
					xml += "<ans:UF>" + guia.getMedico().getEstadoConselho().getCodigo() + "</ans:UF>";
					xml += "<ans:CBOS>" + guia.getMedico().getCbos() + "</ans:CBOS>";
					xml += "</ans:equipeSadt>";
					xml += "</ans:procedimentoExecutado>";
				}
				xml += "</ans:procedimentosExecutados>";

				xml += "<ans:outrasDespesas>";
				for (OutraDespesa outra : guia.getOutrasDespesas()) {
					xml += "<ans:despesa>";
					xml += "<ans:codigoDespesa>" + outra.getCd().getCodigo() + "</ans:codigoDespesa>";
					xml += "<ans:servicosExecutados>";
					xml += "<ans:dataExecucao>" + outra.getDataYMD() + "</ans:dataExecucao>";
					xml += "<ans:codigoTabela>" + outra.getTabelaConsulta().getCodigo() + "</ans:codigoTabela>";
					xml += "<ans:codigoProcedimento>" + outra.getCodigoItem() + "</ans:codigoProcedimento>";
					xml += "<ans:quantidadeExecutada>" + outra.getQuantidade() + "</ans:quantidadeExecutada>";
					xml += "<ans:unidadeMedida>" + outra.getUnidadeMedida().getCodigo() + "</ans:unidadeMedida>";
					xml += "<ans:reducaoAcrescimo>" + outra.getFatorReducaoAcrescimo() + "</ans:reducaoAcrescimo>";
					xml += "<ans:valorUnitario>" + outra.getValorUnitario() + "</ans:valorUnitario>";
					xml += "<ans:valorTotal>" + outra.getValorTotal() + "</ans:valorTotal>";
					xml += "<ans:descricaoProcedimento>" + outra.getDescricao() + "</ans:descricaoProcedimento>";
					xml += "</ans:servicosExecutados>";
					xml += "</ans:despesa>";
				}
				xml += "</ans:outrasDespesas>";

				if (guia.getObservacoes() != null) {
					xml += "<ans:observacao>" + guia.getObservacoes() + "</ans:observacao>";
				}
				xml += "<ans:valorTotal>";
				xml += "<ans:valorProcedimentos>"+guia.getValorTotalProcedimentosExecutados()+"</ans:valorProcedimentos>";
				xml += "<ans:valorDiarias>0.00</ans:valorDiarias>";
				xml += "<ans:valorTaxasAlugueis>"+guia.getValorTotalTaxasAlugueis()+"</ans:valorTaxasAlugueis>";
				xml += "<ans:valorMateriais>"+guia.getValorTotalMateriais()+"</ans:valorMateriais>";
				xml += "<ans:valorMedicamentos>"+guia.getValorTotalMedicamentos()+"</ans:valorMedicamentos>";
				xml += "<ans:valorOPME>"+guia.getValorTotalOPME()+"</ans:valorOPME>";
				xml += "<ans:valorGasesMedicinais>"+guia.getValorTotalGasesMedicinais()+"</ans:valorGasesMedicinais>";
				xml += "<ans:valorTotalGeral>"+guia.getValorTotalGeral()+"</ans:valorTotalGeral>";
				xml += "</ans:valorTotal>";
				xml += "</ans:guiaSP-SADT>";

			}

		}

		xml += "</ans:guiasTISS>";
		xml += "</ans:loteGuias>";
		xml += "</ans:prestadorParaOperadora>";
		xml += "<ans:epilogo>";
		xml += "<ans:hash>HAS_CODE</ans:hash>";
		xml += "</ans:epilogo>";
		xml += "</ans:mensagemTISS>";

		return xml;
	}
}
