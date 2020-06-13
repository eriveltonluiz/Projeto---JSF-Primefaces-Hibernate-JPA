package posjavamavenhibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
	private static EntityManagerFactory emf;
	
	static {
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("pos-java-maven-hibernate");
		}
	}
	
	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	public static Object getPrimaryKey(Object entidade) {
		return emf.getPersistenceUnitUtil().getIdentifier(entidade);
	}
}
