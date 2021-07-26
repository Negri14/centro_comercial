package br.usp.centrocomercial.service;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.usp.centrocomercial.infra.CentroComercialRepository;
import br.usp.centrocomercial.infra.Constants;
import br.usp.centrocomercial.infra.JsonConverter;
import br.usp.centrocomercial.model.AboutQnt;
import br.usp.centrocomercial.model.CarrinhoRequest;
import br.usp.centrocomercial.model.CarrinhoResponse;
import br.usp.centrocomercial.model.CompraRequest;
import br.usp.centrocomercial.model.Item;
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
									"DELETE DATA { <%s> ontologia:quantidade_estoque %d };"+
									"INSERT DATA { <%s> ontologia:quantidade_estoque %d. };";		
	
	String queryAdicionarItemCarrinho =	Constants.PREFIX + 
										"INSERT DATA {  <%s> ontologia:armazena <%s> }; ";		

	String queryRemoverItemCarrinho =	Constants.PREFIX + 
										"DELETE DATA {  <%s> ontologia:armazena <%s> }; ";	
	
	String queryAlterarQuantidadeCarrinho =	Constants.PREFIX + 
											"DELETE DATA {  <%s> ontologia:quantidade_carrinho %d };" +
											"INSERT DATA { <%s> ontologia:quantidade_carrinho %d. }; ";	

	String queryObterQntItens =	Constants.PREFIX
								+"SELECT ?about ?quantidade"
								+"{"
								+"    ?about rdf:type ontologia:Item_Carrinho."
								+"    ?about ontologia:quantidade_carrinho ?quantidade."
								+"    ?about ontologia:especifica ?produto;"
								+"    FILTER(?about = <%s>)"
								+ "   FILTER(?produto = <%s>)"
								+"}";
	
	String queryObterItens =	Constants.PREFIX
								+"SELECT ?item"
								+"{"
								+"    ?about rdf:type ontologia:Carrinho_de_Compras."
								+"    ?about ontologia:armazena ?item;"
								+"    FILTER(?about = <%s>)"
								+"}";

	String queryCarrinho =	Constants.PREFIX
							+"SELECT ?aboutCarrinho ?aboutItemCarrinho ?quantidade_carrinho ?aboutProduto ?id ?nome ?descricao ?categoria ?quantidade_estoque ((?quantidade_carrinho*?preco) AS ?total_produto) ?preco ?pictureURI"
							+"{"
							+"     ?aboutCarrinho      rdf:type ontologia:Carrinho_de_Compras."
							+"		OPTIONAL"
							+"		{ "
							+"	   		?aboutCarrinho      ontologia:armazena ?aboutItemCarrinho."
							+"     		?aboutItemCarrinho  ontologia:quantidade_carrinho ?quantidade_carrinho."
							+"     		?aboutItemCarrinho  ontologia:especifica ?aboutProduto."
							+"     		?aboutProduto       ontologia:preco ?preco."
							+"     		?aboutProduto       ontologia:imagem ?pictureURI."
			            	+"          ?aboutProduto ontologia:id ?id ."
			            	+"          ?aboutProduto ontologia:nome ?nome ."
			            	+"          ?aboutProduto ontologia:descricao ?descricao ."
			            	+"          ?aboutProduto ontologia:categoria ?categoria ."
			            	+"          ?aboutProduto ontologia:quantidade_estoque ?quantidade_estoque;"
							+ "		}" 
							+"    FILTER(?aboutCarrinho = <%s>)"
							+"}";

	
	private CentroComercialRepository repository;
	private ProdutoService produtoService;
	private JsonConverter jsonConverter;
	
	Type lProdutoType = new TypeToken<LinkedList<AboutQnt>>() {}.getType();

	@Autowired
	public CompraService(CentroComercialRepository repository, ProdutoService produtoService, JsonConverter jsonConverter) {
		this.repository = repository;
		this.produtoService = produtoService;
		this.jsonConverter = jsonConverter;
	}

	public void gravarCompra(CompraRequest request) {
		
		//List<String> idItensAdicionados = this.inserirItensCarrinho(request.getItensCarrinho());
		//String idCarrinho = this.inserirCarrinho(idItensAdicionados, request.getValorTotal());
		this.updateUsuario(request.getAboutCarrinho(), request.getAboutCliente());
		//this.updateQuantidade(request.getItensCarrinho());
		this.inserirOrdemServico(request.getAboutCarrinho(), request.getFormaPagamento());
		//this.repository.ler();
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

	public CarrinhoResponse criarCarrinho(CarrinhoRequest request) { //AJUSTAR A MERDA DO VALOR TOTAL
		
		String aboutItemCarrinho = Constants.URI_ONTOLOGIA.concat(UUID.randomUUID().toString());
		System.out.println(String.format(this.queryInserirItemCarrinho, aboutItemCarrinho, request.getQuantidadeProduto(), request.getAboutProduto()));
		repository.executeUpdate(String.format(this.queryInserirItemCarrinho, aboutItemCarrinho, request.getQuantidadeProduto(), request.getAboutProduto()));
		
		String aboutCarrinho = Constants.URI_ONTOLOGIA.concat(UUID.randomUUID().toString());
		System.out.println(String.format(this.queryInserirCarrinho, aboutCarrinho, ("ontologia:armazena"+"<"+aboutItemCarrinho+">"), 0.0));
		repository.executeUpdate(String.format(this.queryInserirCarrinho, aboutCarrinho, ("ontologia:armazena"+"<"+aboutItemCarrinho+">"), 0.0));
		
		System.out.println(String.format(this.queryCarrinho, aboutItemCarrinho));
		ResultSet set = repository.executeSelect(String.format(this.queryCarrinho, aboutCarrinho));	
		List<String> jsonList = jsonConverter.convertResultSetToJson(set);
		System.out.println(new Gson().toJson(jsonList).toString());
		return montarObject(aboutCarrinho);

	}

	public CarrinhoResponse removerProdutoCarrinho(CarrinhoRequest request) {
	
		//System.out.println(request.getAboutCarrinho());
		ResultSet set = repository.executeSelect(String.format(this.queryObterItens, request.getAboutCarrinho()));	
		//System.out.println(set);

		while (set.hasNext()) {

			QuerySolution querySolution = set.next();
			String aboutItem = querySolution.get("item").toString();
			System.out.println(aboutItem);
			ResultSet setProduto = repository.executeSelect(String.format(this.queryObterQntItens, aboutItem, request.getAboutProduto()));	
			List<String> jsonList = jsonConverter.convertResultSetToJson(setProduto);
			List<AboutQnt> response = new Gson().fromJson(jsonList.toString(), lProdutoType);
			
			for (AboutQnt aq : response) {
				System.out.println(aq.getAbout()+" || "+aq.getQuantidade());	
				repository.executeUpdate(String.format(this.queryRemoverItemCarrinho, request.getAboutCarrinho(), aq.getAbout()));

			}

		
		}
		ResultSet setw = repository.executeSelect(String.format(this.queryCarrinho,request.getAboutCarrinho()));	
		List<String> jsonList = jsonConverter.convertResultSetToJson(setw);
		System.out.println(new Gson().toJson(jsonList).toString());	
	
		
		return montarObject(request.getAboutCarrinho());
	}

	public CarrinhoResponse adicionarProdutoCarrinho(CarrinhoRequest request) {
		String aboutItemCarrinho = Constants.URI_ONTOLOGIA.concat(UUID.randomUUID().toString());
		repository.executeUpdate(String.format(this.queryInserirItemCarrinho, aboutItemCarrinho, request.getQuantidadeProduto(), request.getAboutProduto()));
		repository.executeUpdate(String.format(this.queryAdicionarItemCarrinho, request.getAboutCarrinho(), aboutItemCarrinho));	
		ResultSet set = repository.executeSelect(String.format(this.queryCarrinho, request.getAboutCarrinho()));	
		List<String> jsonList = jsonConverter.convertResultSetToJson(set);
		System.out.println(new Gson().toJson(jsonList).toString());
		
		return montarObject(request.getAboutCarrinho());
	}

	public CarrinhoResponse atualizarQntItens(CarrinhoRequest request, int valorAtualizacao) {
		
		ResultSet set = repository.executeSelect(String.format(this.queryObterItens, request.getAboutCarrinho()));	
		
		while (set.hasNext()) {

			QuerySolution querySolution = set.next();
			String aboutItem = querySolution.get("item").toString();
			
			ResultSet setProduto = repository.executeSelect(String.format(this.queryObterQntItens, aboutItem, request.getAboutProduto()));	
			List<String> jsonList = jsonConverter.convertResultSetToJson(setProduto);
			List<AboutQnt> response = new Gson().fromJson(jsonList.toString(), lProdutoType);
			
			for (AboutQnt aq : response) {
				System.out.println("x_x");
				int novaQnt = aq.getQuantidade() + valorAtualizacao;
				
				repository.executeUpdate(String.format(this.queryAlterarQuantidadeCarrinho, aq.getAbout(), aq.getQuantidade(), aq.getAbout(), novaQnt));

			}

		
		}
		return montarObject(request.getAboutCarrinho());
		
	}
	
	
	public CarrinhoResponse montarObject(String aboutCarrinho) {
		
		CarrinhoResponse response = new CarrinhoResponse();
		List<Item> itens = new LinkedList<Item>();
		double valorTotal = 0.00;
		
		ResultSet set = repository.executeSelect(String.format(this.queryCarrinho, aboutCarrinho));
		
		while (set.hasNext()) {

			QuerySolution querySolution = set.next();
			
			
			response.setAboutCarrinho(querySolution.get("aboutCarrinho").toString());
			
			if (querySolution.get("aboutProduto") != null && !(querySolution.get("aboutProduto").toString().equals(""))) {
				Item i = new Item();
				i.setAbout(querySolution.get("aboutProduto").toString());
				i.setPictureURI(querySolution.get("pictureURI").toString());
				i.setCategoria(querySolution.get("categoria").toString());
				i.setDescricao(querySolution.get("descricao").toString());
				i.setId(querySolution.get("id").toString());
				i.setNome(querySolution.get("nome").toString());
				i.setQuantidadeEstoque(Integer.parseInt(querySolution.get("quantidade_estoque").toString().toString().substring(0, querySolution.get("quantidade_estoque").toString().indexOf("^^http"))));
				i.setPreco(Double.parseDouble(querySolution.get("preco").toString().substring(0, querySolution.get("preco").toString().indexOf("^^http"))));
				i.setQuantidadeCarrinho(Integer.parseInt(querySolution.get("quantidade_carrinho").toString().toString().substring(0, querySolution.get("quantidade_carrinho").toString().indexOf("^^http"))));
				valorTotal = valorTotal + Double.parseDouble(querySolution.get("total_produto").toString().substring(0, querySolution.get("total_produto").toString().indexOf("^^http")));
				
				response.setValorTotal(valorTotal);
				
				itens.add(i);
			}
		}
		
		response.setItensCarrinho(itens);
		
		return response;	

	}
	
	public CarrinhoResponse obterCarrinhoAbout(String aboutCarrinho) {
		ResultSet set = repository.executeSelect(String.format(this.queryCarrinho, aboutCarrinho));	
		List<String> jsonList = jsonConverter.convertResultSetToJson(set);
		System.out.println(new Gson().toJson(jsonList).toString());
		return montarObject(aboutCarrinho);
	}

}
