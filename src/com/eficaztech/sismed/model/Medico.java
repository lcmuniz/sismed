package com.eficaztech.sismed.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.eficaztech.sismed.enums.ConselhoProfissional;
import com.eficaztech.sismed.enums.Estado;

@Entity
public class Medico {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "O nome deve ser informado.")
	@Column(length = 50, nullable = false)
	private String nome;

	@NotEmpty(message = "O CPF deve ser informado.")
	@Column(length = 11, nullable = false)
	private String cpf;

	@NotNull(message = "O conselho profissional deve ser informado.")
	@Enumerated(EnumType.STRING)
	private ConselhoProfissional conselhoProfissional;

	@NotNull(message = "O número do conselho deve ser informado.")
	@Column(nullable = false)
	private Integer numeroConselho;

	@NotNull(message = "O estado deve ser informado.")
	@Enumerated(EnumType.STRING)
	private Estado estadoConselho;

	@NotEmpty(message = "A especialidade deve ser informada.")
	@Column(length = 30, nullable = false)
	private String especialidade;

	@NotNull(message = "O número CNES deve ser informado.")
	@Column(nullable = false)
	private Integer numeroCNES;

	@NotEmpty(message = "O CBOS deve ser informado.")
	@Column(length = 10, nullable = false)
	private String cbos;

	@NotEmpty(message = "O nome da receita deve ser informado.")
	@Column(length = 20, nullable = false)
	private String receita;

	@NotNull(message = "O usuário deve ser informado.")
	@ManyToOne
	private Usuario usuario;

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public ConselhoProfissional getConselhoProfissional() {
		return conselhoProfissional;
	}

	public void setConselhoProfissional(ConselhoProfissional conselhoProfissional) {
		this.conselhoProfissional = conselhoProfissional;
	}

	public Integer getNumeroConselho() {
		return numeroConselho;
	}

	public void setNumeroConselho(Integer numeroConselho) {
		this.numeroConselho = numeroConselho;
	}

	public Estado getEstadoConselho() {
		return estadoConselho;
	}

	public void setEstadoConselho(Estado estadoConselho) {
		this.estadoConselho = estadoConselho;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public Integer getNumeroCNES() {
		return numeroCNES;
	}

	public void setNumeroCNES(Integer numeroCNES) {
		this.numeroCNES = numeroCNES;
	}

	public String getCbos() {
		return cbos;
	}

	public void setCbos(String cbos) {
		this.cbos = cbos;
	}

	public String getReceita() {
		return receita;
	}

	public void setReceita(String receita) {
		this.receita = receita;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		Medico other = (Medico) obj;
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
