package br.com.esigjsf.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.com.esigjsf.dao.TarefaDAO;
import br.com.esigjsf.model.Tarefa;

@ManagedBean(name = "tarefaBean")
public class TarefasManagedBean {
	private TarefaDAO daoTarefa = new TarefaDAO();
	private Tarefa tarefa = new Tarefa();
	private List<Tarefa> tarefas;
	private String filtroNumero;
    private String filtroTitulo;
    private String filtroResponsavel;
    private String filtroSituacao;
    private List<Tarefa> tarefasEncontradas = new ArrayList<Tarefa>();
	
	
	public String voltarPagina() {
		return "primeirapagina?faces-redirect=true";
	}
	
	public String avancarPagina() {
		init();
		return "segundapagina?faces-redirect=true";
	}

	public String cadastrarTarefa() {
		if (tarefa.getTitulo() == null || tarefa.getTitulo().isEmpty() || 
		        tarefa.getDescricao() == null || tarefa.getDescricao().isEmpty() || 
		        tarefa.getResponsavel() == null || tarefa.getResponsavel().isEmpty() ||
		        tarefa.getPrioridade() == null || tarefa.getPrioridade().isEmpty() ||
		        tarefa.getDeadline() == null) {
		        
		        FacesContext.getCurrentInstance().addMessage(null, 
		            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos os campos devem ser preenchidos!", ""));
		        return null;
		    }
		try {
			System.out.println(tarefa);
			daoTarefa.inserir(tarefa);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "segundapagina?faces-redirect=true";
	}
	
	public String deletarTarefa(Long id) {
	    if (id == null) {
	        System.out.println("ID inválido para exclusão.");
	        return "";
	    }

	    try {
	        daoTarefa.deletar(id);
	        return "segundapagina?faces-redirect=true";
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	        return "";
	    }
	}
	
	public String concluirTarefa(Tarefa tarefa) {
	    tarefa.setSituacao("Concluída");
	    try {
	        daoTarefa.atualizar(tarefa);
	        return "segundapagina?faces-redirect=true";
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	        return "";
	    }
	}
	
	public void buscarTarefas() {
	    try {
	        this.tarefasEncontradas.clear();

	        Long numero = (filtroNumero != null && !filtroNumero.isEmpty()) 
	            ? Long.parseLong(filtroNumero) 
	            : null;

	        TarefaDAO tarefaDAO = new TarefaDAO();
	        List<Tarefa> tarefasPesquisadas = tarefaDAO.buscarTarefasComFiltros(
	            numero,             
	            filtroTitulo,        
	            filtroResponsavel,   
	            filtroSituacao       
	        );

	        // Ordenar tarefas
	        Collections.sort(tarefasPesquisadas, Comparator.comparing(t -> !t.getSituacao().equals("Em andamento")));

	        this.tarefasEncontradas.addAll(tarefasPesquisadas);

	    } catch (NumberFormatException e) {
	        FacesContext.getCurrentInstance().addMessage(
	            null, 
	            new FacesMessage("Erro: Número inválido!")
	        );
	    } catch (SQLException e) {
	        FacesContext.getCurrentInstance().addMessage(
	            null, 
	            new FacesMessage("Erro ao buscar tarefas!")
	        );
	    }
	}

	
	@PostConstruct
    public void init() {
		buscarTarefas();
        String idParam = FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get("idTarefa");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.parseLong(idParam);
                this.tarefa = daoTarefa.buscarPorId(id);
            } catch (NumberFormatException | SQLException e) {
            }
        }
    }

    public String atualizarTarefa() {
        try {
            daoTarefa.atualizar(tarefa);
            return "segundapagina?faces-redirect=true";
        } catch (SQLException e) {
            return null;
        }
    }
    
    
	public TarefaDAO getDaoTarefa() {
		return daoTarefa;
	}


	public void setDaoTarefa(TarefaDAO daoTarefa) {
		this.daoTarefa = daoTarefa;
	}


	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}


	public Tarefa getTarefa() {
		return tarefa;
	}


	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}
	
	public String getFiltroNumero() { 
		return filtroNumero; 
	}
	
	public void setFiltroNumero(String filtroNumero) { 
		this.filtroNumero = filtroNumero; 
	}

	public String getFiltroTitulo() { 
		return filtroTitulo;
	}
	public void setFiltroTitulo(String filtroTitulo) { 
		this.filtroTitulo = filtroTitulo; 
	}

	public String getFiltroResponsavel() { 
		return filtroResponsavel;
	}
	public void setFiltroResponsavel(String filtroResponsavel) {
		this.filtroResponsavel = filtroResponsavel; 
	}

	public String getFiltroSituacao() {
		return filtroSituacao;
	}
	public void setFiltroSituacao(String filtroSituacao) {
		this.filtroSituacao = filtroSituacao;
	}

	public List<Tarefa> getTarefasEncontradas() {
		return tarefasEncontradas;
	}
	public void setTarefasEncontradas(List<Tarefa> tarefasEncontradas) {
		this.tarefasEncontradas = tarefasEncontradas; 
	}
}

