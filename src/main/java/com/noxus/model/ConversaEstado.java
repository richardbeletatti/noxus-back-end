package com.noxus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ConversaEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client cliente;

    @ManyToOne
    private Intent intent;

    private int indicePerguntaAtual;

	public int getIndicePerguntaAtual() {
		return indicePerguntaAtual;
	}

	public void setIndicePerguntaAtual(int indicePerguntaAtual) {
		this.indicePerguntaAtual = indicePerguntaAtual;
	}

	public Client getCliente() {
		return cliente;
	}

	public void setCliente(Client cliente) {
		this.cliente = cliente;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intentEscolhida) {
		this.intent = intentEscolhida;
	}

}

