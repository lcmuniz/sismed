package com.eficaztech.sismed.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.eficaztech.sismed.enums.CD;
import com.eficaztech.sismed.enums.CaraterSolicitacao;
import com.eficaztech.sismed.enums.IndicacaoAcidente;
import com.eficaztech.sismed.enums.MotivoEncerramentoAtendimento;
import com.eficaztech.sismed.enums.SimNao;
import com.eficaztech.sismed.enums.TipoAtendimento;
import com.eficaztech.sismed.enums.TipoConsulta;

@Entity
public class GuiaSADT {

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

	@Column(length = 20)
	private String numeroGuiaPrincipal;

	@NotNull(message = "A data de emissão da guia deve ser informada.")
	@Column
	private Date dataEmissaoGuia;

	@Column
	private Date dataAutorizacao;

	@Column
	private Date dataValidadeSenha;

	@Column(length = 20)
	private String senha;

	@NotNull(message = "O atendimento a RN deve ser informado.")
	@Enumerated(EnumType.STRING)
	private SimNao atendimentoRN;

	@NotNull(message = "A indicação de acidente deve ser informada.")
	@Enumerated(EnumType.STRING)
	private IndicacaoAcidente indicacaoAcidente;

	@NotNull(message = "A data da solicitação deve ser informada.")
	@Column(nullable = false)
	private Date dataSolicitacao;

	@NotNull(message = "O caráter da solicitação deve ser informado.")
	@Enumerated(EnumType.STRING)
	private CaraterSolicitacao caraterSolicitacao;

	@NotNull(message = "O tipo de atendimento deve ser informado.")
	@Enumerated(EnumType.STRING)
	private TipoAtendimento tipoAtendimento;

	@NotNull(message = "O tipo de consulta deve ser informado.")
	@Enumerated(EnumType.STRING)
	private TipoConsulta tipoConsulta;

	@Enumerated(EnumType.STRING)
	private MotivoEncerramentoAtendimento motivoEncerramentoAtendimento;

	@Column(length = 240)
	private String observacoes;

	@Column(length = 100)
	private String indicacaoClinica;

	@OneToMany(mappedBy = "guiaSADT", cascade = CascadeType.ALL)
	private List<ProcedimentoExecutado> procedimentosExecutados;

	@OneToMany(mappedBy = "guiaSADT", cascade = CascadeType.ALL)
	private List<OutraDespesa> outrasDespesas;

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

	public String getNumeroGuiaPrincipal() {
		return numeroGuiaPrincipal;
	}

	public void setNumeroGuiaPrincipal(String numeroGuiaPrincipal) {
		this.numeroGuiaPrincipal = numeroGuiaPrincipal;
	}

	public Date getDataEmissaoGuia() {
		return dataEmissaoGuia;
	}

	public void setDataEmissaoGuia(Date dataEmissaoGuia) {
		this.dataEmissaoGuia = dataEmissaoGuia;
	}

	public Date getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public Date getDataValidadeSenha() {
		return dataValidadeSenha;
	}

	public void setDataValidadeSenha(Date dataValidadeSenha) {
		this.dataValidadeSenha = dataValidadeSenha;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public CaraterSolicitacao getCaraterSolicitacao() {
		return caraterSolicitacao;
	}

	public void setCaraterSolicitacao(CaraterSolicitacao caraterSolicitacao) {
		this.caraterSolicitacao = caraterSolicitacao;
	}

	public TipoAtendimento getTipoAtendimento() {
		return tipoAtendimento;
	}

	public void setTipoAtendimento(TipoAtendimento tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}

	public TipoConsulta getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(TipoConsulta tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public MotivoEncerramentoAtendimento getMotivoEncerramentoAtendimento() {
		return motivoEncerramentoAtendimento;
	}

	public void setMotivoEncerramentoAtendimento(MotivoEncerramentoAtendimento motivoEncerramentoAtendimento) {
		this.motivoEncerramentoAtendimento = motivoEncerramentoAtendimento;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getIndicacaoClinica() {
		return indicacaoClinica;
	}

	public void setIndicacaoClinica(String indicacaoClinica) {
		this.indicacaoClinica = indicacaoClinica;
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
		GuiaSADT other = (GuiaSADT) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

	@AssertTrue(message = "planoSaude=O cliente deve possuir um plano de saúde.")
	public boolean isPlanoPreenchido() {
		if (planoSaude == null || planoSaude.getNome().equals("Particular")) {
			return false;
		}
		return true;
	}

	@AssertTrue(message = "Deve existir pelo menos um procedimento executado.")
	public boolean isPeloMenosUmProcedimentoPreenchido() {
		if (procedimentosExecutados != null && procedimentosExecutados.size() > 0) {
			return true;
		}
		return false;
	}

	public String getDataEmissaoGuiaBR() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(dataEmissaoGuia);
	}

	public String getDataSolicitacaoYMD() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(getDataSolicitacao());
	}

	public List<ProcedimentoExecutado> getProcedimentosExecutados() {
		return procedimentosExecutados;
	}

	public void setProcedimentosExecutados(List<ProcedimentoExecutado> procedimentosExecutados) {
		this.procedimentosExecutados = procedimentosExecutados;
	}

	public List<OutraDespesa> getOutrasDespesas() {
		return outrasDespesas;
	}

	public void setOutrasDespesas(List<OutraDespesa> outrasDespesas) {
		this.outrasDespesas = outrasDespesas;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

	public BigDecimal getValorTotalGasesMedicinais() {
		BigDecimal total = BigDecimal.ZERO;
		for (OutraDespesa outraDespesa : outrasDespesas) {
			if (outraDespesa.getCd().equals(CD.C_01)) {
				total = total.add(outraDespesa.getValorTotal());
			}
		}
		return total;
	}

	public BigDecimal getValorTotalMedicamentos() {
		BigDecimal total = BigDecimal.ZERO;
		for (OutraDespesa outraDespesa : outrasDespesas) {
			if (outraDespesa.getCd().equals(CD.C_02)) {
				total = total.add(outraDespesa.getValorTotal());
			}
		}
		return total;
	}

	public BigDecimal getValorTotalMateriais() {
		BigDecimal total = BigDecimal.ZERO;
		for (OutraDespesa outraDespesa : outrasDespesas) {
			if (outraDespesa.getCd().equals(CD.C_03)) {
				total = total.add(outraDespesa.getValorTotal());
			}
		}
		return total;
	}

	public BigDecimal getValorTotalTaxasAlugueis() {
		BigDecimal total = BigDecimal.ZERO;
		for (OutraDespesa outraDespesa : outrasDespesas) {
			if (outraDespesa.getCd().equals(CD.C_07)) {
				total = total.add(outraDespesa.getValorTotal());
			}
		}
		return total;
	}

	public BigDecimal getValorTotalOPME() {
		BigDecimal total = BigDecimal.ZERO;
		for (OutraDespesa outraDespesa : outrasDespesas) {
			if (outraDespesa.getCd().equals(CD.C_08)) {
				total = total.add(outraDespesa.getValorTotal());
			}
		}
		return total;
	}

	public BigDecimal getValorTotalProcedimentosExecutados() {
		BigDecimal total = BigDecimal.ZERO;
		for (ProcedimentoExecutado proc : procedimentosExecutados) {
			total = total.add(proc.getValorTotal());
		}
		return total;
	}

	public BigDecimal getValorTotalGeral() {
		BigDecimal total = BigDecimal.ZERO;
		total = total.add(getValorTotalGasesMedicinais());
		total = total.add(getValorTotalMateriais());
		total = total.add(getValorTotalMedicamentos());
		total = total.add(getValorTotalMedicamentos());
		total = total.add(getValorTotalOPME());
		total = total.add(getValorTotalProcedimentosExecutados());
		total = total.add(getValorTotalTaxasAlugueis());
		return total;
	}

}
