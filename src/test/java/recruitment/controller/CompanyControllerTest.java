package recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Company;
import recruitment.dto.CompanyRequest;
import recruitment.repository.ApplicationRepository;

import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

@SpringBootTest
@Transactional
public class CompanyControllerTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private final static String senvexName = "센벡스";

    private final static String senvexCountry = "대한민국";

    private final static String senvexRegion = "당산";

    @Autowired
    CompanyController companyController;

    @Autowired
    JobController jobController;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void companyTestSetup() {
        applicationRepository.deleteAll();
        userRepository.deleteAll();
        jobRepository.deleteAll();
        companyRepository.deleteAll();
        CompanyRequest companyRequestForWanted = CompanyRequest.builder()
                .name("원티드")
                .region("서울")
                .country("한국")
                .build();
        companyController.create(companyRequestForWanted);

        CompanyRequest companyRequestForNaver = CompanyRequest.builder()
                .name("네이버")
                .region("분")
                .country("한국")
                .build();
        companyController.create(companyRequestForNaver);
    }

    @DisplayName(value = "회사 등록 테스트")
    @Test
    void addCompanyTest() {
        CompanyRequest companyRequestForSenvex = CompanyRequest.builder()
                .name(senvexName)
                .region(senvexRegion)
                .country(senvexCountry)
                .build();
        Company company = companyController.create(companyRequestForSenvex).getBody();
        assert company != null;
        long companyId = company.getId();
        Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
        assert mayBeFoundCompany.isPresent();
        Assertions
                .assertThat(company)
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
                .name(senvexName)
                .region(senvexRegion)
                .country(senvexCountry)
                .build();
        companyRepository.save(company3);

        String givenRegion = "판교";
        String givenCountry = "대한민국";
        CompanyRequest companySearchRequest = CompanyRequest.builder()
                .region(givenRegion)
                .country(givenCountry)
                .build();
        List<Company> foundCompanyList = companyController.search(companySearchRequest).getBody();
        assert foundCompanyList != null;

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
                .name(senvexName)
                .region(senvexRegion)
                .country(senvexCountry)
                .build();

        Company savedCompany = companyRepository.save(senvex);
        long savedCompanyId = savedCompany.getId();
        Company foundCompanyDetail = companyController.getDetail(savedCompanyId).getBody();

        String foundCompanyDetailAsString = objectWriter.writeValueAsString(foundCompanyDetail);
        System.out.println(foundCompanyDetailAsString);
        Assertions
                .assertThat(savedCompany)
                .isEqualTo(foundCompanyDetail);
    }

}
