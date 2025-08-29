package br.com.projetojsf;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "pessoabean")
public class PessoaBean  {

	private String name;
	private String sobrenome;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	
}
