package br.usp.centrocomercial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.centrocomercial.model.NovoClienteRequest;
import br.usp.centrocomercial.model.Produto;
import br.usp.centrocomercial.service.ClienteService;
import br.usp.centrocomercial.service.NovoClienteResponse;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Cliente")
public class ClienteController {
	
	private ClienteService service;
	
	@Autowired
	public ClienteController(ClienteService service) {
		this.service = service;
	}
	
	@GetMapping(value = "/clientes")
	public List<Produto> obterClientes() {
		return this.service.buscarClientes();
	}
	
	@GetMapping(value = "/clientes", params = "email")
	public List<Produto> buscarClienteEmail(@RequestParam(value = "email") String email) {
		return this.service.buscarClienteEmail(email);
	}
	
	@PostMapping(value = "/clientes")
	public NovoClienteResponse inserir(@RequestBody NovoClienteRequest request) {
		return this.service.cadastrarCliente(request);
	}
	
}
