package com.noxus.model;

import jakarta.persistence.*;

@Entity
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Amarra com a conversa
    @ManyToOne
    @JoinColumn(name = "conversa_estado_id", nullable = false)
    private ConversaEstado conversaEstado;

    // Amarra com a pergunta
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String resposta;

    public Long getId() {
        return id;
    }

    public ConversaEstado getConversaEstado() {
        return conversaEstado;
    }

    public void setConversaEstado(ConversaEstado conversaEstado) {
        this.conversaEstado = conversaEstado;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
