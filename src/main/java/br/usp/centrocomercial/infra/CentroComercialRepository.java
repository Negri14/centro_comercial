package br.usp.centrocomercial.infra;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CentroComercialRepository {
		
	private Dataset dataset;

	@Autowired
	public CentroComercialRepository(Dataset dataset) {
		this.dataset = dataset;
	}
	
	
	public ResultSet executeSelect(String selectQuery) {
		
		dataset.begin(ReadWrite.READ) ;

		Query query = QueryFactory.create(selectQuery);
		QueryExecution  queryExecution = QueryExecutionFactory.create(query, dataset) ;
	    ResultSet resultSet = queryExecution.execSelect() ;

	    this.dataset.commit();
	    this.dataset.end();
	    
	    return resultSet;
	
	}
	
	public void executeUpdate(String updateQuery) {
		
		this.dataset.begin(ReadWrite.WRITE);
		
		
	    UpdateRequest request = UpdateFactory.create(updateQuery) ;
	    UpdateProcessor processor = UpdateExecutionFactory.create(request, dataset) ;
	    processor.execute() ;
		
	    this.dataset.commit();
	    this.dataset.end();

	}
	
	public void ler() {
		
		Dataset d = TDBFactory.createDataset("Databases/CentroEmpresarial");
		d.begin(ReadWrite.READ);
		
		 String qs1 = "SELECT * {?s ?p ?o}" ;        

		 try(QueryExecution qExec = QueryExecutionFactory.create(qs1, d)) {
		     ResultSet rs = qExec.execSelect() ;
		     ResultSetFormatter.out(rs) ;
		 } 

	}
	
}
