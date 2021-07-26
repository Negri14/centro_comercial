package br.usp.centrocomercial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.centrocomercial.model.CarrinhoRequest;
import br.usp.centrocomercial.model.CarrinhoResponse;
import br.usp.centrocomercial.model.CompraRequest;
import br.usp.centrocomercial.service.CompraService;

@RestController
public class CompraController {
	
	private CompraService service;
	
	@Autowired
	public CompraController(CompraService compraService) {
		this.service = compraService;
	}
	
	@PostMapping("/efetivarCompra")
	public void registrarCompra(@RequestBody CompraRequest request) {
		this.service.gravarCompra(request);
	}
	
	@PostMapping("/criarCarrinho")
	public CarrinhoResponse criarCarrinho(@RequestBody CarrinhoRequest request) {
		return this.service.criarCarrinho(request);	
	}
	
	@PostMapping("/adicionarProduto")
	public CarrinhoResponse adicionarProduto(@RequestBody CarrinhoRequest request) {
		return this.service.adicionarProdutoCarrinho(request);
	}
	
	@PostMapping("/removerProduto")
	public CarrinhoResponse removerProduto(@RequestBody CarrinhoRequest request) {
		return this.service.removerProdutoCarrinho(request);
	}
	
	@PostMapping("/incrementar")
	public CarrinhoResponse incrementarQntProduto(@RequestBody CarrinhoRequest request) {
		return this.service.atualizarQntItens(request, 1);		
	}
	
	@PostMapping("/decrementar")
	public CarrinhoResponse decrementarQntProduto(@RequestBody CarrinhoRequest request) {
		return this.service.atualizarQntItens(request, -1);		
	}
	
	@GetMapping("/carrinho")
	public CarrinhoResponse decrementarQntProduto(@RequestParam("about") String about) {
		return this.service.obterCarrinhoAbout(about);		
	}
	
	@GetMapping("/triplas")
	public void decrementarQntProduto() {
		this.service.exibirTriplas();
	}
	
}
