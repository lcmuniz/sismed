package com.eficaztech.sismed.view;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.avaje.ebean.EbeanServerFactory;
import com.eficaztech.sismed.model.Usuario;
import com.eficaztech.sismed.model.UsuarioDao;
import com.eficaztech.sismed.util.Sessao;
import com.eficaztech.sismed.util.SismedServer;

public class LoginView {

	@Wire
	Window loginWindow;

	@Wire("#loginWindow #sistemaTextbox")
	Textbox sistemaTextbox;

	@Wire("#loginWindow #usuarioTextbox")
	Textbox usuarioTextbox;

	@Wire("#loginWindow #senhaTextbox")
	Textbox senhaTextbox;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		Sessao.set(Sessao.USUARIO, "");
		sistemaTextbox.setFocus(true);
	}

	@Command
	public void login() {

		if (sistemaTextbox.getValue().isEmpty() || usuarioTextbox.getValue().isEmpty() || senhaTextbox.getValue().isEmpty()) {
			Messagebox.show("Login inválido.", "Sismed", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		try {
			SismedServer.ebean = EbeanServerFactory.create(sistemaTextbox.getValue());
		}
		catch(Exception e) {
			Messagebox.show("Não foi possível entrar no sistema. Verifique suas credenciais e tente novamente.", "Sismed", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		UsuarioDao dao = new UsuarioDao();
		Usuario usuario = dao.login(usuarioTextbox.getValue(), senhaTextbox.getValue());

		if (usuario != null) {
			Sessao.set(Sessao.USUARIO, usuario);
			Executions.sendRedirect("/consulta");
		} else {
			Messagebox.show("Login inválido.", "Sismed", Messagebox.OK, Messagebox.EXCLAMATION);
		}

	}
}
