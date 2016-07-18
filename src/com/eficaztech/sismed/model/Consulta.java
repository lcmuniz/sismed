package com.eficaztech.sismed.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.eficaztech.sismed.enums.SimNao;
import com.eficaztech.sismed.enums.StatusConsulta;

@Entity
public class Consulta {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull(message = "O médico deve ser informado.")
	@ManyToOne
	private Medico medico;

	@NotNull(message = "O cliente deve ser informado.")
	@ManyToOne
	private Cliente cliente;

	@NotNull(message = "O plano de saúde deve ser informado.")
	@ManyToOne
	private PlanoSaude planoSaude;

	@Column(length = 30)
	private String modalidade;

	@Column(length = 20)
	private String carteira;

	@Column
	private Date validade;

	@NotNull(message = "A data deve ser informada.")
	@Column(nullable = false)
	private Date data;

	@NotEmpty(message = "A hora deve ser informada.")
	@Column(length = 5, nullable = false)
	@Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message ="A hora deve estar no formato 00:00 (Ex: 07:10).")
	private String hora;

	@NotNull(message = "O retorno deve ser informado.")
	@Enumerated(EnumType.STRING)
	private SimNao retorno;

	@NotNull(message = "O procedimento deve ser informado.")
	@Enumerated(EnumType.STRING)
	private SimNao procedimento;

	@NotNull(message = "O status da consulta deve ser informado.")
	@Enumerated(EnumType.STRING)
	private StatusConsulta statusConsulta;

	@Column
	private String consulta;

	@Column
	private String conduta;

	@Column
	private String receita1;

	@Column
	private String receita2;

	@Column
	private String receita3;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public SimNao getRetorno() {
		return retorno;
	}

	public void setRetorno(SimNao retorno) {
		this.retorno = retorno;
	}

	public SimNao getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(SimNao procedimento) {
		this.procedimento = procedimento;
	}

	public StatusConsulta getStatusConsulta() {
		return statusConsulta;
	}

	public void setStatusConsulta(StatusConsulta statusConsulta) {
		this.statusConsulta = statusConsulta;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public String getConduta() {
		return conduta;
	}

	public void setConduta(String conduta) {
		this.conduta = conduta;
	}

	public String getReceita1() {
		return receita1;
	}

	public void setReceita1(String receita1) {
		this.receita1 = receita1;
	}

	public String getReceita2() {
		return receita2;
	}

	public void setReceita2(String receita2) {
		this.receita2 = receita2;
	}

	public String getReceita3() {
		return receita3;
	}

	public void setReceita3(String receita3) {
		this.receita3 = receita3;
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
		Consulta other = (Consulta) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}

	public String getDataBR() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}

	public String getRetornoIcon() {
		return retorno == SimNao.S ? "z-icon-check" : "";
	}

	public String getProcedimentoIcon() {
		return procedimento == SimNao.S ? "z-icon-check" : "";
	}

	public List<Consulta> getConsultasAnteriores() {
		ConsultaDao dao = new ConsultaDao();
		return dao.findConsultasAnteriores(this);
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
	
	// estilo da linha para o listcell da tela de consulta
	public String getStyleRow() {
		String str = "";
		if (this.getRetorno().equals(SimNao.S))
			str = "background-color:#fee";
		return str;
	}
	

	

}
