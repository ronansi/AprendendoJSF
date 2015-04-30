package br.com.pbd.core;

public class RegraNegocioException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String causa;

	public RegraNegocioException(String causa){
		this.causa = causa;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}
	
	
}
