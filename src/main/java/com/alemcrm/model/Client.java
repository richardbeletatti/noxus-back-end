package com.alemcrm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String telefone;  // ex: 15556464769

    private String nome;

    @ManyToOne
    private Intent intentAtual; // fluxo atual (ex: Comprar, Vender...)

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Intent getIntentAtual() {
		return intentAtual;
	}

	public void setIntentAtual(Intent intentEscolhida) {
		this.intentAtual = intentEscolhida;
	}

}

