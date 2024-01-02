package com.neppiness.recruitment.repository;

import com.neppiness.recruitment.domain.Company;
import java.util.List;

public interface CompanyCustomRepository {

    List<Company> findByParameters(String name, String region, String country);

}
