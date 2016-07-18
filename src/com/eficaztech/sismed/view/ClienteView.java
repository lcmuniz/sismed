package com.eficaztech.sismed.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.enums.Estado;
import com.eficaztech.sismed.model.Cliente;
import com.eficaztech.sismed.model.ClienteDao;
import com.eficaztech.sismed.model.PlanoSaude;
import com.eficaztech.sismed.model.PlanoSaudeDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class ClienteView extends View {

	private List<Cliente> clientes;
	private List<PlanoSaude> planos;

	private Cliente clienteSelecionado;

	@Wire
	Window clienteWindow;

	@Wire
	Window editWindow;

	@Wire("#include #editConsultaWindow")
	Window editConsultaWindow;

	@Wire
	Listbox clienteListbox;

	@Wire
	Textbox filtroTextbox;

	@Wire("#editWindow #excluirButton")
	Button excluirButton;

	@Wire("#editWindow #nomeTextbox")
	Textbox nomeTextbox;

	@Override
	public void afterCompose() {

		iniciarComboboxes();

		limpar();
	}

	private void iniciarComboboxes() {

		PlanoSaudeDao dao2 = new PlanoSaudeDao();
		planos = dao2.all();
		BindUtils.postNotifyChange(null, null, ClienteView.this, "planos");

	}

	@Command
	@NotifyChange("clientes")
	public void pesquisar() {
		clienteSelecionado = null;
		filtroTextbox.setFocus(true);
		ClienteDao dao = new ClienteDao();
		clientes = dao.find(filtroTextbox.getValue());
	}

	@Command
	@NotifyChange("clientes")
	public void limpar() {
		filtroTextbox.setValue("");
		filtroTextbox.setFocus(true);
		ClienteDao dao = new ClienteDao();
		clientes = dao.all();
	}

	@Command
	@NotifyChange("clienteSelecionado")
	public void novo() {
		clienteSelecionado = new Cliente();
		editWindow.setTitle("Novo Cliente");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		nomeTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("clienteSelecionado")
	public void editar(@BindingParam("id") Long id) {
		ClienteDao dao = new ClienteDao();
		clienteSelecionado = dao.find(id);
		editWindow.setTitle("Editar Cliente");
		editWindow.doModal();
		excluirButton.setDisabled(false);
		nomeTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("clientes")
	public void salvar() {
		ClienteDao dao = new ClienteDao();

		try {
			dao.save(clienteSelecionado);
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		clienteSelecionado = null;
		pesquisar();
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir este CLIENTE?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					ClienteDao dao = new ClienteDao();
					dao.delete(clienteSelecionado);
					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, ClienteView.this, "clientes");
					pesquisar();
				}
			}
		});

	}

	@Command
	@NotifyChange("clientes")
	public void cancelar() {
		editWindow.setVisible(false);
		pesquisar();
	}

	@Command
	public void novaConsulta(@BindingParam("id") Long id) {

		Map<String, Long> args = new HashMap<String, Long>();
		args.put("consulta_id", new Long(0));
		args.put("cliente_id", id);
		Window win = (Window) Executions.getCurrent().createComponents("/consulta/edit_consulta.zul", clienteWindow, args);
		win.doModal();

	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

	public List<PlanoSaude> getPlanos() {
		return planos;
	}

	public void setPlanos(List<PlanoSaude> planos) {
		this.planos = planos;
	}

}
