package com.eficaztech.sismed.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.model.Consulta;
import com.eficaztech.sismed.model.ConsultaDao;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class ConsultaView extends View {

	private List<Consulta> consultas;

	@Wire
	Window consultaWindow;

	@Wire
	Listbox consultaListbox;

	@Wire
	Textbox filtroClienteTextbox;

	@Wire
	Datebox filtroInicioDatebox;

	@Wire
	Datebox filtroFimDatebox;

	@Override
	public void afterCompose() {
		pesquisarHoje();
	}

	@Command
	public void pesquisar() {
		filtroClienteTextbox.setFocus(true);
		ConsultaDao dao = new ConsultaDao();
		consultas = dao.find(filtroClienteTextbox.getValue(), filtroInicioDatebox.getValue(), filtroFimDatebox.getValue(), true);
		BindUtils.postNotifyChange(null, null, ConsultaView.this, "consultas");
	}

	public void pesquisarHoje() {
		filtroClienteTextbox.setValue("");
		filtroInicioDatebox.setValue(new Date());
		filtroFimDatebox.setValue(new Date());
		filtroClienteTextbox.setFocus(true);
		ConsultaDao dao = new ConsultaDao();
		consultas = dao.find(filtroClienteTextbox.getValue(), filtroInicioDatebox.getValue(), filtroFimDatebox.getValue(), true);
		BindUtils.postNotifyChange(null, null, ConsultaView.this, "consultas");
	}

	@Command
	@NotifyChange("consultas")
	public void limpar() {
		filtroClienteTextbox.setValue("");
		filtroInicioDatebox.setValue(null);
		filtroFimDatebox.setValue(null);
		filtroClienteTextbox.setFocus(true);
		ConsultaDao dao = new ConsultaDao();
		consultas = dao.all();
	}

	@Command
	public void novo() {

		Map<String, Long> args = new HashMap<String, Long>();
		args.put("consulta_id", new Long(0));
		Window win = (Window) Executions.getCurrent().createComponents("/consulta/edit_consulta.zul", consultaWindow, args);
		win.doModal();

	}

	@Command
	public void editar(@BindingParam("id") Long id) {

		Map<String, Long> args = new HashMap<String, Long>();
		args.put("consulta_id", id);
		Window win = (Window) Executions.getCurrent().createComponents("/consulta/edit_consulta.zul", consultaWindow, args);
		win.doModal();

	}

	public List<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

}
