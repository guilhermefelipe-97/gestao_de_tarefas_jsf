package br.com.esigjsf.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.com.esigjsf.model.Tarefa;

public class TarefaDAO {
    private Connection conexao;

    public TarefaDAO() {
        try {
			this.conexao = Conexao.conectar();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void inserir(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefa (titulo, descricao, responsavel, prioridade, deadline, situacao) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println(this.conexao);
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getResponsavel());
            stmt.setString(4, tarefa.getPrioridade());
            stmt.setString(5, tarefa.getDeadline());
            stmt.setString(6, tarefa.getSituacao());
            stmt.executeUpdate();
        }
    }

    public List<Tarefa> listar() throws SQLException {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefa";
        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(rs.getLong("id"));
                tarefa.setTitulo(rs.getString("titulo"));
                tarefa.setDescricao(rs.getString("descricao"));
                tarefa.setResponsavel(rs.getString("responsavel"));
                tarefa.setPrioridade(rs.getString("prioridade"));
                tarefa.setDeadline(rs.getString("deadline"));
                tarefa.setSituacao(rs.getString("situacao"));
                tarefas.add(tarefa);
            }
        }
        return tarefas;
    }

    public void atualizar(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE tarefa SET titulo = ?, descricao = ?, responsavel = ?, prioridade = ?, deadline = ?, situacao = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getResponsavel());
            stmt.setString(4, tarefa.getPrioridade());
            stmt.setString(5, tarefa.getDeadline());
            stmt.setString(6, tarefa.getSituacao());
            stmt.setLong(7, tarefa.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM tarefa WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<Tarefa> buscarTarefasComFiltros(
    	    Long id, 
    	    String titulo, 
    	    String responsavel, 
    	    String situacao
    	) throws SQLException {
    	    
    	    List<Tarefa> tarefas = new ArrayList<>();
    	    StringBuilder sql = new StringBuilder("SELECT * FROM tarefa WHERE 1=1");
    	    List<Object> parametros = new ArrayList<>();

    	    if (id != null) {
    	        sql.append(" AND id = ?");
    	        parametros.add(id);
    	    }

    	    if (titulo != null && !titulo.isEmpty()) {
    	        sql.append(" AND (titulo ILIKE ? OR descricao ILIKE ?)");
    	        parametros.add("%" + titulo + "%");
    	        parametros.add("%" + titulo + "%");
    	    }


    	    if (responsavel != null && !responsavel.isEmpty()) {
    	        sql.append(" AND responsavel = ?");
    	        parametros.add(responsavel);
    	    }


    	    if (situacao != null && !situacao.isEmpty()) {
    	        sql.append(" AND situacao = ?");
    	        parametros.add(situacao);
    	    }

    	    try (
    	        PreparedStatement stmt = this.conexao.prepareStatement(sql.toString());
    	    ) {

    	        for (int i = 0; i < parametros.size(); i++) {
    	            stmt.setObject(i + 1, parametros.get(i));
    	        }

    	        try (ResultSet rs = stmt.executeQuery()) {
    	            while (rs.next()) {
    	                Tarefa tarefa = new Tarefa();
    	                tarefa.setId(rs.getLong("id"));
    	                tarefa.setTitulo(rs.getString("titulo"));
    	                tarefa.setDescricao(rs.getString("descricao"));
    	                tarefa.setResponsavel(rs.getString("responsavel"));
    	                tarefa.setPrioridade(rs.getString("prioridade"));
    	                tarefa.setDeadline(rs.getString("deadline"));
    	                tarefa.setSituacao(rs.getString("situacao"));
    	                tarefas.add(tarefa);
    	            }
    	        }
    	    }

    	    return tarefas;
    	}
    
    public Tarefa buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM tarefa WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tarefa tarefa = new Tarefa();
                    tarefa.setId(rs.getLong("id"));
                    tarefa.setTitulo(rs.getString("titulo"));
                    tarefa.setDescricao(rs.getString("descricao"));
                    tarefa.setResponsavel(rs.getString("responsavel"));
                    tarefa.setPrioridade(rs.getString("prioridade"));
                    tarefa.setDeadline(rs.getString("deadline"));
                    tarefa.setSituacao(rs.getString("situacao"));
                    return tarefa;
                }
            }
        }
        return null;
    }

}