package br.com.repository;

import br.com.entidade.Pessoa;

public interface IDaoPessoa {

	Pessoa consultarUsuario(String login, String senha);
}
