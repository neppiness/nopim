package com.neppiness.nopim.repository;

import com.neppiness.nopim.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyCustomRepository {
}
