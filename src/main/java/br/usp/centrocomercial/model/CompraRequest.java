package br.usp.centrocomercial.model;

public class CompraRequest {

	private String aboutCliente;
	private String aboutCarrinho;
	private String formaPagamento;
	
	public String getAboutCliente() {
		return aboutCliente;
	}

	public void setAboutCliente(String aboutCliente) {
		this.aboutCliente = aboutCliente;
	}

	public String getAboutCarrinho() {
		return aboutCarrinho;
	}

	public void setAboutCarrinho(String aboutCarrinho) {
		this.aboutCarrinho = aboutCarrinho;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

}
