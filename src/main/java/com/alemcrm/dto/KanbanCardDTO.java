package com.alemcrm.dto;

import java.util.Objects;

import com.alemcrm.model.KanbanCard;

public class KanbanCardDTO {
    private Long id;
    private String title;
    private String description;

    public KanbanCardDTO() {}

    public KanbanCardDTO(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public KanbanCardDTO(KanbanCard card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // equals e hashCode
    @Override
    public int hashCode() {
        return Objects.hash(description, id, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KanbanCardDTO other = (KanbanCardDTO) obj;
        return Objects.equals(description, other.description) && Objects.equals(id, other.id)
                && Objects.equals(title, other.title);
    }
}
