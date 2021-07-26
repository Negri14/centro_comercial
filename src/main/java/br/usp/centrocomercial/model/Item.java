package br.usp.centrocomercial.model;

public class Item {

	private String about;
	private String id;
	private String nome;
	private String descricao;
	private double preco;
	private String pictureURI;
	private String categoria;
	private int quantidade_estoque;
	private int quantidadeCarrinho;
	
	
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public String getPictureURI() {
		return pictureURI;
	}
	public void setPictureURI(String pictureURI) {
		this.pictureURI = pictureURI;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public int getQuantidadeCarrinho() {
		return quantidadeCarrinho;
	}
	public void setQuantidadeCarrinho(int quantidadeCarrinho) {
		this.quantidadeCarrinho = quantidadeCarrinho;
	}
	public int getQuantidadeEstoque() {
		return quantidade_estoque;
	}
	public void setQuantidadeEstoque(int quantidadeEstoque) {
		this.quantidade_estoque = quantidadeEstoque;
	}
	
	

	
	
}
