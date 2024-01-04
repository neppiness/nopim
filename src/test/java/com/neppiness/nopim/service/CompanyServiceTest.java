package com.neppiness.nopim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.neppiness.nopim.domain.Company;
import com.neppiness.nopim.dto.CompanyDetailResponse;
import com.neppiness.nopim.dto.CompanyRequest;
import com.neppiness.nopim.dto.CompanyResponse;
import com.neppiness.nopim.repository.CompanyRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:data/init.sql")
@Transactional
@SpringBootTest
class CompanyServiceTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @DisplayName(value = "회사 등록 테스트")
    @Test
    void createTest() {
        CompanyRequest companyRequestForSenvex = CompanyRequest.builder()
                .name("센벡스")
                .region("당산")
                .country("대한민국")
                .build();
        CompanyResponse createdCompanyAsResponse = companyService.create(companyRequestForSenvex);
        long companyId = createdCompanyAsResponse.getId();
        Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
        assert mayBeFoundCompany.isPresent();

        Assertions
                .assertThat(createdCompanyAsResponse.getId())
                .isEqualTo(mayBeFoundCompany.get().getId());
        Assertions
                .assertThat(createdCompanyAsResponse.getName())
                .isEqualTo(mayBeFoundCompany.get().getName());
        Assertions
                .assertThat(createdCompanyAsResponse.getRegion())
                .isEqualTo(mayBeFoundCompany.get().getRegion());
        Assertions
                .assertThat(createdCompanyAsResponse.getCountry())
                .isEqualTo(mayBeFoundCompany.get().getCountry());
    }

    @DisplayName(value = "회사 검색 테스트")
    @Test
    void searchTest() throws JsonProcessingException {
        String givenRegion = "판교";
        String givenCountry = "대한민국";
        CompanyRequest companySearchRequest = CompanyRequest.builder()
                .region(givenRegion)
                .country(givenCountry)
                .build();
        List<CompanyResponse> foundCompanyResponses = companyService.search(companySearchRequest);

        for (CompanyResponse foundCompanyResponse : foundCompanyResponses) {
            String foundCompanyResponseAsString = objectWriter.writeValueAsString(foundCompanyResponse);
            System.out.println(foundCompanyResponseAsString);
            Assertions
                    .assertThat(foundCompanyResponse.getRegion())
                    .isEqualTo(givenRegion);
            Assertions
                    .assertThat(foundCompanyResponse.getCountry())
                    .isEqualTo(givenCountry);
        }
    }

    @DisplayName(value = "회사 상세 정보 조회 테스트")
    @Test
    void getDetailTest() throws JsonProcessingException {
        Long savedCompanyId = 1L;
        CompanyDetailResponse foundCompanyDetail = companyService.getDetail(savedCompanyId);
        String foundCompanyDetailResponseAsString = objectWriter.writeValueAsString(foundCompanyDetail);
        System.out.println(foundCompanyDetailResponseAsString);
    }

}