package br.usp.centrocomercial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.centrocomercial.model.Produto;
import br.usp.centrocomercial.service.ClienteService;
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
	
	@GetMapping(value = "/clientesCPF", params = "cpf")
	public List<Produto> obterLojasPorAtividade(@RequestParam("cpf") String cpf) {
		return this.service.buscarClientePorCPF(cpf);
	}
	
//	@PostMapping(value = "/insereCliente")
//	public void inserir() {
//		this.service.cadastrarCliente(null);
//	}
	
//	@GetMapping(value = "/clientesDB")
//	public String obterClientesDB() {
//		//return this.service.recuperarDB();
//	}

}
