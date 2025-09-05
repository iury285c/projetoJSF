package br.com.projetojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;


import br.com.dao.DaoGeneric;
import br.com.entidade.Pessoa;

@javax.faces.bean.ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean implements Serializable  {

	
	private static final long serialVersionUID = 1L;
	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	public void salvar() {
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
	}
	
	public void novo() {
		pessoa = new Pessoa();
	}
	
	public void deletar() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	
	public void carregarPessoas() {
		pessoas = daoGeneric.getListENtity(Pessoa.class);
	}
}
