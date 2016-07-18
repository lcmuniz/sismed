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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.eficaztech.sismed.enums.CD;
import com.eficaztech.sismed.enums.TabelaConsulta;
import com.eficaztech.sismed.enums.UnidadeMedida;

@Entity
public class OutraDespesa {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private GuiaSADT guiaSADT;

	@NotNull(message = "A data deve ser informada.")
	@Column(nullable = false)
	private Date data;

	@NotNull(message = "O CD deve ser informado.")
	@Enumerated(EnumType.STRING)
	private CD cd;

	@NotNull(message = "O unidade de medida deve ser informada.")
	@Enumerated(EnumType.STRING)
	private UnidadeMedida unidadeMedida;

	@NotNull(message = "A tabela deve ser informada.")
	@Enumerated(EnumType.STRING)
	private TabelaConsulta tabelaConsulta;

	@NotEmpty(message = "O código do item deve ser informado.")
	@Column(length = 10, nullable = false)
	private String codigoItem;

	@NotEmpty(message = "A descrição deve ser informada.")
	@Column(length = 60, nullable = false)
	private String descricao;

	@NotNull(message = "A quantidade deve ser informada.")
	@Column(nullable = false)
	private Integer quantidade;

	@NotNull(message = "O fator de redução ou acréscimo deve ser informado.")
	@Column
	private BigDecimal fatorReducaoAcrescimo;

	@NotNull(message = "O valor unitário deve ser informado.")
	@Column(nullable = false)
	private BigDecimal valorUnitario;

	@Transient
	private Long timestamp = new Long(0);
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GuiaSADT getGuiaSADT() {
		return guiaSADT;
	}

	public void setGuiaSADT(GuiaSADT guiaSADT) {
		this.guiaSADT = guiaSADT;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TabelaConsulta getTabelaConsulta() {
		return tabelaConsulta;
	}

	public void setTabelaConsulta(TabelaConsulta tabelaConsulta) {
		this.tabelaConsulta = tabelaConsulta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getFatorReducaoAcrescimo() {
		return fatorReducaoAcrescimo;
	}

	public void setFatorReducaoAcrescimo(BigDecimal fatorReducaoAcrescimo) {
		this.fatorReducaoAcrescimo = fatorReducaoAcrescimo;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public CD getCd() {
		return cd;
	}

	public void setCd(CD cd) {
		this.cd = cd;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(String codigoItem) {
		this.codigoItem = codigoItem;
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
		OutraDespesa other = (OutraDespesa) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		if (getId()==null && other.getId()==null) {
			if (!getTimestamp().equals(other.getTimestamp())) return false;
		}
		return true;
	}

	public String getDataBR() {
		if (data == null) return "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}

	public String getDataYMD() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(getData());
	}

	public BigDecimal getValorTotal() {
		if (quantidade == null || valorUnitario == null) return BigDecimal.ZERO;
		return valorUnitario.multiply(new BigDecimal(quantidade));
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
