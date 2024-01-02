package com.neppiness.recruitment.repository;

import com.neppiness.recruitment.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyCustomRepository {
}
