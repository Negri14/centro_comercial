package br.usp.centrocomercial.model;

import java.util.List;

public class NovoClienteRequest {

	private String nome;
	private String email;
	private String cpf;
	private String aniversario;
	private List<String> interesses;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDataNascimento() {
		return aniversario;
	}

	public void setDataNascimento(String aniversario) {
		this.aniversario = aniversario;
	}

	public List<String> getInteresses() {
		return interesses;
	}

	public void setInteresses(List<String> interesses) {
		this.interesses = interesses;
	}
	
	

}
