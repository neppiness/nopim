package recruitment.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Company;
import recruitment.repository.CompanyRepository;

@Transactional
@SpringBootTest
class CompanyCustomRepositoryImplTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    CompanyRepository companyRepository;

    @DisplayName(value = "findBySearchRequest 테스트")
    @Test
    void findBySearchRequestTest() throws JsonProcessingException {
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

        String givenRegion = "판교";
        String givenCountry = "대한민국";
        List<Company> foundCompanyList = companyRepository.findByParameters(null, givenRegion, givenCountry);
        for (Company foundCompany : foundCompanyList) {
            String foundCompanyAsString = objectWriter.writeValueAsString(foundCompany);
            System.out.println(foundCompanyAsString);
        }
    }

}