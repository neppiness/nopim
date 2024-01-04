package com.neppiness.nopim.repository;

import com.neppiness.nopim.dto.CompanyResponse;
import java.util.List;

public interface CompanyCustomRepository {

    List<CompanyResponse> findByParameters(String name, String region, String country);

}
