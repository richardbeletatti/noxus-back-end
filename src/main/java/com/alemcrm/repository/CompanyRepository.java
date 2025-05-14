package com.alemcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alemcrm.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	Company findByCnpj(String cnpj);
}
