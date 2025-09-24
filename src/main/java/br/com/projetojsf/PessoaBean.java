package br.com.projetojsf;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.security.interfaces.DSAParams;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidade.Cidades;
import br.com.entidade.Estados;
import br.com.entidade.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@javax.faces.bean.ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean implements Serializable  {

	
	private static final long serialVersionUID = 1L;
	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	private List<SelectItem> cidades;
	private List<SelectItem> estados;
	private Part arquivofoto;
	
	public void salvar() throws IOException {
		
		byte[] imagemByte = getByte(arquivofoto.getInputStream());
		pessoa.setFotoIconBAse64Original(imagemByte);
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB :  bufferedImage.getType();
		int largura = 200;
		int altura = 200;
		
		BufferedImage resizedImage = new BufferedImage(altura, largura, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, largura, altura, null);
		g.dispose();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivofoto.getContentType().split("\\/")[1];
		ImageIO.write(resizedImage, extensao, baos);
		
		String miniImagem = "data" + arquivofoto.getContentType() + ";base64," +
		                     DatatypeConverter.printBase64Binary(baos.toByteArray());
		
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		
		
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Cadastrado com sucesso!");
	}
	
	public void novo() {
		pessoa = new Pessoa();
	}
	
	public void limpar() {
		Long idExustente = pessoa.getId();
		pessoa = new Pessoa();
		pessoa.setId(idExustente);
	}
	
	public void deletar() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		mostrarMsg("Excluido com sucesso!");
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	
	@PostConstruct
	public void carregarPessoas() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaU = (Pessoa) externalContext.getSessionMap().get("ususarioLogado");
		pessoas = daoGeneric.getListENtity(Pessoa.class);
		
	}
	
	public String logar() {
		
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		if (pessoaUser != null) {
			
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("ususarioLogado", pessoaUser);
			return "primeirapagina.jsf";
		}
		return "index.jsf";
	}
	
	public String deslogar() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		httpServletRequest.getSession().invalidate();
		
		return "index.jsf";
	}
	
	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("ususarioLogado");
		
		return pessoaUser.getPerfilUser().equals(acesso);
	}
	
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}
	
	public void pesquisacep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String cep = "";
			StringBuilder jsoncep = new StringBuilder();
			
			while ((cep = br.readLine()) != null) {
				jsoncep.append(cep);	
			}
			Pessoa gsonAux = new Gson().fromJson(jsoncep.toString(), Pessoa.class);
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setUnidade(gsonAux.getUnidade());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setEstado(gsonAux.getEstado());
			pessoa.setRegiao(gsonAux.getRegiao());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());
			pessoa.setDdd(gsonAux.getDdd());
			pessoa.setSiafi(gsonAux.getSiafi());
		} catch (Exception e) {
		 e.printStackTrace();
		 mostrarMsg("Erro ao consultar CEP");
		}
	}
	
	
	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}
	
	public void carregaCidade(AjaxBehaviorEvent event) {
		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();
			if (estado != null ) {
				pessoa.setEstados(estado);
				List<Cidades> cidades = JPAUtil.getEntityManager().createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();
				List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();
				
				for (Cidades cidade : cidades) {
					selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
					
				}
				setCidades(selectItemsCidade);
			}
		}
	
	public void editar() {
		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstados(estado);;
			List<Cidades> cidades = JPAUtil.getEntityManager().createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();
			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();
			
			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
				
			}
			setCidades(selectItemsCidade);
		
		}
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public Part getArquivofoto() {
		return arquivofoto;
	}

	public void setArquivofoto(Part arquivofoto) {
		this.arquivofoto = arquivofoto;
	}
	
	private byte[] getByte (InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf = null;
		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		}else {
			ByteArrayOutputStream boss = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1) {
				boss.write(buf, 0, len);
			}
			buf = boss.toByteArray();
		}
		return buf;
	}
	
	public void download () {
		Map<String, String> parms = FacesContext.getCurrentInstance().getExternalContext().getInitParameterMap();
		String fileDownloadId = parms.get("fileDownloadId");
	}
}
