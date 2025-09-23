package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidade.Cidades;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Cidades.class)
public class CidadeConverter implements Converter<Object>, Serializable {

	
	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		Cidades cidades = (Cidades) entityManager.find(Cidades.class, Long.parseLong(codigoCidade));
		
		return cidades;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object cidades) {
		
		if (cidades == null) {
			return null;
		}
		if (cidades instanceof Cidades) {
			return ((Cidades) cidades).getId().toString();
		}else {
			return cidades.toString();
		}
	}

}
