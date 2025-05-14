package com.alemcrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alemcrm.model.Company;

import com.alemcrm.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public List<Company> listAllCompany() {
        return companyRepository.findAll();
    }
}
