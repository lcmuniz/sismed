package com.eficaztech.sismed.view;

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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.model.PlanoSaude;
import com.eficaztech.sismed.model.PlanoSaudeDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class PlanoSaudeView extends View {

	private List<PlanoSaude> planos;

	private PlanoSaude planoSelecionado;

	@Wire
	Window editWindow;

	@Wire
	Listbox planoListbox;

	@Wire
	Textbox filtroTextbox;

	@Wire("#editWindow #excluirButton")
	Button excluirButton;

	@Wire("#editWindow #nomeTextbox")
	Textbox nomeTextbox;

	@Override
	public void afterCompose() {
		limpar();
	}

	@Command
	@NotifyChange("planos")
	public void pesquisar() {
		filtroTextbox.setFocus(true);
		PlanoSaudeDao dao = new PlanoSaudeDao();
		planos = dao.find(filtroTextbox.getValue());
	}

	@Command
	@NotifyChange("planos")
	public void limpar() {
		filtroTextbox.setValue("");
		filtroTextbox.setFocus(true);
		PlanoSaudeDao dao = new PlanoSaudeDao();
		planos = dao.all();
	}

	@Command
	@NotifyChange("planoSelecionado")
	public void novo() {
		planoSelecionado = new PlanoSaude();
		editWindow.setTitle("Novo Plano de Saúde");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		nomeTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("planoSelecionado")
	public void editar(@BindingParam("id") Long id) {
		PlanoSaudeDao dao = new PlanoSaudeDao();
		planoSelecionado = dao.find(id);
		editWindow.setTitle("Editar Plano de Saúde");
		editWindow.doModal();
		excluirButton.setDisabled(false);
		nomeTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("planos")
	public void salvar() {
		PlanoSaudeDao dao = new PlanoSaudeDao();

		try {
			dao.save(planoSelecionado);
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		pesquisar();
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir este PLANO DE SAÚDE?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					PlanoSaudeDao dao = new PlanoSaudeDao();
					dao.delete(planoSelecionado);
					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, PlanoSaudeView.this, "planos");
					pesquisar();
				}
			}
		});

	}

	@Command
	@NotifyChange("planos")
	public void cancelar() {
		editWindow.setVisible(false);
		pesquisar();
	}

	public List<PlanoSaude> getPlanos() {
		return planos;
	}

	public void setPlanos(List<PlanoSaude> planos) {
		this.planos = planos;
	}

	public PlanoSaude getPlanoSelecionado() {
		return planoSelecionado;
	}

	public void setPlanoSelecionado(PlanoSaude planoSelecionado) {
		this.planoSelecionado = planoSelecionado;
	}

}
