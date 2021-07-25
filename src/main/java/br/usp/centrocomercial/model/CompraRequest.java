package br.usp.centrocomercial.model;

import java.util.List;

public class CompraRequest {

	private String aboutCliente;
	private List<ItemCarrinho> itensCarrinho;
	private double valorTotal;
	private String formaPagamento;

	public String getAboutCliente() {
		return aboutCliente;
	}

	public void setAboutCliente(String aboutCliente) {
		this.aboutCliente = aboutCliente;
	}

	public List<ItemCarrinho> getItensCarrinho() {
		return itensCarrinho;
	}

	public void setItensCarrinho(List<ItemCarrinho> itensCarrinho) {
		this.itensCarrinho = itensCarrinho;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	

}
