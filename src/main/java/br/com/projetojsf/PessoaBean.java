package br.com.projetojsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;


import br.com.dao.DaoGeneric;
import br.com.entidade.Pessoa;

@javax.faces.bean.ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean implements Serializable  {

	
	private static final long serialVersionUID = 1L;
	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	
	public void salvar() {
		pessoa = daoGeneric.merge(pessoa);
		
	}
	
	public void novo() {
		pessoa = new Pessoa();
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
	
}
