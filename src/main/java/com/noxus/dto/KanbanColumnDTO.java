package com.noxus.dto;

import java.util.List;
import java.util.stream.Collectors;
import com.noxus.model.KanbanColumn;

public class KanbanColumnDTO {
    private Long id;
    private String name;
    private List<KanbanCardDTO> cards;

    public KanbanColumnDTO() {}

    public KanbanColumnDTO(Long id, String name, List<KanbanCardDTO> cards) {
        this.id = id;
        this.name = name;
        this.cards = cards;
    }

    public KanbanColumnDTO(KanbanColumn column) {
        this.id = column.getId();
        this.name = column.getName();
        this.cards = column.getCards().stream()
            .map(KanbanCardDTO::new)
            .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<KanbanCardDTO> getCards() { return cards; }
    public void setCards(List<KanbanCardDTO> cards) { this.cards = cards; }
}
