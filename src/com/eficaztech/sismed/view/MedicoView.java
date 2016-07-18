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

import com.eficaztech.sismed.enums.ConselhoProfissional;
import com.eficaztech.sismed.enums.Estado;
import com.eficaztech.sismed.model.Medico;
import com.eficaztech.sismed.model.MedicoDao;
import com.eficaztech.sismed.model.Usuario;
import com.eficaztech.sismed.model.UsuarioDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class MedicoView extends View {

	private List<Medico> medicos;
	private List<Usuario> usuarios;

	private Medico medicoSelecionado;

	@Wire
	Window editWindow;

	@Wire
	Listbox medicoListbox;

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
		UsuarioDao dao1 = new UsuarioDao();
		usuarios = dao1.all();
		BindUtils.postNotifyChange(null, null, MedicoView.this, "usuarios");
	}

	@Command
	@NotifyChange("medicos")
	public void pesquisar() {
		filtroTextbox.setFocus(true);
		MedicoDao dao = new MedicoDao();
		medicos = dao.find(filtroTextbox.getValue());
	}

	@Command
	@NotifyChange("medicos")
	public void limpar() {
		filtroTextbox.setValue("");
		filtroTextbox.setFocus(true);
		MedicoDao dao = new MedicoDao();
		medicos = dao.all();
	}

	@Command
	@NotifyChange("medicoSelecionado")
	public void novo() {
		medicoSelecionado = new Medico();
		editWindow.setTitle("Novo Médico");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		nomeTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("medicoSelecionado")
	public void editar(@BindingParam("id") Long id) {
		MedicoDao dao = new MedicoDao();
		medicoSelecionado = dao.find(id);
		editWindow.setTitle("Editar Médico");
		editWindow.doModal();
		excluirButton.setDisabled(false);
		nomeTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("medicos")
	public void salvar() {
		MedicoDao dao = new MedicoDao();

		try {
			dao.save(medicoSelecionado);
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		pesquisar();
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir este MÉDICO?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					MedicoDao dao = new MedicoDao();
					dao.delete(medicoSelecionado);
					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, MedicoView.this, "medicos");
					pesquisar();
				}
			}
		});

	}

	@Command
	@NotifyChange("medicos")
	public void cancelar() {
		editWindow.setVisible(false);
		pesquisar();
	}

	public List<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(List<Medico> medicos) {
		this.medicos = medicos;
	}

	public Medico getMedicoSelecionado() {
		return medicoSelecionado;
	}

	public void setMedicoSelecionado(Medico medicoSelecionado) {
		this.medicoSelecionado = medicoSelecionado;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public ConselhoProfissional[] getConselhos() {
		return ConselhoProfissional.values();
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

}
