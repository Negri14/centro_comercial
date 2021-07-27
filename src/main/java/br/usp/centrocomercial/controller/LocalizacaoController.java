package br.usp.centrocomercial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.centrocomercial.service.CompraService;
import br.usp.centrocomercial.service.LocalizacaoService;

@RestController
public class LocalizacaoController {
	
	private LocalizacaoService service;
	
	@Autowired
	public LocalizacaoController(LocalizacaoService service) {
		this.service = service;
	}
	
	@GetMapping("/local")
	public String local(@RequestParam(value = "loja", defaultValue = "Livraria Galáxia") String loja)  {
		return this.service.obterCaminho(loja);
	}
}
