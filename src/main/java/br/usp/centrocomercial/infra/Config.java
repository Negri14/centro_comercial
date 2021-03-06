package br.usp.centrocomercial.infra;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Config {
	
	private final String DIRECTORY  = "Databases/CentroComercial";
	private final String FILE  = "OntologiaCentroCusto.owl";

	
	@Bean
	public Dataset dataset() {
		
		Dataset dataset = TDBFactory.createDataset(DIRECTORY);
		dataset.begin(ReadWrite.WRITE);
		Model model = dataset.getDefaultModel();
		TDBLoader.loadModel(model, FILE);
		dataset.commit();
		dataset.end();
		return dataset;

	}
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }

}
