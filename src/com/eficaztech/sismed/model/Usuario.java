package com.eficaztech.sismed.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.eficaztech.sismed.enums.Funcao;

@Entity
public class Usuario {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "O usuário deve ser informado.")
	@Column(length = 10, nullable = false)
	private String usuario;

	@NotEmpty(message = "O nome deve ser informado.")
	@Column(length = 50, nullable = false)
	private String nome;

	@NotEmpty(message = "O email deve ser informado.")
	@Email(message = "Não é um email válido.")
	@Column(length = 30, nullable = false)
	private String email;

	@NotNull(message = "A função deve ser informada.")
	@Enumerated(EnumType.STRING)
	private Funcao funcao;

	@NotEmpty(message = "A senha deve ser informada.")
	@Column(length = 32, nullable = false)
	private String senha;

	@Transient
	private String confirmacaoSenha;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
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
		Usuario other = (Usuario) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

	@AssertTrue(message = "confirmacaoSenha=A senha e sua confirmação devem ser iguais.")
	public boolean isSenhaConfirmada() {
		if (confirmacaoSenha == null || confirmacaoSenha.isEmpty()) {
			return true;
		} else {
			if (senha.equals(confirmacaoSenha)) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean isAdm() {
		return funcao.equals(Funcao.ADM);
	}
	
	public Boolean isSec() {
		return funcao.equals(Funcao.SEC);
	}
	
	public Boolean isMed() {
		return funcao.equals(Funcao. MED);
	}

}
