package br.usp.centrocomercial.service;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.usp.centrocomercial.infra.CentroComercialRepository;
import br.usp.centrocomercial.infra.Constants;
import br.usp.centrocomercial.infra.JsonConverter;
import br.usp.centrocomercial.model.Produto;
import br.usp.centrocomercial.model.QuantidadeEstoque;

@Service
public class ProdutoService {
	
	private String queryProdutos =  Constants.PREFIX+
					           "SELECT ?about ?id ?nome ?preco ?quantidade_estoque ?categoria ?pictureURI ?descricao { "+
					           		  "?aboutLoja rdf:type ontologia:Loja."+
					           		  "?aboutLoja ontologia:oferece ?about."+
					            	  "?about ontologia:id ?id ."+
					            	  "?about ontologia:nome ?nome ."+
					            	  "?about ontologia:preco ?preco ."+
					            	  "?about ontologia:descricao ?descricao ."+
					            	  "?about ontologia:categoria ?categoria ."+
					            	  "?about ontologia:quantidade_estoque ?quantidade_estoque ."+
					            	  "?about ontologia:imagem ?pictureURI ."
					            	  + "%s} "; 
	
	private String queryQntProdutos =   Constants.PREFIX+
							            "SELECT ?quantidade_estoque  { "+
							            "?about rdf:type ontologia:Mercadoria ." +
							            "?about ontologia:quantidade_estoque ?quantidade_estoque ." +
							            " %s } "; 
	
		
	private String queryFilter = "FILTER(%s = %s)";
	private String queryFilterInteresse = "FILTER(%s IN (%s))";

	private CentroComercialRepository repository;
	private JsonConverter jsonConverter;
	
	Type lProdutoType = new TypeToken<LinkedList<Produto>>() {}.getType();

	@Autowired
	public ProdutoService(CentroComercialRepository owlRepository, JsonConverter jsonConverter) {
		this.repository = owlRepository;
		this.jsonConverter = jsonConverter;
	}
	
	public void deletar() {
		this.repository.executeUpdate("DELETE { ?s ?o ?p } WHERE { ?s ?o ?p }");
	}
	
	public List<Produto> buscarProdutos(String aboutLoja) {
		//repository.ler();
		System.out.println(aboutLoja);
		String filter = String.format(queryFilter, "?aboutLoja", "<"+aboutLoja+">");
		System.out.println(String.format(queryProdutos, filter));
		ResultSet resultSet = repository.executeSelect(String.format(queryProdutos, filter));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		System.out.println(jsonList.toString());
		List<Produto> response = new Gson().fromJson(jsonList.toString(), lProdutoType);
		return response;
	}

	
	public List<Produto> buscarProdutoPorCategoria(String about, String[] interesse) {
		String filter = String.format(queryFilter, "?aboutLoja", "<"+about+">");
		String interesses = "";
		for (String x : interesse) {
			interesses = interesses + "\"" + x + "\"" + ",";
		}
		interesses = interesses.substring(0, interesses.length() - 1);
		System.out.print(interesses);
		String filter2 = String.format(queryFilterInteresse, "?categoria", interesses);
		ResultSet resultSet = repository.executeSelect(String.format(queryProdutos, filter.concat(filter2)));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		List<Produto> response = new Gson().fromJson(jsonList.toString(), lProdutoType);
		return response;
	}

	public int obterQuantidadeItens(String URI) {
		ResultSet resultSet = repository.executeSelect(String.format(this.queryQntProdutos, "FILTER(?about = <"+URI+">)"));
		List<String> result = jsonConverter.convertResultSetToJson(resultSet);
		QuantidadeEstoque item = new Gson().fromJson(result.get(0), QuantidadeEstoque.class);
		return item.getQuantidade_estoque();
	}

}
