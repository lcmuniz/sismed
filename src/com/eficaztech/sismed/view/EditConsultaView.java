package com.eficaztech.sismed.view;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.enums.SimNao;
import com.eficaztech.sismed.enums.StatusConsulta;
import com.eficaztech.sismed.model.Cliente;
import com.eficaztech.sismed.model.ClienteDao;
import com.eficaztech.sismed.model.Consulta;
import com.eficaztech.sismed.model.ConsultaDao;
import com.eficaztech.sismed.model.Medicamento;
import com.eficaztech.sismed.model.MedicamentoDao;
import com.eficaztech.sismed.model.Medico;
import com.eficaztech.sismed.model.MedicoDao;
import com.eficaztech.sismed.model.PlanoSaude;
import com.eficaztech.sismed.model.PlanoSaudeDao;
import com.eficaztech.sismed.model.Usuario;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.Receita;
import com.eficaztech.sismed.util.Sessao;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;
import com.eficaztech.sismed.util.Warning;

public class EditConsultaView {

	private List<Medico> medicos;
	private List<Cliente> clientes;
	private List<PlanoSaude> planos;
	private List<Medicamento> medicamentos;

	private Consulta consultaSelecionada;
	private Medicamento medicamentoSelecionado;
	private Cliente clienteSelecionado;

	Window editWindow;

	@Wire
	Window detalhesWindow;

	@Wire
	Window medicamentosWindow;

	@Wire
	Window clientesWindow;

	// componentes da janela de edicao

	@Wire
	Button excluirButton;

	@Wire
	Datebox dataDatebox;

	@Wire
	Tab consultaTab;

	@Wire
	Tab condutaTab;

	@Wire
	Tab receita1Tab;

	@Wire
	Tab receita2Tab;

	@Wire
	Tab receita3Tab;

	@Wire
	Tab anterioresTab;

	@Wire
	Combobox medicoCombobox;

	// componentes da janela de detalhes

	@Wire("#detalhesWindow #consultaDetalheLabel")
	Label consultaDetalheLabel;

	@Wire("#detalhesWindow #condutaDetalheLabel")
	Label condutaDetalheLabel;

	@Wire("#detalhesWindow #receita1DetalheLabel")
	Label receita1DetalheLabel;

	@Wire("#detalhesWindow #receita2DetalheLabel")
	Label receita2DetalheLabel;

	@Wire("#detalhesWindow #receita3DetalheLabel")
	Label receita3DetalheLabel;

	// componentes da janela de medicamentos

	@Wire("#medicamentosWindow #filtroMedicamentosTextbox")
	Textbox filtroMedicamentosTextbox;

	// componentes da janela de clientes

	@Wire("#clientesWindow #filtroClientesTextbox")
	Textbox filtroClientesTextbox;

	private Boolean isMed = false; // usuario eh medico?

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("consulta_id") Long consultaId, @ExecutionArgParam("cliente_id") Long clienteId) {

		Selectors.wireComponents(view, this, false);

		editWindow = (Window) view;

		iniciarComboboxes();

		ConsultaDao dao = new ConsultaDao();

		if (consultaId == 0) {

			// nova consulta
			consultaSelecionada = new Consulta();

			// preeche retorno aqui para depois usar preencherRetorno, se a
			// consulta for nova
			consultaSelecionada.setRetorno(SimNao.N);

			if (clienteId != null && clienteId > 0) {
				ClienteDao cdao = new ClienteDao();
				Cliente cliente = cdao.find(clienteId);
				consultaSelecionada.setCliente(cliente);
				consultaSelecionada.setPlanoSaude(cliente.getPlanoSaude());
				String cart = (cliente.getCarteira() == null) ? "" : cliente.getCarteira().trim(); 
				consultaSelecionada.setCarteira(cart);
				consultaSelecionada.setModalidade(cliente.getModalidade());
				consultaSelecionada.setValidade(cliente.getValidade());

				preencherRetorno(dao, consultaSelecionada.getCliente(), consultaSelecionada.getCliente().getPlanoSaude());
			}

			preencherCamposPadroes();

			editWindow.setTitle("Nova Consulta");
			excluirButton.setDisabled(true);
		} else {
			// edicao
			consultaSelecionada = dao.find(consultaId);
			editWindow.setTitle("Editar Consulta");
			excluirButton.setDisabled(false);
			// read only se so tiver um medico
			if (medicos.size() == 1) {
				medicoCombobox.setReadonly(true);
			} else {
				medicoCombobox.setReadonly(false);
			}
		}

		consultaTab.setSelected(true);
		dataDatebox.setFocus(true);

		Usuario usuario = (Usuario) Sessao.get(Sessao.USUARIO);
		isMed = usuario.isMed();

		consultaTab.setVisible(isMed);
		condutaTab.setVisible(isMed);
		receita1Tab.setVisible(isMed);
		receita2Tab.setVisible(isMed);
		receita3Tab.setVisible(isMed);

		if (!isMed) {
			anterioresTab.setSelected(true);
		}

		BindUtils.postNotifyChange(null, null, EditConsultaView.this, "consultaSelecionada");

	}

	private void preencherRetorno(ConsultaDao dao, Cliente cliente, PlanoSaude plano) {
		// preencher retorno: testa prazo de retorno do plano e data da ultima
		// consulta
		Consulta ultimaConsulta = dao.findUltimaConsulta(cliente, plano);
		if (ultimaConsulta == null) {
			consultaSelecionada.setRetorno(SimNao.N);
		} else {
			DateTime dataConsulta = new DateTime(ultimaConsulta.getData());
			DateTime hoje = new DateTime();

			int dias = Days.daysBetween(dataConsulta, hoje).getDays();

			if (dias <= plano.getPrazoRetorno()) {
				consultaSelecionada.setRetorno(SimNao.S);
			} else {
				consultaSelecionada.setRetorno(SimNao.N);
			}
		}
	}

	private void iniciarComboboxes() {

		MedicoDao dao1 = new MedicoDao();
		setMedicos(dao1.all());
		BindUtils.postNotifyChange(null, null, EditConsultaView.this, "medicos");

		PlanoSaudeDao dao3 = new PlanoSaudeDao();
		setPlanos(dao3.all());
		BindUtils.postNotifyChange(null, null, EditConsultaView.this, "planos");

	}

	public void preencherCamposPadroes() {

		// seta data e hora
		DateTime agora = DateTime.now();
		
		String hora = ("0" + agora.getHourOfDay());
		hora = (hora.length() == 2) ? hora : hora.substring(1);
		String minuto = ("0" + agora.getMinuteOfHour());
		minuto = (minuto.length() == 2) ? minuto : minuto.substring(1);
	
		consultaSelecionada.setData(agora.toDate());
		consultaSelecionada.setHora(hora + ":" + minuto);
		// seta status
		StatusConsulta status = StatusConsulta.CON;
		consultaSelecionada.setStatusConsulta(status);
		// set procedimento
		consultaSelecionada.setProcedimento(SimNao.N);
		// seta medico se so tiver um
		if (medicos.size() == 1) {
			consultaSelecionada.setMedico(medicos.get(0));
			medicoCombobox.setReadonly(true);
		} else {
			medicoCombobox.setReadonly(false);
		}

	}

	@Command
	public void verDetalhes(@BindingParam("id") Long id) {
		ConsultaDao dao = new ConsultaDao();
		Consulta c = dao.find(id);
		detalhesWindow.setTitle("Data: " + c.getDataBR() + " - Hora: " + c.getHora());
		consultaDetalheLabel.setValue(c.getConsulta());
		condutaDetalheLabel.setValue(c.getConduta());
		receita1DetalheLabel.setValue(c.getReceita1());
		receita2DetalheLabel.setValue(c.getReceita2());
		receita3DetalheLabel.setValue(c.getReceita3());
		detalhesWindow.doPopup();
	}

	@Command
	@NotifyChange("medicamentos")
	public void verMedicamentos() {
		MedicamentoDao dao = new MedicamentoDao();
		setMedicamentos(dao.find(filtroMedicamentosTextbox.getValue()));
		medicamentosWindow.doPopup();
		filtroMedicamentosTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("medicamentos")
	public void pesquisarMedicamentos() {
		MedicamentoDao dao = new MedicamentoDao();
		setMedicamentos(dao.find(filtroMedicamentosTextbox.getValue()));
		filtroMedicamentosTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("clientes")
	public void verClientes() {
		ClienteDao dao = new ClienteDao();
		if (!filtroClientesTextbox.getValue().isEmpty()) {
			setClientes(dao.find(filtroClientesTextbox.getValue()));
		} else {
			setClientes(null);
		}
		clientesWindow.doPopup();
		filtroClientesTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("clientes")
	public void pesquisarClientes() {
		ClienteDao dao = new ClienteDao();
		setClientes(dao.find(filtroClientesTextbox.getValue()));
		filtroClientesTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("consultaSelecionada")
	public void selecionarCliente() {

		// ao selecionar cliente altera o plano, carteira e validade da consulta
		// para o mesmo do cliente
		getConsultaSelecionada().setCliente(getClienteSelecionado());
		getConsultaSelecionada().setPlanoSaude(getClienteSelecionado().getPlanoSaude());
		getConsultaSelecionada().setModalidade(getClienteSelecionado().getModalidade());
		String cart = (getClienteSelecionado().getCarteira() == null) ? "" : getClienteSelecionado().getCarteira().trim();
		getConsultaSelecionada().setCarteira(cart);
		getConsultaSelecionada().setValidade(getClienteSelecionado().getValidade());

		preencherRetorno(new ConsultaDao(), getClienteSelecionado(), getClienteSelecionado().getPlanoSaude());

		clientesWindow.setVisible(false);
	}

	@Command
	public void imprimirReceita1() {
		imprimirReceita(1);
	}

	@Command
	public void imprimirReceita2() {
		imprimirReceita(2);
	}

	@Command
	public void imprimirReceita3() {
		imprimirReceita(3);
	}

	private void imprimirReceita(int id) {
		String receita = ((Textbox) editWindow.getFellow("receita" + id + "Textbox")).getValue();
		if (receita.isEmpty()) {
			Messagebox.show("A receita está vazia.", "Sismed", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		Receita.visualizar(getConsultaSelecionada(), receita, editWindow);
	}

	@Command
	public void salvar() {
		ConsultaDao dao = new ConsultaDao();

		try {
			dao.save(consultaSelecionada);
			Warning.clear(editWindow.getFellows());
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

	}

	@Command
	public void salvar_fechar() {
		salvar();
		fechar();
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir esta CONSULTA?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					ConsultaDao dao = new ConsultaDao();
					dao.delete(consultaSelecionada);
					Notification.show("success", "Item excluído com sucesso.");
					fechar();
				}
			}
		});

	}

	@Command
	public void cancelar() {
		fechar();
	}

	public Boolean getEhMed() {
		return isMed;
	}

	private void fechar() {
		editWindow.setVisible(false);
		clienteSelecionado = null;
		Binder bind = (Binder) editWindow.getParent().getAttribute("binder");
		bind.postCommand("pesquisar", null);
		editWindow.detach();
	}

	public String concat(String s1, String s2) {
		return s1 + s2;
	}

	public Consulta getConsultaSelecionada() {
		return consultaSelecionada;
	}

	public void setConsultaSelecionada(Consulta consultaSelecionada) {
		this.consultaSelecionada = consultaSelecionada;
	}

	public List<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(List<Medico> medicos) {
		this.medicos = medicos;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<PlanoSaude> getPlanos() {
		return planos;
	}

	public void setPlanos(List<PlanoSaude> planos) {
		this.planos = planos;
	}

	public StatusConsulta[] getStatuss() {
		return StatusConsulta.values();
	}

	public Medicamento getMedicamentoSelecionado() {
		return medicamentoSelecionado;
	}

	public void setMedicamentoSelecionado(Medicamento medicamentoSelecionado) {
		this.medicamentoSelecionado = medicamentoSelecionado;
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public SimNao[] getRetornos() {
		return SimNao.values();
	}

	public SimNao[] getProcedimentos() {
		return SimNao.values();
	}

}
