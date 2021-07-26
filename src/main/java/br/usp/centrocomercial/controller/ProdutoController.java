package br.usp.centrocomercial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.centrocomercial.model.Produto;
import br.usp.centrocomercial.service.ProdutoService;

@RestController
public class ProdutoController {

	
	private ProdutoService service;
	
	@Autowired
	public ProdutoController(ProdutoService service) {
		this.service = service;
	}
	
	@GetMapping(value = "/produtos")
	public List<Produto> obterProdutos(@RequestParam("aboutLoja") String aboutLoja) {
		return this.service.buscarProdutos(aboutLoja);
	}
	
	@GetMapping(value = "/produtosInteresse")
	public List<Produto> obterProdutosPorInteresse(@RequestParam("aboutLoja") String aboutLoja, @RequestParam("interesse") String[] interesse) {
		return this.service.buscarProdutoPorCategoria(aboutLoja, interesse);
	}
	
	@GetMapping(value = "/delete")
	public void deletar() {
		this.service.deletar();
	}
	
}
