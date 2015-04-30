package br.com.pbd.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.pbd.core.RegraNegocioException;
import br.com.pbd.jdbc.ConnectionFactory;
import br.com.pbd.model.Atividade;

public class AtividadeDao {
	private static AtividadeDao instance;
	private Connection connection;
	
	public AtividadeDao(){
		this.connection = new ConnectionFactory().getConnection();
	}
	
	public static AtividadeDao getInstance(){
		if(instance == null){
			instance = new AtividadeDao();
		}
		
		return instance;
	}
	
	public void inserir(Atividade atividade){
		
		beforeInsert(atividade);
		
		String sql = "insert into atividades (nome, executada) values (?,?)";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, atividade.getNome());
			stmt.setBoolean(2, atividade.getExecutada());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void beforeInsert(Atividade atividade) {
		
		if(atividade.getNome() == null || atividade.getNome().equals("")){
			throw new RegraNegocioException("Preencha o nome da tarefa!");
		}
		
		Atividade atividadeConsultada = consultarPorNome(atividade);
		if(atividadeConsultada != null){
			throw new RegraNegocioException("Já existe uma tarefa cadastrada com este nome!");
		}
		
	}

	public List<Atividade> listarTodas(){
		try {
			List<Atividade> atividades = new ArrayList<Atividade>();
			PreparedStatement stmt = this.connection.prepareStatement("select * from atividades");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				Atividade atividadeTemp = new Atividade();
				atividadeTemp.setId(rs.getLong("id"));
				atividadeTemp.setNome(rs.getString("nome"));
				atividadeTemp.setExecutada(rs.getBoolean("executada"));
				
				atividades.add(atividadeTemp);
			}
			
			rs.close();
			stmt.close();
			return atividades;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void delete(Atividade atividade){
		String sql = "delete from atividades where id = ?";
		try {
			PreparedStatement stmt = this.connection.prepareStatement(sql);
			stmt.setLong(1, atividade.getId());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void update(Atividade atividade){
		String sql = "update atividades set nome = ?, executada = ? where id = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, atividade.getNome());
			stmt.setBoolean(2, atividade.getExecutada());
			stmt.setLong(3, atividade.getId());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Atividade> listar(Atividade atividade){
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select * from atividades ");
			sql.append(" where 1=1 ");
			
			if(atividade.getExecutada() != null){
				sql.append(" and executada = ? ");
			}
			
			List<Atividade> atividades = new ArrayList<Atividade>();
			PreparedStatement stmt = this.connection.prepareStatement(sql.toString());
			
			if(atividade.getExecutada() != null){
				stmt.setBoolean(1, atividade.getExecutada());
			}
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				Atividade atividadeTemp = new Atividade();
				atividadeTemp.setId(rs.getLong("id"));
				atividadeTemp.setNome(rs.getString("nome"));
				atividadeTemp.setExecutada(rs.getBoolean("executada"));
				
				atividades.add(atividadeTemp);
			}
			
			rs.close();
			stmt.close();
			return atividades;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Atividade consultarPorNome(Atividade atividade){
		Atividade atividadeTemp;
		try {
			PreparedStatement stmt = this.connection.prepareStatement("select * from atividades where nome = ? ");
			stmt.setString(1, atividade.getNome() != null ? atividade.getNome() : "");
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				atividadeTemp = new Atividade();
				atividadeTemp.setId(rs.getLong("id"));
				atividadeTemp.setNome(rs.getString("nome"));
				atividadeTemp.setExecutada(rs.getBoolean("executada"));
			}else{
				atividadeTemp = null;
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return atividadeTemp;
	}
}
