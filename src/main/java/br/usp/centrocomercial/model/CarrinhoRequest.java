package br.usp.centrocomercial.model;

public class CarrinhoRequest {
	
	String aboutCarrinho;
	String aboutProduto;
	int quantidadeProduto;
	
	public CarrinhoRequest() {}

	public String getAboutCarrinho() {
		return aboutCarrinho;
	}

	public void setAboutCarrinho(String aboutCarrinho) {
		this.aboutCarrinho = aboutCarrinho;
	}

	public String getAboutProduto() {
		return aboutProduto;
	}

	public void setAboutProduto(String aboutProduto) {
		this.aboutProduto = aboutProduto;
	}

	public int getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(int quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	};
	
	
}
