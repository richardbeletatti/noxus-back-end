package com.noxus.dto;

import java.util.List;

public class BotConfigDTO {
	private String whatsappNumber;
	private String greetingMessage;
	private List<IntentDTO> intents;

	public String getWhatsappNumber() {
		return whatsappNumber;
	}

	public void setWhatsappNumber(String whatsappNumber) {
		this.whatsappNumber = whatsappNumber;
	}

	public String getGreetingMessage() {
		return greetingMessage;
	}

	public void setGreetingMessage(String greetingMessage) {
		this.greetingMessage = greetingMessage;
	}

	public List<IntentDTO> getIntents() {
		return intents;
	}

	public void setIntents(List<IntentDTO> intents) {
		this.intents = intents;
	}
}
