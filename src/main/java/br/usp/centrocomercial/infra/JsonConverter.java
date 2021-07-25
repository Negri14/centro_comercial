package br.usp.centrocomercial.infra;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {
	
	public List<String> convertResultSetToJson(ResultSet resultSet) {
		
		List<String> jsonList = new LinkedList<>();
		
		while (resultSet.hasNext()) {

			QuerySolution querySolution = resultSet.next();
			String json = "{";
            Iterator<String> iterator =  querySolution.varNames();
            
            while(iterator.hasNext()){
            	
                String atributo = iterator.next();
                String valor = querySolution.get(atributo).toString();
                valor = valor.contains("^^http") ? valor.substring(0, valor.indexOf("^^http")) : "\""+valor+"\"";
                valor = valor.replace("\\\"", "");
                json = json + "\"" + atributo + "\"" + ":" +valor+", ";
                
            }
            
            json = json.substring(0, json.length() - 2) + "}";
            jsonList.add(json);
		
		}
		
		return jsonList;
	}
}
