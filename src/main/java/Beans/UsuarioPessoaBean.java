package Beans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import Dao.DaoEmail;
import Dao.DaoUsuario;
import Model.EmailUser;
import Model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaBean")
@ViewScoped
public class UsuarioPessoaBean {

	private boolean novoUsuario;
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private EmailUser emailUser = new EmailUser();
	private DaoUsuario<UsuarioPessoa> dao = new DaoUsuario<>();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<>();
	private List<UsuarioPessoa> pessoas = new ArrayList<UsuarioPessoa>();
	private BarChartModel barChartModel = new BarChartModel();

	@PostConstruct
	public void init() {
		pessoas = dao.listar(UsuarioPessoa.class);
		ChartSeries userSalario = new ChartSeries();

		for (UsuarioPessoa usuarioPessoa : pessoas) {
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());   //add salarios
		}
		
		barChartModel.addSeries(userSalario);
		barChartModel.setTitle("Gráfico de salários");
		novoUsuario = true;
	}
	
	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	public String salvar() {
		usuarioPessoa = dao.atualizar(usuarioPessoa);

		if (novoUsuario) {
			pessoas.add(usuarioPessoa);
		}
		usuarioPessoa = new UsuarioPessoa();
		init();

		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
		return "";
	}

	public String remover() {
		try {
			dao.removerTelefonesUser(usuarioPessoa);
			pessoas.remove(usuarioPessoa);
			usuarioPessoa = new UsuarioPessoa();
			init();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ",
						"Existem telefones para esse usuário"));
			}
		}
		return "";
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("http://viacep.com.br/ws/" + usuarioPessoa.getCep() + "/json");
			URLConnection con = url.openConnection();
			InputStream inputStream = con.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			UsuarioPessoa usuarioPessoaCep = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			
			usuarioPessoa.setCep(usuarioPessoaCep.getCep());
			usuarioPessoa.setLogradouro(usuarioPessoaCep.getLogradouro());
			usuarioPessoa.setComplemento(usuarioPessoaCep.getComplemento());
			usuarioPessoa.setBairro(usuarioPessoaCep.getBairro());
			usuarioPessoa.setLocalidade(usuarioPessoaCep.getLocalidade());
			usuarioPessoa.setUf(usuarioPessoaCep.getUf());
			usuarioPessoa.setUnidade(usuarioPessoaCep.getUnidade());
			usuarioPessoa.setIbge(usuarioPessoaCep.getIbge());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addEmail() {
		emailUser.setUsuarioPessoa(usuarioPessoa);
		emailUser = daoEmail.atualizar(emailUser);
		usuarioPessoa.getListEmails().add(emailUser);
		emailUser = new EmailUser();
		
		FacesContext faces = FacesContext.getCurrentInstance();
		faces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado: ", "Email cadastrado com sucesso"));
	}
	
	public void removerEmail() throws Exception {
		FacesContext faces = FacesContext.getCurrentInstance();
		ExternalContext context = faces.getExternalContext();
		String codigoemail = context.getRequestParameterMap().get("codigoemail");	
		EmailUser remover = new EmailUser();
		remover.setId(Long.parseLong(codigoemail));
		daoEmail.deletar(remover);
		usuarioPessoa.getListEmails().remove(remover);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado: ", "Email removido com sucesso"));
	}
	
	public void editar() {
		novoUsuario = false;
	}

	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	public List<UsuarioPessoa> getPessoas() {
		return pessoas;
	}

	public EmailUser getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}
	
	public DaoEmail<EmailUser> getDaoEmail() {
		return daoEmail;
	}
	
	public void setDaoEmail(DaoEmail<EmailUser> daoEmail) {
		this.daoEmail = daoEmail;
	}

}
