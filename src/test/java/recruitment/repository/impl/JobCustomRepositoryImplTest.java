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
import recruitment.domain.Job;
import recruitment.dto.JobSimpleResponse;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;

@Transactional
@SpringBootTest
class JobCustomRepositoryImplTest {

    private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @DisplayName(value = "검색 기능 테스트")
    @Test
    void searchByKeywordTest() throws JsonProcessingException {
        Company company = Company.builder()
                .name("센벡스")
                .region("당산")
                .country("대한민국")
                .build();
        Company savedCompany = companyRepository.save(company);
        String position = "백엔드 주니어 개발자";
        long bounty = 500_000L;
        String stack = "Django";
        String description = "원티드에서 백엔드 주니어 개발자를 채용합니다. 우대사항 - Django 사용 경험자.";

        Job job = Job.builder()
                .company(savedCompany)
                .position(position)
                .bounty(bounty)
                .stack(stack)
                .description(description)
                .build();
        jobRepository.save(job);

        List<JobSimpleResponse> foundJobSimpleResponseList = jobRepository.findByKeyword("센");
        for (JobSimpleResponse jobSimpleResponse : foundJobSimpleResponseList) {
            String jobSimpleResponseAsString = objectWriter.writeValueAsString(jobSimpleResponse);
            System.out.println(jobSimpleResponseAsString);
        }
    }

}