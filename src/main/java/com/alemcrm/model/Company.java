package com.alemcrm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String cnpj;
    
    private String tokenWhatsapp;
    private String instanceWhatsapp;
    
    private Boolean isActivated;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getTokenWhatsapp() {
		return tokenWhatsapp;
	}

	public void setTokenWhatsapp(String tokenWhatsapp) {
		this.tokenWhatsapp = tokenWhatsapp;
	}

	public String getInstanceWhatsapp() {
		return instanceWhatsapp;
	}

	public void setInstanceWhatsapp(String instanceWhatsapp) {
		this.instanceWhatsapp = instanceWhatsapp;
	}

	public Boolean getIsActivated() {
		return isActivated;
	}

	public void setIsActivated(Boolean isActivated) {
		this.isActivated = isActivated;
	}

}
