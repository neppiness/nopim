package recruitment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import recruitment.domain.Company;
import recruitment.dto.CompanyRequest;
import recruitment.repository.CompanyRepository;

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
        Company createdCompany = companyService.create(companyRequestForSenvex);
        long companyId = createdCompany.getId();
        Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
        assert mayBeFoundCompany.isPresent();
        Assertions
                .assertThat(createdCompany)
                .isEqualTo(mayBeFoundCompany.get());
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
        List<Company> foundCompanyList = companyService.search(companySearchRequest);

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

    @DisplayName(value = "회사 상세 정보 조회 테스트")
    @Test
    void getDetailTest() throws JsonProcessingException {
        Long savedCompanyId = 1L;
        Company foundCompanyDetail = companyService.getDetail(savedCompanyId);
        String foundCompanyDetailAsString = objectWriter.writeValueAsString(foundCompanyDetail);
        System.out.println(foundCompanyDetailAsString);
    }

}