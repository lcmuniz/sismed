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

import com.eficaztech.sismed.model.Medicamento;
import com.eficaztech.sismed.model.MedicamentoDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class MedicamentoView extends View {

	private List<Medicamento> medicamentos;

	private Medicamento medicamentoSelecionado;

	@Wire
	Window editWindow;

	@Wire
	Listbox medicamentoListbox;

	@Wire
	Textbox filtroTextbox;

	@Wire("#editWindow #excluirButton")
	Button excluirButton;

	@Wire("#editWindow #codigoTextbox")
	Textbox codigoTextbox;

	@Override
	public void afterCompose() {
		limpar();

	}

	@Command
	@NotifyChange("medicamentos")
	public void pesquisar() {
		filtroTextbox.setFocus(true);
		MedicamentoDao dao = new MedicamentoDao();
		medicamentos = dao.find(filtroTextbox.getValue());
	}

	@Command
	@NotifyChange("medicamentos")
	public void limpar() {
		filtroTextbox.setValue("");
		filtroTextbox.setFocus(true);
		MedicamentoDao dao = new MedicamentoDao();
		medicamentos = dao.all();
	}

	@Command
	@NotifyChange("medicamentoSelecionado")
	public void novo() {
		medicamentoSelecionado = new Medicamento();
		editWindow.setTitle("Novo Medicamento");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		codigoTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("medicamentoSelecionado")
	public void editar(@BindingParam("id") Long id) {
		MedicamentoDao dao = new MedicamentoDao();
		medicamentoSelecionado = dao.find(id);
		editWindow.setTitle("Editar Medicamento");
		editWindow.doModal();
		excluirButton.setDisabled(false);
		codigoTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("medicamentos")
	public void salvar() {
		MedicamentoDao dao = new MedicamentoDao();

		try {
			dao.save(medicamentoSelecionado);
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		pesquisar();
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir este MEDICAMENTO?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					MedicamentoDao dao = new MedicamentoDao();
					dao.delete(medicamentoSelecionado);
					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, MedicamentoView.this, "medicamentos");
					pesquisar();
				}
			}
		});

	}

	@Command
	@NotifyChange("medicamentos")
	public void cancelar() {
		editWindow.setVisible(false);
		pesquisar();
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	public Medicamento getMedicamentoSelecionado() {
		return medicamentoSelecionado;
	}

	public void setMedicamentoSelecionado(Medicamento medicamentoSelecionado) {
		this.medicamentoSelecionado = medicamentoSelecionado;
	}

}
