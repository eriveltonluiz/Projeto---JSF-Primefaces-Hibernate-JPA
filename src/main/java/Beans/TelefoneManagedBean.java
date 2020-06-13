package Beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import Dao.DaoTelefone;
import Dao.DaoUsuario;
import Model.Telefone;
import Model.UsuarioPessoa;

@ManagedBean(name = "telefoneManagedBean")
@ViewScoped
public class TelefoneManagedBean {

	boolean novoTelefone;
	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefone<Telefone> daoTel = new DaoTelefone<Telefone>();
	private List<Telefone> telefones = new ArrayList<Telefone>();
	private Telefone telefone = new Telefone();

	@PostConstruct
	public void init() {
		FacesContext faces = FacesContext.getCurrentInstance();
		ExternalContext context = faces.getExternalContext();
		String coduser = context.getRequestParameterMap().get("codigouser");
		user = daoUser.consultar2(Long.parseLong(coduser), UsuarioPessoa.class);
		novoTelefone = true;
	}

	public void removerTelefone() throws Exception {
		daoTel.deletar(telefone);
		user = daoUser.consultar2(user.getId(), UsuarioPessoa.class);
		telefone = new Telefone();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Telefone removido com sucesso"));
	}
	
	public void salvar() {
		telefone.setUsuarioPessoa(user);
		telefone = daoTel.atualizar(telefone);
		telefone = new Telefone();
		user = daoUser.consultar2(user.getId(), UsuarioPessoa.class);
		if (novoTelefone) {
			telefones.add(telefone);
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Telefone salvo com sucesso"));
	}

	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}

	public UsuarioPessoa getUser() {
		return user;
	}

	public void setDaoTel(DaoTelefone<Telefone> daoTel) {
		this.daoTel = daoTel;
	}

	public DaoTelefone<Telefone> getDaoTel() {
		return daoTel;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}
}
