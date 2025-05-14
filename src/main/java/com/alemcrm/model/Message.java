package com.alemcrm.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String message;
	
	private LocalDateTime sendData;
	
	private String sendStatus;
	
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getSendData() {
		return sendData;
	}

	public void setSendData(LocalDateTime sendData) {
		this.sendData = sendData;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
}
