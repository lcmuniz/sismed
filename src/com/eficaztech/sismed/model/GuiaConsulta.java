package com.eficaztech.sismed.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.eficaztech.sismed.enums.IndicacaoAcidente;
import com.eficaztech.sismed.enums.SimNao;
import com.eficaztech.sismed.enums.TabelaConsulta;
import com.eficaztech.sismed.enums.TipoConsulta;

@Entity
public class GuiaConsulta {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull(message = "O plano de saúde deve ser informado.")
	@ManyToOne
	private PlanoSaude planoSaude;

	@NotEmpty(message = "A carteira deve ser informada.")
	@Column(length = 30)
	// @Pattern(regexp = "(^$|[0-9]{16})", message =
	// "A carteira deve conter apenas números e ter 16 caracteres.")
	private String carteira;

	@NotNull(message = "A validade da carteira deve ser informada.")
	@Column
	private Date validadeCarteira;

	@NotNull(message = "O cliente deve ser informado.")
	@ManyToOne
	private Cliente cliente;

	@NotNull(message = "O médico deve ser informado.")
	@ManyToOne
	private Medico medico;

	@NotEmpty(message = "O número da guia deve ser informado.")
	@Column(length = 20, nullable = false)
	private String numeroGuia;

	@NotNull(message = "A data de emissão da guia deve ser informada.")
	@Column
	private Date dataEmissaoGuia;

	@NotNull(message = "O atendimento a RN deve ser informado.")
	@Enumerated(EnumType.STRING)
	private SimNao atendimentoRN;

	@NotNull(message = "A indicação de acidente deve ser informada.")
	@Enumerated(EnumType.STRING)
	private IndicacaoAcidente indicacaoAcidente;

	@NotNull(message = "A data o atendimento deve ser informada.")
	@Column(nullable = false)
	private Date dataAtendimento;

	@NotNull(message = "O tipo de consulta deve ser informado.")
	@Enumerated(EnumType.STRING)
	private TipoConsulta tipoConsulta;

	@NotNull(message = "A tabela deve ser informada.")
	@Enumerated(EnumType.STRING)
	private TabelaConsulta tabelaConsulta;

	@NotEmpty(message = "O código do procedimento deve ser informado.")
	@Column(length = 10)
	private String codigoProcedimento;

	@NotNull(message = "O valor do procedimento deve ser informado.")
	@Column
	private BigDecimal valorProcedimento;

	@Column(length = 240)
	private String observacoes;

	@ManyToOne
	private Lote lote;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlanoSaude getPlanoSaude() {
		return planoSaude;
	}

	public void setPlanoSaude(PlanoSaude planoSaude) {
		this.planoSaude = planoSaude;
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}

	public Date getValidadeCarteira() {
		return validadeCarteira;
	}

	public void setValidadeCarteira(Date validadeCarteira) {
		this.validadeCarteira = validadeCarteira;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public SimNao getAtendimentoRN() {
		return atendimentoRN;
	}

	public void setAtendimentoRN(SimNao atendimentoRN) {
		this.atendimentoRN = atendimentoRN;
	}

	public IndicacaoAcidente getIndicacaoAcidente() {
		return indicacaoAcidente;
	}

	public void setIndicacaoAcidente(IndicacaoAcidente indicacaoAcidente) {
		this.indicacaoAcidente = indicacaoAcidente;
	}

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public TipoConsulta getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(TipoConsulta tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public TabelaConsulta getTabelaConsulta() {
		return tabelaConsulta;
	}

	public void setTabelaConsulta(TabelaConsulta tabelaConsulta) {
		this.tabelaConsulta = tabelaConsulta;
	}

	public String getCodigoProcedimento() {
		return codigoProcedimento;
	}

	public void setCodigoProcedimento(String codigoProcedimento) {
		this.codigoProcedimento = codigoProcedimento;
	}

	public BigDecimal getValorProcedimento() {
		return valorProcedimento;
	}

	public void setValorProcedimento(BigDecimal valorProcedimento) {
		this.valorProcedimento = valorProcedimento;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Date getDataEmissaoGuia() {
		return dataEmissaoGuia;
	}

	public void setDataEmissaoGuia(Date dataEmissaoGuia) {
		this.dataEmissaoGuia = dataEmissaoGuia;
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
		GuiaConsulta other = (GuiaConsulta) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

	public String getDataAtendimentoBR() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(dataAtendimento);
	}

	public String getDataEmissaoGuiaBR() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(dataEmissaoGuia);
	}

	public String getValidadeCarteiraBR() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(validadeCarteira);
	}

	public String getDataAtendimentoYMD() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(getDataAtendimento());
	}

	@AssertTrue(message = "planoSaude=O cliente deve possuir um plano de saúde.")
	public boolean isPlanoPreenchido() {
		if (planoSaude == null || planoSaude.getNome().trim().equalsIgnoreCase("Particular")) {
			return false;
		}
		return true;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

}
