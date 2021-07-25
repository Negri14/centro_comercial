package br.usp.centrocomercial.model;

import java.util.List;

public class CarrinhoResponse {
	
	private String aboutCarrinho;
	private List<Item> itensCarrinho;
	private double valorTotal;
	
	public CarrinhoResponse() {}

	public String getAboutCarrinho() {
		return aboutCarrinho;
	}

	public void setAboutCarrinho(String aboutCarrinho) {
		this.aboutCarrinho = aboutCarrinho;
	}

	public List<Item> getItensCarrinho() {
		return itensCarrinho;
	}

	public void setItensCarrinho(List<Item> itensCarrinho) {
		this.itensCarrinho = itensCarrinho;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal2) {
		this.valorTotal = valorTotal2;
	}
	
	
	
	
	
}
