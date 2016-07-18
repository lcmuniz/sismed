package com.eficaztech.sismed.view;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.enums.Estado;
import com.eficaztech.sismed.model.Empresa;
import com.eficaztech.sismed.model.EmpresaDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class EmpresaView extends View {

	private Empresa empresaSelecionada;

	@Wire
	Window editWindow;

	@Wire
	Textbox nomeTextbox;

	@Override
	public void afterCompose() {

		EmpresaDao dao = new EmpresaDao();
		empresaSelecionada = dao.find(1L);

		editWindow.doOverlapped();

	}

	@Command
	public void salvar() {
		EmpresaDao dao = new EmpresaDao();

		try {
			dao.save(empresaSelecionada);
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}
	}

	@Command
	@NotifyChange("medicos")
	public void cancelar() {
		Executions.sendRedirect("/cid");
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

}
