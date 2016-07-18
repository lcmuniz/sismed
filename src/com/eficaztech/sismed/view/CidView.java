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

import com.eficaztech.sismed.model.Cid;
import com.eficaztech.sismed.model.CidDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class CidView extends View {

	private List<Cid> cids;

	private Cid cidSelecionado;

	@Wire
	Window editWindow;

	@Wire
	Listbox cidListbox;

	@Wire
	Textbox filtroTextbox;

	@Wire("#editWindow #excluirButton")
	Button excluirButton;

	@Wire("#editWindow #cidTextbox")
	Textbox cidTextbox;

	@Override
	public void afterCompose() {
		limpar();
	}

	@Command
	@NotifyChange("cids")
	public void pesquisar() {
		filtroTextbox.setFocus(true);
		CidDao dao = new CidDao();
		cids = dao.find(filtroTextbox.getValue());
	}

	@Command
	@NotifyChange("cids")
	public void limpar() {
		filtroTextbox.setValue("");
		filtroTextbox.setFocus(true);
		CidDao dao = new CidDao();
		cids = dao.all();
	}

	@Command
	@NotifyChange("cidSelecionado")
	public void novo() {
		cidSelecionado = new Cid();
		editWindow.setTitle("Novo Cid");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		cidTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("cidSelecionado")
	public void editar(@BindingParam("id") Long id) {
		CidDao dao = new CidDao();
		cidSelecionado = dao.find(id);
		editWindow.setTitle("Editar Cid");
		editWindow.doModal();
		excluirButton.setDisabled(false);
		cidTextbox.setFocus(true);
	}

	@Command
	@NotifyChange("cids")
	public void salvar() {
		CidDao dao = new CidDao();

		try {
			dao.save(cidSelecionado);
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		pesquisar();
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir este CID?", "Sismed", Messagebox.YES
				| Messagebox.NO, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					@Override
					public void onEvent(Event e) {
						if (Messagebox.ON_YES.equals(e.getName())) {
							CidDao dao = new CidDao();
							dao.delete(cidSelecionado);
							Notification.show("success",
									"Item excluído com sucesso.");
							editWindow.setVisible(false);
							BindUtils.postNotifyChange(null, null,
									CidView.this, "cids");
							pesquisar();
						}
					}
				});

	}

	@Command
	@NotifyChange("cids")
	public void cancelar() {
		editWindow.setVisible(false);
		pesquisar();
	}

	public List<Cid> getCids() {
		return cids;
	}

	public void setCids(List<Cid> cids) {
		this.cids = cids;
	}

	public Cid getCidSelecionado() {
		return cidSelecionado;
	}

	public void setCidSelecionado(Cid cidSelecionado) {
		this.cidSelecionado = cidSelecionado;
	}

}
