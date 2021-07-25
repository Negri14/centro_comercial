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
					           "SELECT ?about ?id ?nome ?preco ?quantidade_estoque ?categoria { "+
					            	  "?about rdf:type ontologia:Mercadoria ." +
					            	  "?about ontologia:id ?id ."+
					            	  "?about ontologia:nome ?nome ."+
					            	  "?about ontologia:preco ?preco ."+
					            	  "?usuario ontologia:categoria ?categoria ."+
					            	  "?about ontologia:quantidade_estoque ?quantidade_estoque ."
					            	  + "%s } "; 
	
	private String queryQntProdutos =   Constants.PREFIX+
							            "SELECT ?quantidade_estoque  { "+
							            "?about rdf:type ontologia:Mercadoria ." +
							            "?about ontologia:quantidade_estoque ?quantidade_estoque ." +
							            " %s } "; 
	
		
	private String queryFilter = "FILTER(%s = \"%s\")";
	
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
	
	public List<Produto> buscarProdutos() {
		ResultSet resultSet = repository.executeSelect(String.format(queryProdutos, ""));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		List<Produto> response = new Gson().fromJson(jsonList.toString(), lProdutoType);
		return response;
	}

	
	public List<Produto> buscarProdutoPorCategoria(String categoria) {
		String filter = String.format(queryFilter, "?categoria", categoria);
		ResultSet resultSet = repository.executeSelect(String.format(queryProdutos, filter));
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
