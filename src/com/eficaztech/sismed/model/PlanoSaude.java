package com.eficaztech.sismed.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class PlanoSaude {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "O nome deve ser informado.")
	@Column(length = 50, nullable = false)
	private String nome;

	@NotNull(message = "O prazo de retorno deve ser informado.")
	@Column(nullable = false)
	private Integer prazoRetorno;

	@NotNull(message = "O registro ANS deve ser informado.")
	@Column(nullable = false)
	private String registroANS;

	@NotNull(message = "O valor da consulta deve ser informado.")
	@Column
	private BigDecimal valorConsulta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getPrazoRetorno() {
		return prazoRetorno;
	}

	public void setPrazoRetorno(Integer prazoRetorno) {
		this.prazoRetorno = prazoRetorno;
	}

	public String getRegistroANS() {
		return registroANS;
	}

	public void setRegistroANS(String registroANS) {
		this.registroANS = registroANS;
	}

	public BigDecimal getValorConsulta() {
		return valorConsulta;
	}

	public void setValorConsulta(BigDecimal valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

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
		PlanoSaude other = (PlanoSaude) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

}
