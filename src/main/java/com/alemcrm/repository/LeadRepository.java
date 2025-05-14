package com.alemcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long>{
    List<Lead> findByCompanyId(Long companyId);
}
