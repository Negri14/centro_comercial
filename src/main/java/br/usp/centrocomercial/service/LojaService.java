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
import br.usp.centrocomercial.model.Loja;

@Service
public class LojaService {
	
	private String queryLojas = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
					            "PREFIX ontologia: <http://www.centrocomercial.com/ontologia#> "+
					            "SELECT ?about ?nome ?site ?telefone ?atividade ?pictureURI { "+
					            	  "?about rdf:type ontologia:Loja ." +
					            	  "?about ontologia:nome ?nome ."+
					            	  "?about ontologia:site ?site ."+
					            	  "?about ontologia:telefone ?telefone ."+
					            	  "?about ontologia:atividade ?atividade ."+
					            	  "?about ontologia:imagem ?pictureURI."
					            	  + "%s } %s";
	
	private String queryFilter = "FILTER(lcase(str(%s)) = \"%s\")";
	
	private String queryOrder = "ORDER BY ?%s";
	
	private CentroComercialRepository repository;
	private JsonConverter jsonConverter;
	
	Type lLojaType = new TypeToken<LinkedList<Loja>>() {}.getType();

	@Autowired
	public LojaService(CentroComercialRepository owlRepository, JsonConverter jsonConverter) {
		this.repository = owlRepository;
		this.jsonConverter = jsonConverter;
	}
	
	public List<Loja> buscarLojas() {
		ResultSet resultSet = repository.executeSelect(String.format(this.queryLojas, "", ""));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		System.out.println(jsonList.toString());
		List<Loja> response = new Gson().fromJson(jsonList.toString(), lLojaType);
		return response;
	}
	
	public List<Loja> buscarLojasAtividade(String atividade) {
		String filter = String.format(queryFilter, "?atividade", atividade.toLowerCase());
		ResultSet resultSet = repository.executeSelect(String.format(this.queryLojas, filter, ""));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		List<Loja> response = new Gson().fromJson(jsonList.toString(), lLojaType);
		return response;

	}

	public List<Loja> buscarLojasOrdenado(String tipo) {
		ResultSet resultSet = repository.executeSelect(String.format(this.queryLojas, "", String.format(this.queryOrder, tipo)));
		List<String> jsonList = jsonConverter.convertResultSetToJson(resultSet);
		List<Loja> response = new Gson().fromJson(jsonList.toString(), lLojaType);
		return response;

	}
	
}
