package br.com.pbd.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.pbd.core.Constantes;
import br.com.pbd.core.RegraNegocioException;
import br.com.pbd.jdbc.dao.AtividadeDao;
import br.com.pbd.model.Atividade;

@ManagedBean
public class AtividadeBean {
	private String nomeAtividade;
	private int tipoListagem = 1;
	
	public String getNomeAtividade() {
		return nomeAtividade;
	}

	public void setNomeAtividade(String nomeAtividade) {
		this.nomeAtividade = nomeAtividade;
	}
	
	public List<Atividade> getAtividades(){
		List<Atividade> atividades = new ArrayList<Atividade>();
		Atividade atividadeTemp = new Atividade();
		
		switch (tipoListagem) {
		case Constantes.TIPO_LISTAGEM_ALL: 
			atividades = AtividadeDao.getInstance().listarTodas(); 
			break;
			
		case Constantes.TIPO_LISTAGEM_EXECUTADAS:
			atividadeTemp.setExecutada(true);
			atividades = AtividadeDao.getInstance().listar(atividadeTemp);
			break;
			
		case Constantes.TIPO_LISTAGEM_NAO_EXECUTADAS:
			atividadeTemp.setExecutada(false);
			atividades = AtividadeDao.getInstance().listar(atividadeTemp);
			break;
			
		default:
			atividades = new ArrayList<Atividade>();
			break;
		}

		Collections.sort(atividades);
		return atividades;
	}

	public void setAtividadesAll(){
		tipoListagem = Constantes.TIPO_LISTAGEM_ALL;
	}
	
	public void setAtividadesExecutadas(){
		tipoListagem = Constantes.TIPO_LISTAGEM_EXECUTADAS;
	}
	
	public void setAtividadesNaoExecutadas(){
		tipoListagem = Constantes.TIPO_LISTAGEM_NAO_EXECUTADAS;
	}
	
	public void inserir(){
		Atividade atividade = new Atividade();
		
		atividade.setNome(nomeAtividade);
		atividade.setExecutada(Boolean.FALSE);
		
		try{
			AtividadeDao.getInstance().inserir(atividade);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Tarefa " + atividade.getNome() + " inserida com sucesso!", ""));
			this.nomeAtividade = "";
		} catch (RegraNegocioException e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getCausa(), ""));
		}
	}
	
	public void changeStatus(ActionEvent event){
		Atividade atividade = (Atividade) event.getComponent().getAttributes().get("atividade");
		atividade.setExecutada(!atividade.getExecutada());
		AtividadeDao.getInstance().update(atividade);
	}
	
	public void excluirExecutadas(){
		Atividade atividadeTemp = new Atividade();
		atividadeTemp.setExecutada(true);
		List<Atividade> atividades = AtividadeDao.getInstance().listar(atividadeTemp);
		
		for (Atividade atividade : atividades) {
			AtividadeDao.getInstance().delete(atividade);
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"As tarefas já executadas foram removidas da lista!", ""));
	}
}
