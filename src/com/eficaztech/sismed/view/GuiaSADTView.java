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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.enums.CD;
import com.eficaztech.sismed.enums.CaraterSolicitacao;
import com.eficaztech.sismed.enums.IndicacaoAcidente;
import com.eficaztech.sismed.enums.MotivoEncerramentoAtendimento;
import com.eficaztech.sismed.enums.SimNao;
import com.eficaztech.sismed.enums.TabelaConsulta;
import com.eficaztech.sismed.enums.TipoAtendimento;
import com.eficaztech.sismed.enums.TipoConsulta;
import com.eficaztech.sismed.enums.UnidadeMedida;
import com.eficaztech.sismed.model.Cliente;
import com.eficaztech.sismed.model.ClienteDao;
import com.eficaztech.sismed.model.GuiaSADT;
import com.eficaztech.sismed.model.GuiaSADTDao;
import com.eficaztech.sismed.model.Medico;
import com.eficaztech.sismed.model.MedicoDao;
import com.eficaztech.sismed.model.OutraDespesa;
import com.eficaztech.sismed.model.PlanoSaude;
import com.eficaztech.sismed.model.PlanoSaudeDao;
import com.eficaztech.sismed.model.ProcedimentoExecutado;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;
import com.eficaztech.sismed.util.Warning;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class GuiaSADTView extends View {

	private List<GuiaSADT> guias;

	private List<Medico> medicos;
	private List<Cliente> clientes;
	private List<PlanoSaude> planos;

	private GuiaSADT guiaSelecionada;
	private Cliente clienteSelecionado;
	private ProcedimentoExecutado procedimentoSelecionado;
	private ProcedimentoExecutado procedimentoAntesEdicao;
	private OutraDespesa outraDespesaSelecionada;
	private OutraDespesa outraDespesaAntesEdicao;

	@Wire
	Window editWindow;

	@Wire
	Window editProcedimentoWindow;

	@Wire
	Window editOutraDespesaWindow;

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

	@Wire("#editWindow #dadosPrincipaisTab")
	Tab dadosPrincipaisTab;

	@Wire("#clientesWindow #filtroClientesTextbox")
	Textbox filtroClientesTextbox;

	@Wire("#editProcedimentoWindow #excluirProcedimentoButton")
	Button excluirProcedimentoButton;

	@Wire("#editProcedimentoWindow #dataDatebox")
	Datebox dataDatebox;

	@Wire("#editOutraDespesaWindow #excluirOutraDespesaButton")
	Button excluirOutraDespesaButton;

	@Wire("#editOutraDespesaWindow #cdCombobox")
	Combobox cdCombobox;

	@Override
	public void afterCompose() {

		iniciarComboboxes();

		pesquisarHoje();
	}

	private void iniciarComboboxes() {

		MedicoDao dao2 = new MedicoDao();
		setMedicos(dao2.all());
		BindUtils.postNotifyChange(null, null, GuiaSADTView.this, "medicos");

		PlanoSaudeDao dao3 = new PlanoSaudeDao();
		setPlanos(dao3.all());
		BindUtils.postNotifyChange(null, null, GuiaSADTView.this, "planos");

	}

	@Command
	@NotifyChange("guias")
	public void pesquisar() {
		guiaSelecionada = null;
		filtroClienteTextbox.setFocus(true);
		GuiaSADTDao dao = new GuiaSADTDao();
		guias = dao.find(filtroClienteTextbox.getValue(), filtroInicioDatebox.getValue(), filtroFimDatebox.getValue());
	}

	@Command
	public void pesquisarHoje() {
		filtroClienteTextbox.setValue("");
		filtroInicioDatebox.setValue(new Date());
		filtroFimDatebox.setValue(new Date());
		filtroClienteTextbox.setFocus(true);
		GuiaSADTDao dao = new GuiaSADTDao();
		guias = dao.find(filtroClienteTextbox.getValue(), filtroInicioDatebox.getValue(), filtroFimDatebox.getValue());
		BindUtils.postNotifyChange(null, null, GuiaSADTView.this, "guias");
	}

	@Command
	@NotifyChange("guias")
	public void limpar() {
		filtroClienteTextbox.setValue("");
		filtroInicioDatebox.setValue(null);
		filtroFimDatebox.setValue(null);
		filtroClienteTextbox.setFocus(true);
		GuiaSADTDao dao = new GuiaSADTDao();
		guias = dao.all();
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void novo() {
		guiaSelecionada = new GuiaSADT();

		getGuiaSelecionada().setDataEmissaoGuia(new Date());
		getGuiaSelecionada().setAtendimentoRN(SimNao.N);

		// seta medico se so tiver um
		if (medicos.size() == 1) {
			getGuiaSelecionada().setMedico(medicos.get(0));
			medicoCombobox.setReadonly(true);
		} else {
			medicoCombobox.setReadonly(false);
		}

		editWindow.setTitle("Nova Guia SADT");
		dadosPrincipaisTab.setSelected(true);
		editWindow.doModal();
		excluirButton.setDisabled(true);
		numeroGuiaTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void editar(@BindingParam("id") Long id) {
		GuiaSADTDao dao = new GuiaSADTDao();
		setGuiaSelecionada(dao.find(id));

		editWindow.setTitle("Editar Guia SADT");
		dadosPrincipaisTab.setSelected(true);
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
		GuiaSADTDao dao = new GuiaSADTDao();

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

		Messagebox.show("Excluir esta GUIA SADT?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					GuiaSADTDao dao = new GuiaSADTDao();
					dao.delete(guiaSelecionada);
					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, GuiaSADTView.this, "guias");
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
		// para o mesmo do cliente
		getGuiaSelecionada().setCliente(getClienteSelecionado());
		getGuiaSelecionada().setPlanoSaude(getClienteSelecionado().getPlanoSaude());
		getGuiaSelecionada().setCarteira(getClienteSelecionado().getCarteira().trim());
		getGuiaSelecionada().setValidadeCarteira(getClienteSelecionado().getValidade());

		clientesWindow.setVisible(false);
	}

	@Command
	@NotifyChange({ "guiaSelecionada", "procedimentoSelecionado" })
	public void novoProcedimento() {
		procedimentoSelecionado = new ProcedimentoExecutado();
		procedimentoAntesEdicao = new ProcedimentoExecutado();
		getGuiaSelecionada().getProcedimentosExecutados().add(procedimentoSelecionado);

		procedimentoSelecionado.setTimestamp(new Date().getTime());
		procedimentoSelecionado.setData(new Date());
		procedimentoSelecionado.setFatorReducaoAcrescimo(new BigDecimal(1));

		editProcedimentoWindow.setTitle("Novo Procedimento Executado");
		editProcedimentoWindow.doModal();
		excluirProcedimentoButton.setDisabled(true);
		dataDatebox.setFocus(true);
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void salvarProcedimento() {
		try {
			Validations.validate(procedimentoSelecionado);
			editProcedimentoWindow.setVisible(false);
		} catch (ValidationException e) {
			Validations.show(editProcedimentoWindow, e.getErros());
		}
		procedimentoSelecionado = null;
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void cancelarProcedimento() {

		// se a descricao eh nula, significa que
		// este registro esta todo em branco e pode ser excluido
		if (procedimentoSelecionado.getDescricao() == null) {
			guiaSelecionada.getProcedimentosExecutados().remove(procedimentoSelecionado);
		}

		fazCopiaProcedimento(procedimentoAntesEdicao, procedimentoSelecionado);
		editProcedimentoWindow.setVisible(false);

	}

	@Command
	public void excluirProcedimento() {

		Messagebox.show("Excluir este PROCEDIMENTO?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					getGuiaSelecionada().getProcedimentosExecutados().remove(procedimentoSelecionado);
					editProcedimentoWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, GuiaSADTView.this, "guiaSelecionada");
				}
			}
		});

	}

	@Command
	@NotifyChange("procedimentoSelecionado")
	public void editarProcedimento(@BindingParam("id") Long id, @BindingParam("data") ProcedimentoExecutado data) {

		procedimentoAntesEdicao = new ProcedimentoExecutado();
		fazCopiaProcedimento(data, procedimentoAntesEdicao);

		setProcedimentoSelecionado(data);

		editProcedimentoWindow.setTitle("Editar Procedimento Executado");
		editProcedimentoWindow.doModal();
		excluirProcedimentoButton.setDisabled(false);
		dataDatebox.setFocus(true);
	}

	private void fazCopiaProcedimento(ProcedimentoExecutado de, ProcedimentoExecutado para) {

		para.setId(de.getId());
		para.setData(de.getData());
		para.setTabelaConsulta(de.getTabelaConsulta());
		para.setCodigoProcedimento(de.getCodigoProcedimento());
		para.setDescricao(de.getDescricao());
		para.setQuantidade(de.getQuantidade());
		para.setFatorReducaoAcrescimo(de.getFatorReducaoAcrescimo());
		para.setValorUnitario(de.getValorUnitario());
		para.setTimestamp(de.getTimestamp());

	}

	@Command
	@NotifyChange({ "guiaSelecionada", "outraDespesaSelecionada" })
	public void novaOutraDespesa() {
		outraDespesaSelecionada = new OutraDespesa();
		outraDespesaAntesEdicao = new OutraDespesa();
		getGuiaSelecionada().getOutrasDespesas().add(outraDespesaSelecionada);

		outraDespesaSelecionada.setTimestamp(new Date().getTime());
		outraDespesaSelecionada.setData(new Date());
		outraDespesaSelecionada.setFatorReducaoAcrescimo(new BigDecimal(1));

		editOutraDespesaWindow.setTitle("Nova Outra Despesa");
		editOutraDespesaWindow.doModal();
		excluirOutraDespesaButton.setDisabled(true);
		cdCombobox.setFocus(true);
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void salvarOutraDespesa() {
		try {
			Validations.validate(outraDespesaSelecionada);
			editOutraDespesaWindow.setVisible(false);
		} catch (ValidationException e) {
			Validations.show(editOutraDespesaWindow, e.getErros());
		}
		
		outraDespesaSelecionada = null;
	}

	@Command
	@NotifyChange("guiaSelecionada")
	public void cancelarOutraDespesa() {

		// se a descricao eh nula, significa que
		// este registro esta todo em branco e pode ser excluido
		if (outraDespesaSelecionada.getDescricao() == null) {
			guiaSelecionada.getOutrasDespesas().remove(outraDespesaSelecionada);
		}

		fazCopiaOutraDespesa(outraDespesaAntesEdicao, outraDespesaSelecionada);
		editOutraDespesaWindow.setVisible(false);

	}

	@Command
	public void excluirOutraDespesa() {

		Messagebox.show("Excluir esta DESPESA?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					getGuiaSelecionada().getOutrasDespesas().remove(outraDespesaSelecionada);
					editOutraDespesaWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, GuiaSADTView.this, "guiaSelecionada");
				}
			}
		});

	}

	@Command
	@NotifyChange("outraDespesaSelecionada")
	public void editarOutraDespesa(@BindingParam("id") Long id, @BindingParam("data") OutraDespesa data) {

		outraDespesaAntesEdicao = new OutraDespesa();
		fazCopiaOutraDespesa(data, outraDespesaAntesEdicao);

		setOutraDespesaSelecionada(data);
		
		editOutraDespesaWindow.setTitle("Editar Outra Despesa");
		editOutraDespesaWindow.doModal();
		excluirOutraDespesaButton.setDisabled(false);
		cdCombobox.setFocus(true);
	}

	private void fazCopiaOutraDespesa(OutraDespesa de, OutraDespesa para) {

		para.setId(de.getId());
		para.setCd(de.getCd());
		para.setData(de.getData());
		para.setTabelaConsulta(de.getTabelaConsulta());
		para.setCodigoItem(de.getCodigoItem());
		para.setDescricao(de.getDescricao());
		para.setQuantidade(de.getQuantidade());
		para.setUnidadeMedida(de.getUnidadeMedida());
		para.setFatorReducaoAcrescimo(de.getFatorReducaoAcrescimo());
		para.setValorUnitario(de.getValorUnitario());
		para.setTimestamp(de.getTimestamp());

	}

	public List<GuiaSADT> getGuias() {
		return guias;
	}

	public void setGuias(List<GuiaSADT> guias) {
		this.guias = guias;
	}

	public GuiaSADT getGuiaSelecionada() {
		return guiaSelecionada;
	}

	public void setGuiaSelecionada(GuiaSADT guiaSelecionada) {
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

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public CaraterSolicitacao[] getCarateres() {
		return CaraterSolicitacao.values();
	}

	public TipoAtendimento[] getTiposAtendimentos() {
		return TipoAtendimento.values();
	}

	public TipoConsulta[] getTiposConsultas() {
		return TipoConsulta.values();
	}

	public MotivoEncerramentoAtendimento[] getMotivos() {
		return MotivoEncerramentoAtendimento.values();
	}

	public ProcedimentoExecutado getProcedimentoSelecionado() {
		return procedimentoSelecionado;
	}

	public void setProcedimentoSelecionado(ProcedimentoExecutado procedimentoSelecionado) {
		this.procedimentoSelecionado = procedimentoSelecionado;
	}

	public TabelaConsulta[] getTabelas() {
		return TabelaConsulta.values();
	}

	public CD[] getCds() {
		return CD.values();
	}

	public UnidadeMedida[] getUnidades() {
		return UnidadeMedida.values();
	}

	public OutraDespesa getOutraDespesaSelecionada() {
		return outraDespesaSelecionada;
	}

	public void setOutraDespesaSelecionada(OutraDespesa outraDespesaSelecionada) {
		this.outraDespesaSelecionada = outraDespesaSelecionada;
	}

	public IndicacaoAcidente[] getIndicacoes() {
		return IndicacaoAcidente.values();
	}
	
	public SimNao[] getAtendimentoRNs() {
		return SimNao.values();
	}

}
