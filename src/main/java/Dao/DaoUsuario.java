package Dao;

import Model.UsuarioPessoa;

public class DaoUsuario<E> extends DaoGenerico<UsuarioPessoa>{
	
	public void removerTelefonesUser(UsuarioPessoa pessoa) throws Exception{
		getEntityManager().getTransaction().begin();
		// usar o getReference, pois o hibernate detecta as entidades do banco como classes objetos no java
		//com isso é passado o objeto a ser removido juntamente com seu id 
		getEntityManager().remove(getEntityManager().getReference(UsuarioPessoa.class, pessoa.getId()));
		getEntityManager().getTransaction().commit();
	}
}