package br.usp.centrocomercial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.usp.centrocomercial.model.CompraRequest;
import br.usp.centrocomercial.service.CompraService;

@RestController
public class CompraController {

	
	private CompraService compraService;
	
	@Autowired
	public CompraController(CompraService compraService) {
		this.compraService = compraService;
	}
	
	@PostMapping("/efetivarCompra")
	public void registrarCompra(@RequestBody CompraRequest request) {
		this.compraService.gravarCompra(request);
	}
	
}
