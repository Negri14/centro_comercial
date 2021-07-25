package br.usp.centrocomercial.model;

public class Item {

	private String about;
	private String pictureURI;
	private double valor;
	private int quantidadeCarrinho;

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getPictureURI() {
		return pictureURI;
	}

	public void setPictureURI(String pictureURI) {
		this.pictureURI = pictureURI;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public int getQuantidadeCarrinho() {
		return quantidadeCarrinho;
	}

	public void setQuantidadeCarrinho(int quantidade_carrinho) {
		this.quantidadeCarrinho = quantidade_carrinho;
	}
	
	
}
