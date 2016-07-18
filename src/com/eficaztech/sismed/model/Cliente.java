package com.eficaztech.sismed.model;

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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.eficaztech.sismed.enums.Estado;

@Entity
public class Cliente {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "O nome deve ser informado.")
	@Column(length = 100, nullable = false)
	private String nome;

	@NotNull(message = "A data de nascimento deve ser informada.")
	@Column(nullable = false)
	private Date dataNascimento;

	@Column(length = 100)
	private String filiacao;

	@Column(length = 100)
	private String profissao;

	@Column(length = 100)
	private String endereco;

	@Column(length = 8)
	private String cep;

	@Column(length = 50)
	private String cidade;

	@Enumerated(EnumType.STRING)
	private Estado estado;

	@NotEmpty(message = "Os telefones devem ser informados.")
	@Column(length = 50, nullable = false)
	private String telefones;

	@Column(length = 20)
	private String identidade;

	@NotNull(message = "O plano de saúde deve ser informado (se não tiver, escolher 'Particular'.")
	@ManyToOne
	private PlanoSaude planoSaude;

	@Column(length = 50)
	private String modalidade;

	@Column(length = 30)
	private String carteira;

	@Column
	private Date validade;

	@Column(length = 20)
	private String cartaoNacionalSaude;

	@Column(length = 50)
	private String indicacao;

	@Email(message = "Não é um email válido.")
	@Column(length = 100)
	private String email;

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

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getFiliacao() {
		return filiacao;
	}

	public void setFiliacao(String filiacao) {
		this.filiacao = filiacao;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getTelefones() {
		return telefones;
	}

	public void setTelefones(String telefones) {
		this.telefones = telefones;
	}

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}

	public PlanoSaude getPlanoSaude() {
		return planoSaude;
	}

	public void setPlanoSaude(PlanoSaude planoSaude) {
		this.planoSaude = planoSaude;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}

	public Date getValidade() {
		return validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}

	public String getCartaoNacionalSaude() {
		return cartaoNacionalSaude;
	}

	public void setCartaoNacionalSaude(String cartaoNacionalSaude) {
		this.cartaoNacionalSaude = cartaoNacionalSaude;
	}

	public String getIndicacao() {
		return indicacao;
	}

	public void setIndicacao(String indicacao) {
		this.indicacao = indicacao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		Cliente other = (Cliente) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

	public String getDataNascimentoBR() {
		if (dataNascimento == null) return "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(dataNascimento);
	}

	public String getValidadeBR() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(validade);
	}

	public String getIdade() {
		DateTime inicio = new DateTime(dataNascimento);
		DateTime fim = new DateTime();
		Period p = new Period(inicio, fim);
		PeriodFormatter pf = new PeriodFormatterBuilder().appendYears().printZeroIfSupported().toFormatter();
		return pf.print(p);
	}

	@AssertTrue(message = "planoSaude=Se o plano de saúde não for 'Particular', a modalidade, a carteira e a validade devem ser preenchidos.")
	public boolean isPlanoTodoPreenchido() {
		if (planoSaude == null || planoSaude.getNome().trim().equalsIgnoreCase("Particular")) {
			return true;
		} else {
			boolean modalidadePreenchida = modalidade != null && !modalidade.isEmpty();
			boolean carteiraPreenchida = carteira != null && !carteira.isEmpty();
			boolean validadePreenchida = validade != null;
			if (modalidadePreenchida && carteiraPreenchida && validadePreenchida) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return nome;
	}

}