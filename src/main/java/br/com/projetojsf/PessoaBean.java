package br.com.projetojsf;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "pessoaBean")
public class PessoaBean  {

	private String nome;
	private String sobrenome;
	public String getNome() {
		return nome;
	}
	public void setNome(String name) {
		this.nome = name;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	
}
