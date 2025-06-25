package com.alemcrm.dto;

import java.util.Objects;

import com.alemcrm.model.KanbanCard;

public class KanbanCardDTO {
    private Long id;
    private String title;
    private String description;
    private String phoneNumber;

    public KanbanCardDTO() {}

    public KanbanCardDTO(Long id, String title, String description, String phoneNumber, String conversationHistory) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    public KanbanCardDTO(KanbanCard card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.phoneNumber = card.getPhoneNumber();
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public int hashCode() {
        return Objects.hash(description, id, phoneNumber, title);
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
        return Objects.equals(description, other.description) 
            && Objects.equals(id, other.id)
            && Objects.equals(phoneNumber, other.phoneNumber)
            && Objects.equals(title, other.title);
    }
}
