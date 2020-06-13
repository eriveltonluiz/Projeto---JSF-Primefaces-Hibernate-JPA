package posjavamavenhibernate;

import java.util.List;

import org.junit.Test;

import Dao.DaoGenerico;
import Model.Telefone;
import Model.UsuarioPessoa;
import junit.framework.TestCase;

public class Teste extends TestCase {

	@Test
	public void testarInsert() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();

		UsuarioPessoa user = new UsuarioPessoa();

		user.setNome("teste");
		user.setSobrenome("teste");
		user.setLogin("eetsu");
		user.setSenha("admin");

		dao.salvar(user);
	}

	@Test
	public void testarConsulta() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();
		UsuarioPessoa user = new UsuarioPessoa();
		user.setId(2L);

		user = dao.consultar(user);
		System.out.println(user);
	}

	@Test
	public void testarConsulta2() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();
		UsuarioPessoa user = dao.consultar2(3L, UsuarioPessoa.class);
		System.out.println(user);
	}

	@Test
	public void testarAtualizar() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();
		UsuarioPessoa user = dao.consultar2(2L, UsuarioPessoa.class);
		user.setLogin("admin");

		user = dao.atualizar(user);
		System.out.println(user);

	}

	@Test
	public void testarDelete() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();
		UsuarioPessoa user = dao.consultar2(3L, UsuarioPessoa.class);
		try {
			dao.deletar(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(user);
	}

	@Test
	public void testarTodos() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();
		List<UsuarioPessoa> list = dao.listar(UsuarioPessoa.class);

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out.println("---------------------------");
		}
	}

	@Test
	public void testQueryList() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();
		List<UsuarioPessoa> list = dao.getEntityManager().createQuery("from UsuarioPessoa where login = 'eetsu'")
				.getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out.println("---------------------------");
		}
	}

	@Test
	public void testNamedQuery() {
		DaoGenerico<UsuarioPessoa> dao = new DaoGenerico<UsuarioPessoa>();
		List<UsuarioPessoa> list = dao.getEntityManager().createNamedQuery("UsuarioPessoa.Todos").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out.println("---------------------------");
		}

		List<UsuarioPessoa> list2 = dao.getEntityManager().createNamedQuery("UsuarioPessoa.buscaNome")
				.setParameter("login", "eetsu").getResultList();

		for (UsuarioPessoa usuarioPessoa : list2) {
			System.out.println(usuarioPessoa);
			System.out.println("---------------------------");
		}

	}

	@Test
	public void testeSalvarTel() {
		DaoGenerico dao = new DaoGenerico();
		UsuarioPessoa user = (UsuarioPessoa) dao.consultar2(1L, UsuarioPessoa.class);

		Telefone tel = new Telefone();
		tel.setTipo("casa");
		tel.setNumero("4534634534");
		tel.setUsuarioPessoa(user);
		dao.salvar(tel);

	}

	@Test
	public void testeBuscarTel() {
		DaoGenerico dao = new DaoGenerico();
		UsuarioPessoa user = (UsuarioPessoa) dao.consultar2(1L, UsuarioPessoa.class);

		for (Telefone tel : user.getListTel()) {
			System.out.println(tel.getTipo());
			System.out.println(tel.getNumero());
			System.out.println(tel.getUsuarioPessoa().getNome());
			System.out.println("----------------------------------------");
		}

	}

	@Test
	public void testeAtualizarTel() {
		DaoGenerico dao = new DaoGenerico();
		UsuarioPessoa usuarioPessoa = (UsuarioPessoa) dao.consultar2(1L, UsuarioPessoa.class);

		Telefone tel = (Telefone) dao.consultar2(2L, Telefone.class);
		tel.setTipo("casa");
		tel.setNumero("213124233");
		tel.setUsuarioPessoa(usuarioPessoa);

		dao.atualizar(tel);
	}

	@Test
	public void testeDeletarTel() {
		DaoGenerico<Telefone> dao = new DaoGenerico<Telefone>();
		Telefone tel = dao.consultar2(3L, Telefone.class);
		try {
			dao.deletar(tel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
