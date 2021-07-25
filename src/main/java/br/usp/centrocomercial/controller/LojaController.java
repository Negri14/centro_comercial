package br.usp.centrocomercial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.centrocomercial.model.Loja;
import br.usp.centrocomercial.service.LojaService;

@RestController
public class LojaController {
	
	private LojaService service;
	
	@Autowired
	public LojaController(LojaService service) {
		this.service = service;
	}
	
	@GetMapping(value = "/lojas")
	public List<Loja> obterLojas() {
		return this.service.buscarLojas();
	}
	
	@GetMapping(value = "/lojasAtividade", params = "atividade")
	public List<Loja> obterLojasPorAtividade(@RequestParam(value="atividade", required=false) String atividade) {
		return this.service.buscarLojasAtividade(atividade);
	}
	
	@GetMapping(value = "/lojasOrdenado")
	public List<Loja> obterLojasOrdenado() {
		return this.service.buscarLojasOrdenado();
	}
}
