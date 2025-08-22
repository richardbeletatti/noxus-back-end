package com.noxus.dto;

public class LoginResponse {
    private String token;
    private String role;
    private String name;

    public LoginResponse(String token, String role, String name) {
        this.setToken(token);
        this.setRole(role);
        this.setName(name);
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
