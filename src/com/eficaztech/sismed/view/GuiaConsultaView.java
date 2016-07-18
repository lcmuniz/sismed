package com.eficaztech.sismed.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.enums.IndicacaoAcidente;
import com.eficaztech.sismed.enums.SimNao;
import com.eficaztech.sismed.enums.TabelaConsulta;
import com.eficaztech.sismed.enums.TipoConsulta;
import com.eficaztech.sismed.model.Cliente;
import com.eficaztech.sismed.model.ClienteDao;
import com.eficaztech.sismed.model.GuiaConsulta;
import com.eficaztech.sismed.model.GuiaConsultaDao;
import com.eficaztech.sismed.model.Medico;
import com.eficaztech.sismed.model.MedicoDao;
import com.eficaztech.sismed.model.PlanoSaude;
import com.eficaztech.sismed.model.PlanoSaudeDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;
import com.eficaztech.sismed.util.Warning;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class GuiaConsultaView extends View {

	private List<GuiaConsulta> guias;

	private List<Medico> medicos;
	private List<Cliente> clientes;
	private List<PlanoSaude> planos;

	private GuiaConsulta guiaSelecionada;
	private Cliente clienteSelecionado;

	@Wire
	Window editWindow;

	@Wire
	Window clientesWindow;

	@Wire
	Listbox guiaListbox;

	@Wire
	Textbox filtroClienteTextbox;

	@Wire
	Datebox filtroInicioDatebox;

	@Wire
	Datebox filtroFimDatebox;

	@Wire("#editWindow #excluirButton")
	Button excluirButton;

	@Wire("#editWindow #medicoCombobox")
	Combobox medicoCombobox;

	@Wire("#editWindow #numeroGuiaTextbox")
	Textbox numeroGuiaTextbox;

	@Wire("#clientesWindow #filtroClientesTextbox")
	Textbox filtroClientesTextbox;

	@Override
	public void afterCompose() {

		iniciarComboboxes();

		pesquisarHoje();
	}

	private void iniciarComboboxes() {

		MedicoDao dao2 = new MedicoDao();
		setMedicos(dao2.all());
		BindUtils.postNotifyChange(null, null, GuiaConsultaView.this, "medicos");

		PlanoSaudeDao dao3 = new PlanoSaudeDao();
		setPlanos(dao3.all());
		BindUtils.postNotifyChange(null, null, GuiaConsultaView.this, "planos");

	}

	@Command
	@NotifyChange("guias")
	public void pesquisar() {
		guiaSelecionada = null;
		filtroClienteTextbox.setFocus(true);
		GuiaConsultaDao dao = new GuiaConsultaDao();
		guias = dao.find(filtroClienteTextbox.getValue(), filtroInicioDatebox.getValue(), filtroFimDatebox.getValue());
	}

	@Command
	public void pesquisarHoje() {
		filtroClienteTextbox.setValue("");
		filtroInicioDatebox.setValue(new Date());
		filtroFimDatebox.setValue(new Date());
		filtroClienteTextbox.setFocus(true);
		GuiaConsultaDao dao = new GuiaConsultaDao();
		guias = dao.find(filtroClienteTextbox.getValue(), filtroInicioDatebox.getValue(), filtroFimDatebox.getValue());
		BindUtils.postNotifyChange(null, null, GuiaConsultaView.this, "guias");
	}

	@Command
	@NotifyChange("guias")
	public void limpar() {
		filtroClienteTextbox.setValue("");
		filtroInicioDatebox.setValue(null);
		filtroFimDatebox.setValue(null);
		filtroClienteTextbox.setFocus(true);
		GuiaConsultaDao dao = new GuiaConsultaDao();
		guias = dao.all();
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void novo() {
		guiaSelecionada = new GuiaConsulta();

		getGuiaSelecionada().setDataEmissaoGuia(new Date());
		getGuiaSelecionada().setAtendimentoRN(SimNao.N);

		// seta medico se so tiver um
		if (medicos.size() == 1) {
			getGuiaSelecionada().setMedico(medicos.get(0));
			medicoCombobox.setReadonly(true);
		} else {
			medicoCombobox.setReadonly(false);
		}

		getGuiaSelecionada().setTabelaConsulta(TabelaConsulta.C_22);
		getGuiaSelecionada().setValorProcedimento(new BigDecimal(00.00));
		getGuiaSelecionada().setCodigoProcedimento("10101012");
		getGuiaSelecionada().setIndicacaoAcidente(IndicacaoAcidente.C_9);
		getGuiaSelecionada().setDataAtendimento(new Date());
		getGuiaSelecionada().setTipoConsulta(TipoConsulta.C_1);

		editWindow.setTitle("Nova Guia de Consulta");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		numeroGuiaTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void editar(@BindingParam("id") Long id) {
		GuiaConsultaDao dao = new GuiaConsultaDao();
		guiaSelecionada = dao.find(id);
		editWindow.setTitle("Editar Guia de Consulta");
		editWindow.doModal();
		excluirButton.setDisabled(false);
		numeroGuiaTextbox.setFocus(true);

		if (medicos.size() == 1) {
			medicoCombobox.setReadonly(true);
		} else {
			medicoCombobox.setReadonly(false);
		}

	}

	@Command
	@NotifyChange("guias")
	public void salvar() {
		GuiaConsultaDao dao = new GuiaConsultaDao();

		try {
			dao.save(guiaSelecionada);
			Notification.show("success", "Item salvo com sucesso.");
			Warning.clear(editWindow.getFellows());
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		guiaSelecionada  = null;
		pesquisar();
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir esta GUIA DE CONSULTA?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					GuiaConsultaDao dao = new GuiaConsultaDao();
					dao.delete(guiaSelecionada);
					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, GuiaConsultaView.this, "guias");
					pesquisar();
				}
			}
		});

	}

	@Command
	@NotifyChange("guias")
	public void cancelar() {
		editWindow.setVisible(false);
		pesquisar();
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
	@NotifyChange("guiaSelecionada")
	public void selecionarCliente() {

		// ao selecionar cliente altera o plano, carteira e validade da guia
		// para o mesmo do client e tambem o valor do procedimento
		getGuiaSelecionada().setCliente(getClienteSelecionado());
		getGuiaSelecionada().setPlanoSaude(getClienteSelecionado().getPlanoSaude());
		getGuiaSelecionada().setCarteira(getClienteSelecionado().getCarteira().trim());
		getGuiaSelecionada().setValidadeCarteira(getClienteSelecionado().getValidade());
		getGuiaSelecionada().setValorProcedimento(getClienteSelecionado().getPlanoSaude().getValorConsulta());

		clientesWindow.setVisible(false);
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void selecionarPlano() {

		// ao selecionar plano, alterar o valor do procedimento
		getGuiaSelecionada().setValorProcedimento(getGuiaSelecionada().getPlanoSaude().getValorConsulta());

		clientesWindow.setVisible(false);
	}

	public List<GuiaConsulta> getGuias() {
		return guias;
	}

	public void setGuias(List<GuiaConsulta> guias) {
		this.guias = guias;
	}

	public GuiaConsulta getGuiaSelecionada() {
		return guiaSelecionada;
	}

	public void setGuiaSelecionada(GuiaConsulta guiaSelecionada) {
		this.guiaSelecionada = guiaSelecionada;
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

	public TipoConsulta[] getTipos() {
		return TipoConsulta.values();
	}

	public TabelaConsulta[] getTabelas() {
		return TabelaConsulta.values();
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public SimNao[] getAtendimentoRNs() {
		return SimNao.values();
	}

	public IndicacaoAcidente[] getIndicacoes() {
		return IndicacaoAcidente.values();
	}

}
