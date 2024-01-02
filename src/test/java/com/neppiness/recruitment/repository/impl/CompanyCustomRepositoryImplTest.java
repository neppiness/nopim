package com.neppiness.recruitment.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.neppiness.recruitment.domain.Company;
import com.neppiness.recruitment.repository.CompanyRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(value = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "classpath:data/init.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class CompanyCustomRepositoryImplTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    CompanyRepository companyRepository;

    @DisplayName(value = "findBySearchRequest 테스트")
    @Test
    void findBySearchRequestTest() throws JsonProcessingException {
        String givenRegion = "판교";
        String givenCountry = "대한민국";
        List<Company> foundCompanyList = companyRepository.findByParameters(null, givenRegion, givenCountry);
        for (Company foundCompany : foundCompanyList) {
            String foundCompanyAsString = objectWriter.writeValueAsString(foundCompany);
            System.out.println(foundCompanyAsString);

            Assertions
                    .assertThat(foundCompany.getRegion())
                    .isEqualTo(givenRegion);
            Assertions
                    .assertThat(foundCompany.getCountry())
                    .isEqualTo(givenCountry);
        }
    }

}