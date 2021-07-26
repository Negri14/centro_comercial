package br.usp.centrocomercial.service;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.usp.centrocomercial.infra.CentroComercialRepository;
import br.usp.centrocomercial.infra.Constants;
import br.usp.centrocomercial.infra.JsonConverter;
import br.usp.centrocomercial.model.Cliente;
import br.usp.centrocomercial.model.NovoClienteRequest;
import br.usp.centrocomercial.model.NovoClienteResponse;
import br.usp.centrocomercial.model.Produto;

@Service
public class ClienteService {
	
	String queryClientes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
		                   "PREFIX ontologia: <http://www.centrocomercial.com/ontologia#> "+
		                   "SELECT ?about ?nome ?email ?cpf ?interesse { "+
		                   	  "?about rdf:type ontologia:Cliente ." +
		                   	  "?about ontologia:nome ?nome."+
		                   	  "?about ontologia:email ?email."+
		                   	  "?about ontologia:cpf ?cpf."+
		                   	  "OPTIONAL {?about ontologia:interesse ?interesse }"+
		                   	 "%s }"; 
	
	
	String queryCadastrar =   Constants.PREFIX 
							+ "INSERT DATA {" 
							+ " <%s> rdf:type ontologia:Cliente; "
							+ "      ontologia:nome \"%s\"; "
							+ "		 ontologia:email \"%s\";" 
							+ "		 ontologia:cpf \"%s\";"
							+ " 	 ontologia:nascimento \"%s\";" 
							+ " %s }";

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
	
	public List<Produto> buscarClienteEmail(String email) {
		String filter = String.format(queryFilter, "?email", email);
		System.out.println(String.format(queryClientes, filter));
		ResultSet resultSet = repository.executeSelect(String.format(queryClientes, filter));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		List<Produto> response = new Gson().fromJson(jsonList.toString(), lClienteType);
		return response;
	}
	
	
	public NovoClienteResponse cadastrarCliente(NovoClienteRequest cliente) {
		
		String aboutCliente = Constants.URI_ONTOLOGIA.concat(UUID.randomUUID().toString());

		String interesses = "";
		
		for (String interesse : cliente.getInteresses()) {
			interesses = interesses.concat("ontologia:interesse ").concat("\""+interesse+"\"").concat(";");
		}
		
		String queryInsert = String.format(queryCadastrar, aboutCliente, cliente.getNome().replace("\"", ""),
				cliente.getEmail(), cliente.getCpf(), cliente.getDataNascimento(), interesses); 
		System.out.println(queryInsert);
		this.repository.executeUpdate(queryInsert);
		
		return new NovoClienteResponse(aboutCliente);
		
		
	}

}
