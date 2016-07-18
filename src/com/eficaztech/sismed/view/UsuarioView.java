package com.eficaztech.sismed.view;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.eficaztech.sismed.enums.Funcao;
import com.eficaztech.sismed.model.Usuario;
import com.eficaztech.sismed.model.UsuarioDao;
import com.eficaztech.sismed.util.Notification;
import com.eficaztech.sismed.util.Sessao;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

@Init(superclass = true)
@AfterCompose(superclass = true)
public class UsuarioView extends View {

	private List<Usuario> usuarios;

	private Usuario usuarioSelecionado;

	@Wire
	Window editWindow;

	@Wire
	Listbox usuarioListbox;

	@Wire
	Textbox filtroTextbox;

	@Wire("#editWindow #excluirButton")
	Button excluirButton;

	@Wire("#editWindow #nomeTextbox")
	Textbox nomeTextbox;

	@Wire("#editWindow #senhaTextbox")
	Textbox senhaTextbox;

	@Wire("#editWindow #confirmacaoSenhaTextbox")
	Textbox confirmacaoSenhaTextbox;

	@Wire("#editWindow #senhaLabel")
	Label senhaLabel;

	@Wire("#editWindow #obsLabel")
	Label obsLabel;

	@Override
	public void afterCompose() {
		limpar();
	}

	@Command
	@NotifyChange("usuarios")
	public void pesquisar() {
		filtroTextbox.setFocus(true);
		UsuarioDao dao = new UsuarioDao();

		Usuario atual = (Usuario) Sessao.get(Sessao.USUARIO);

		if (atual.isAdm()) {
			usuarios = dao.find(filtroTextbox.getValue());
		}
		else {
			usuarios = dao.find(atual.getNome());
		}
		
	}

	@Command
	@NotifyChange("usuarios")
	public void limpar() {
		filtroTextbox.setValue("");
		pesquisar();
	}

	@Command
	@NotifyChange("usuarioSelecionado")
	public void novo() {
		usuarioSelecionado = new Usuario();
		editWindow.setTitle("Novo Usuário");
		editWindow.doModal();
		excluirButton.setDisabled(true);
		nomeTextbox.setFocus(true);
		senhaTextbox.setValue("");
		confirmacaoSenhaTextbox.setValue("");
		senhaLabel.setValue("Senha");
		obsLabel.setVisible(false);
	}

	@Command
	@NotifyChange("usuarioSelecionado")
	public void editar(@BindingParam("id") Long id) {
		UsuarioDao dao = new UsuarioDao();
		usuarioSelecionado = dao.find(id);
		editWindow.setTitle("Editar Usuário");
		editWindow.doModal();
		excluirButton.setDisabled(false);
		nomeTextbox.setFocus(true);
		senhaTextbox.setValue("");
		confirmacaoSenhaTextbox.setValue("");
		senhaLabel.setValue("Senha *");
		obsLabel.setVisible(true);
	}

	@Command
	@NotifyChange("usuarios")
	public void salvar() {
		UsuarioDao dao = new UsuarioDao();

		String senha = senhaTextbox.getValue();
		String confirmacaoSenha = confirmacaoSenhaTextbox.getValue();

		if (!senha.isEmpty()) {
			// senha foi informada
			String senhaCriptografada = criptografaSenha(senha);
			String confirmacaoSenhaCriptografada = criptografaSenha(confirmacaoSenha);
			usuarioSelecionado.setSenha(senhaCriptografada);
			usuarioSelecionado.setConfirmacaoSenha(confirmacaoSenhaCriptografada);
		}

		try {
			dao.save(usuarioSelecionado);
			Notification.show("success", "Item salvo com sucesso.");
		} catch (ValidationException e) {
			Validations.show(editWindow, e.getErros());
		}

		editWindow.setVisible(false);
		pesquisar();
	}

	private String criptografaSenha(String senha) {
		return DigestUtils.md5Hex(senha);
	}

	@Command
	public void excluir() {

		Messagebox.show("Excluir este USUÁRIO?", "Sismed", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
			@Override
			public void onEvent(Event e) {
				if (Messagebox.ON_YES.equals(e.getName())) {
					UsuarioDao dao = new UsuarioDao();
					dao.delete(usuarioSelecionado);
					Notification.show("success", "Item excluído com sucesso.");
					editWindow.setVisible(false);
					BindUtils.postNotifyChange(null, null, UsuarioView.this, "usuarios");
					pesquisar();
				}
			}
		});

	}

	@Command
	@NotifyChange("usuarios")
	public void cancelar() {
		editWindow.setVisible(false);
		pesquisar();
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public Funcao[] getFuncoes() {
		return Funcao.values();
	}

}
