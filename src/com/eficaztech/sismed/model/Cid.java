package com.eficaztech.sismed.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Cid {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "O Cid deve ser informado.")
	@Column(length = 5, nullable = false)
	private String cid;

	@NotEmpty(message = "A descrição deve ser informada.")
	@Column(length = 50, nullable = false)
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		Cid other = (Cid) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

}
