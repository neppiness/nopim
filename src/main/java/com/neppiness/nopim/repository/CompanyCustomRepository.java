package com.neppiness.nopim.repository;

import com.neppiness.nopim.domain.Company;
import java.util.List;

public interface CompanyCustomRepository {

    List<Company> findByParameters(String name, String region, String country);

}
