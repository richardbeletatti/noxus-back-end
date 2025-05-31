package com.alemcrm.dto;

import java.util.List;

public class KanbanColumnDTO {
    private Long id;
    private String name;
    private List<KanbanCardDTO> cards;

    // construtores
    public KanbanColumnDTO() {}
    public KanbanColumnDTO(Long id, String name, List<KanbanCardDTO> cards) {
        this.id = id;
        this.name = name;
        this.cards = cards;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<KanbanCardDTO> getCards() {
		return cards;
	}
	public void setCards(List<KanbanCardDTO> cards) {
		this.cards = cards;
	}

}

