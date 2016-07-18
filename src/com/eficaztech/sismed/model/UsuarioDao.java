package com.eficaztech.sismed.model;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.avaje.ebean.Query;
import com.eficaztech.sismed.util.SismedServer;
import com.eficaztech.sismed.util.ValidationException;
import com.eficaztech.sismed.util.Validations;

public class UsuarioDao {

	public void save(Usuario usuario) throws ValidationException {
		Validations.validate(usuario);

		if (usuario.getId() == null) {
			SismedServer.ebean.save(usuario);
		} else {
			SismedServer.ebean.update(usuario);
		}
	}

	public Usuario find(Long id) {
		return SismedServer.ebean.find(Usuario.class, id);
	}

	public void delete(Usuario usuario) {
		SismedServer.ebean.delete(usuario);
	}

	public List<Usuario> all() {
		Query<Usuario> query = SismedServer.ebean.createQuery(Usuario.class).orderBy("nome");
		return query.findList();
	}

	public List<Usuario> find(String filtro) {
		String sql = "find usuario where nome like :filtro or usuario like :filtro order by nome";
		Query<Usuario> query = SismedServer.ebean.createQuery(Usuario.class, sql).setParameter("filtro", "%" + filtro + "%");
		return query.findList();
	}

	public Usuario login(String usuario, String senha) {
		String senhaCriptografada = DigestUtils.md5Hex(senha);
		String sql = "find usuario where usuario = :usuario and senha = :senha";
		Query<Usuario> query = SismedServer.ebean.createQuery(Usuario.class, sql).setParameter("usuario", usuario).setParameter("senha", senhaCriptografada);
		return query.findUnique();
	}

}
