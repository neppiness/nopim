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
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Company;
import recruitment.dto.CompanyRequest;
import recruitment.repository.CompanyRepository;

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
        Company company1 = Company.builder()
                .name("네이버클라우드 판교오피스")
                .region("판교")
                .country("대한민국")
                .build();
        companyRepository.save(company1);
        Company company2 = Company.builder()
                .name("스노우")
                .region("판교")
                .country("대한민국")
                .build();
        companyRepository.save(company2);
        Company company3 = Company.builder()
                .name("센벡스")
                .region("당산")
                .country("대한민국")
                .build();
        companyRepository.save(company3);

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
        }
        Assertions
                .assertThat(foundCompanyList.size())
                .isEqualTo(2);
    }

    @DisplayName(value = "회사 상세 정보 조회 테스트")
    @Test
    void getDetailTest() throws JsonProcessingException {
        Company senvex = Company.builder()
                .name("센벡스")
                .region("당산")
                .country("대한민국")
                .build();
        Company savedCompany = companyRepository.save(senvex);
        long savedCompanyId = savedCompany.getId();

        Company foundCompanyDetail = companyService.getDetail(savedCompanyId);
        String foundCompanyDetailAsString = objectWriter.writeValueAsString(foundCompanyDetail);
        System.out.println(foundCompanyDetailAsString);
        Assertions
                .assertThat(savedCompany)
                .isEqualTo(foundCompanyDetail);
    }

}