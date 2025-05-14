package com.alemcrm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemcrm.model.Company;
import com.alemcrm.service.CompanyService;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
	
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company newCompany = companyService.save(company);
        return ResponseEntity.ok(newCompany);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Company> buscarPorId(@PathVariable Long id) {
        return companyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Company>> listAllCompany() {
        return ResponseEntity.ok(companyService.listAllCompany());
    }
}
