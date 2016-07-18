package com.eficaztech.sismed.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.eficaztech.sismed.enums.Estado;

@Entity
public class Empresa {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "O nome deve ser informado.")
	@Column(length = 50, nullable = false)
	private String nome;

	@NotEmpty(message = "O endereço deve ser informado.")
	@Column(length = 100, nullable = false)
	private String endereco;

	@NotEmpty(message = "O município deve ser informado.")
	@Column(length = 20, nullable = false)
	private String municipio;

	@NotNull(message = "O estado deve ser informado.")
	@Enumerated(EnumType.STRING)
	private Estado estado;

	@NotEmpty(message = "O CEP deve ser informado.")
	@Column(length = 8, nullable = false)
	private String cep;

	@NotNull(message = "O código do IBGE deve ser informado.")
	@Column(nullable = false)
	private Integer codigoIBGE;

	@NotEmpty(message = "Os telefones devem ser informados.")
	@Column(length = 50, nullable = false)
	private String telefones;

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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Integer getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(Integer codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	public String getTelefones() {
		return telefones;
	}

	public void setTelefones(String telefones) {
		this.telefones = telefones;
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
		Empresa other = (Empresa) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

}
