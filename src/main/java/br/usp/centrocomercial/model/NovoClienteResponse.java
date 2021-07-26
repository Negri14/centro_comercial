package br.usp.centrocomercial.model;

public class NovoClienteResponse {
	
	private String about;
	
	public NovoClienteResponse(String aboutCliente) { this.about = aboutCliente; }

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	};
	
}
