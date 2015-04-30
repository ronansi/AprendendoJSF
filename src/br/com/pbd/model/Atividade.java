package br.com.pbd.model;

public class Atividade implements Comparable<Atividade> {
	private Long id;
	private String nome;
	private Boolean executada;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Boolean getExecutada() {
		return executada;
	}
	public void setExecutada(Boolean executada) {
		this.executada = executada;
	}
	@Override
	public int compareTo(Atividade o) {
		return this.nome.compareToIgnoreCase(o.getNome());
	}	
}
