package br.com.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

public class DaoGeneric<O> {

	public void salvar(O entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.persist(entidade);
		entityTransaction.commit();
		entityManager.close();
	}
	
	public O merge(O entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		O retorno = entityManager.merge(entidade);
		entityTransaction.commit();
		entityManager.close();
		return retorno;
	}
	
	public void deletePorId(O entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		Object id = JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery("delete from "+ entidade.getClass().getCanonicalName() + "where id = " + id).executeUpdate();
		entityTransaction.commit();
		entityManager.close();
		
	}
}
