package recruitment.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import recruitment.dto.JobSimpleResponse;
import recruitment.repository.JobRepository;

@Sql(value = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "classpath:data/init.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class JobCustomRepositoryImplTest {

    private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private JobRepository jobRepository;

    @DisplayName(value = "검색 기능 테스트: 키워드 - 백엔드")
    @Test
    void searchByKeywordTest1() throws JsonProcessingException {
        List<JobSimpleResponse> foundJobSimpleResponseList = jobRepository.findByKeyword("백엔드");
        for (JobSimpleResponse jobSimpleResponse : foundJobSimpleResponseList) {
            String jobSimpleResponseAsString = objectWriter.writeValueAsString(jobSimpleResponse);
            System.out.println(jobSimpleResponseAsString);
        }
    }

    @DisplayName(value = "검색 기능 테스트: 키워드 - 개발자")
    @Test
    void searchByKeywordTest2() throws JsonProcessingException {
        List<JobSimpleResponse> foundJobSimpleResponseList = jobRepository.findByKeyword("개발자");
        for (JobSimpleResponse jobSimpleResponse : foundJobSimpleResponseList) {
            String jobSimpleResponseAsString = objectWriter.writeValueAsString(jobSimpleResponse);
            System.out.println(jobSimpleResponseAsString);
        }
    }

}