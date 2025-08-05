package com.alemcrm.dto;

import java.util.List;

public class IntentDTO {
    private String name;
    private List<String> questions;
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getQuestions() {
		return questions;
	}
	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}
}
