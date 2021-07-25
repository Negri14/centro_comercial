package br.usp.centrocomercial.service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.usp.centrocomercial.infra.CentroComercialRepository;
import br.usp.centrocomercial.infra.Constants;
import br.usp.centrocomercial.model.CompraRequest;
import br.usp.centrocomercial.model.ItemCarrinho;

@Service
public class CompraService {


	String queryInserirItemCarrinho = Constants.PREFIX + 
						              "INSERT DATA {  <%s> rdf:type ontologia:Item_Carrinho ; ontologia:quantidade_carrinho %d; ontologia:especifica <%s>; }";
	
	String queryInserirCarrinho = 	Constants.PREFIX + 
						            "INSERT DATA { <%s> rdf:type ontologia:Carrinho_de_Compras; %s; ontologia:valor_total %f; }";
	
	String updateComprasUsuario = 	Constants.PREFIX + 
									"INSERT DATA { <%s> ontologia:possui <%s> } ";		
	
	String queryInserirOrdemServ = 	Constants.PREFIX + 
						            "INSERT DATA { <%s> rdf:type ontologia:Ordem_Servico;  ontologia:gera <%s>; ontologia:forma_pagamento \"%s\"; }";
	
	String queryUpdateQntProdutos =	Constants.PREFIX + 
									"DELETE DATA { <%s> ontologia:quantidade_estoque %d }; INSERT DATA { <%s> ontologia:quantidade_estoque %d. }; ";		
	
	
	private CentroComercialRepository repository;
	private ProdutoService produtoService;
	
	@Autowired
	public CompraService(CentroComercialRepository repository, ProdutoService produtoService) {
		this.repository = repository;
		this.produtoService = produtoService;
	}

	public void gravarCompra(CompraRequest request) {
		
		List<String> idItensAdicionados = this.inserirItensCarrinho(request.getItensCarrinho());
		String idCarrinho = this.inserirCarrinho(idItensAdicionados, request.getValorTotal());
		this.updateUsuario(idCarrinho, request.getAboutCliente());
		this.updateQuantidade(request.getItensCarrinho());
		this.inserirOrdemServico(idCarrinho, request.getFormaPagamento());
		this.repository.ler();
	}

	
	private void inserirOrdemServico(String idCarrinho, String formaPagamento) {
			String idOS = Constants.URI_ONTOLOGIA.concat(UUID.randomUUID().toString());
			this.repository.executeUpdate(String.format(this.queryInserirOrdemServ, idOS, idCarrinho, formaPagamento));
		
	}
	
	
	private void updateQuantidade(List<ItemCarrinho> itens) {
		for ( ItemCarrinho ic : itens) {
			int qntAnt = produtoService.obterQuantidadeItens(ic.getAboutProduto());
			int qntAtu = qntAnt - ic.getQuantidade();
			this.repository.executeUpdate(String.format(this.queryUpdateQntProdutos, ic.getAboutProduto(), qntAnt, ic.getAboutProduto(), qntAtu));
		}
	}
	
	
	private void updateUsuario(String idCarrinho, String cpf) {
		System.out.println(String.format(this.updateComprasUsuario, cpf, idCarrinho));
		repository.executeUpdate(String.format(this.updateComprasUsuario, cpf, idCarrinho));
	}

	private String inserirCarrinho(List<String> idItensAdicionados, double valorTotal) {
		
		String idCarrinho = Constants.URI_ONTOLOGIA.concat(UUID.randomUUID().toString());
		String insertItens = "";
		for (String id : idItensAdicionados) {
			insertItens = insertItens.concat("ontologia:armazena <".concat(id).concat(">; "));
		}
		
		System.out.println(String.format(this.queryInserirCarrinho, idCarrinho, insertItens, valorTotal));
		repository.executeUpdate(String.format(this.queryInserirCarrinho, idCarrinho, insertItens, valorTotal));
		return idCarrinho;
	}

	public List<String> inserirItensCarrinho(List<ItemCarrinho> itens) {
		
		List<String> itensAdicionados = new LinkedList<String>();
		
		for(ItemCarrinho ic : itens) {
			String idItemCarrinho = Constants.URI_ONTOLOGIA.concat(UUID.randomUUID().toString());
			itensAdicionados.add(idItemCarrinho);
			repository.executeUpdate(String.format(this.queryInserirItemCarrinho, idItemCarrinho, ic.getQuantidade(), ic.getAboutProduto()));

		}
		return itensAdicionados;
	}

}
