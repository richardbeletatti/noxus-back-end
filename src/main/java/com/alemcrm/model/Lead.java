package com.alemcrm.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "lead")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String phone;
    
    private String status; // Example: "New", "In Progress", "Closed"

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL)
    private List<Interaction> interactions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
