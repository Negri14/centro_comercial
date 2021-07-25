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
import br.usp.centrocomercial.infra.JsonConverter;
import br.usp.centrocomercial.model.Cliente;
import br.usp.centrocomercial.model.NovoClienteRequest;
import br.usp.centrocomercial.model.Produto;

@Service
public class ClienteService {
	
	String queryClientes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		                   "PREFIX ontologia: <http://www.centrocomercial.com/ontologia#> "+
		                   "SELECT ?about ?nome ?email ?cpf ?interesse{ "+
		                   	  "?about rdf:type ontologia:Cliente ." +
		                   	  "?about ontologia:nome ?nome ."+
		                   	  "?about ontologia:e-mail ?email ."+
		                   	  "?about ontologia:cpf ?cpf ."+
		                   	  "OPTIONAL {?about ontologia:interesse ?interesse }"+
		                   	 "%s }"; 
	
	
	String queryCadastrar =   "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
	         				  "PREFIX ontologia: <http://www.centrocomercial.com/ontologia#>"+
		                      "INSERT DATA { " +
		                      "   <%s> rdf:type ontologia:Cliente ;" +
			              	  "			ontologia:nome   %s;"+
			              	  "			ontologia:e-mail %s;"+
			              	  "			ontologia:cpf    %s. }";
	
	
	private String queryFilter = "FILTER(%s = \"%s\")";
	
	private CentroComercialRepository repository;
	private JsonConverter jsonConverter;

	
	Type lClienteType = new TypeToken<LinkedList<Cliente>>() {}.getType();

	@Autowired
	public ClienteService(CentroComercialRepository repository, JsonConverter jsonConverter) {
		this.repository = repository;
		this.jsonConverter = jsonConverter;
	}
	
	public List<Produto> buscarClientes() {
		ResultSet resultSet = repository.executeSelect(String.format(queryClientes, ""));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		List<Produto> response = new Gson().fromJson(jsonList.toString(), lClienteType);
		return response;
	}
	
	public List<Produto> buscarClientePorCPF(String cpf) {
		String filter = String.format(queryFilter, "?cpf", cpf);
		ResultSet resultSet = repository.executeSelect(String.format(queryClientes, filter));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		List<Produto> response = new Gson().fromJson(jsonList.toString(), lClienteType);
		return response;
	}
	
	
	public void cadastrarCliente(NovoClienteRequest novoCliente) {
		
		String queryInsert = String.format(queryCadastrar, novoCliente.getNome(),
				novoCliente.getEmail(), novoCliente.getCpf()); 
		
		this.repository.executeUpdate(queryInsert);
		
	}

}
