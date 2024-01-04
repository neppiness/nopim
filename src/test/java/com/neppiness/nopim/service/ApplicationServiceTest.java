package com.neppiness.nopim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.neppiness.nopim.dto.ApplicationResponse;
import java.util.List;
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
class ApplicationServiceTest {

    private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobService jobService;

    @DisplayName("유저 ID로 지원내역을 검색하는 기능 테스트")
    @Test
    void findApplicationsByUserIdTest() throws JsonProcessingException {
        String name = "0414kjh";
        Long jobIdForWanted = 1L;
        Long jobIdForNaver = 2L;
        jobService.apply(jobIdForWanted, name);
        jobService.apply(jobIdForNaver, name);

        List<ApplicationResponse> foundApplicationResponses = applicationService.getAllByUsername(name);
        assert foundApplicationResponses != null;

        String foundApplicationDtoInJson = objectWriter.writeValueAsString(foundApplicationResponses);
        System.out.println(foundApplicationDtoInJson);
        Assertions
                .assertThat(foundApplicationResponses.size())
                .isEqualTo(2);
    }

}