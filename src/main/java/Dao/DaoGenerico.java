package Dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import posjavamavenhibernate.HibernateUtil;

public class DaoGenerico<E> {
	private EntityManager em = HibernateUtil.getEntityManager();

	public void salvar(E entidade) {
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.persist(entidade);
		trans.commit();
	}

	public E consultar(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) em.find(entidade.getClass(), id);
		return e;
	}

	public E consultar2(Long id, Class<E> entidade) {
		em.clear();
		E e = (E) em.createQuery("from "+entidade.getSimpleName() + " where id = " + id).getSingleResult();   //getName traz pacote.classe já getSimpleName traz só a classe
		return e;
	}

	public E atualizar(E entidade) {
		em.clear();
		EntityTransaction trans = em.getTransaction();
	//	if (!trans.isActive()) {
			trans.begin();
		//}
		E entidadeAtual = em.merge(entidade);
		trans.commit();
		return entidadeAtual;
	}

	public void deletar(E entity) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entity);
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.createNativeQuery("delete from " + entity.getClass().getSimpleName().toLowerCase() + " where id = " + id) //envia um sql ao banco
				.executeUpdate(); // faz o delete
		trans.commit(); // grava a alteração no banco
	}

	public List<E> listar(Class<E> entidade) {
		List<E> list = em.createQuery("from " + entidade.getName()).getResultList();
		return list;
	}

	public EntityManager getEntityManager() {
		return em;
	}
}