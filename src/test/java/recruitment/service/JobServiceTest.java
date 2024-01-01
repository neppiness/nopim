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
import recruitment.domain.Job;
import recruitment.domain.Status;
import recruitment.dto.ApplicationResponse;
import recruitment.dto.JobRequest;
import recruitment.dto.JobResponse;
import recruitment.dto.JobSimpleResponse;
import recruitment.repository.JobRepository;

@Sql(scripts = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:data/init.sql")
@Transactional
@SpringBootTest
class JobServiceTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @DisplayName("모든 채용공고 조회 기능 테스트")
    @Test
    void findAllJobsTest() throws JsonProcessingException {
        List<JobSimpleResponse> allJobSimpleResponses = jobService.getAll();
        for (JobSimpleResponse jobSimpleResponse : allJobSimpleResponses) {
            String jobSimpleResponseAsString = objectWriter.writeValueAsString(jobSimpleResponse);
            System.out.println(jobSimpleResponseAsString);
        }
        Assertions
                .assertThat(allJobSimpleResponses.size())
                .isEqualTo(4);
    }

    @DisplayName("채용공고 추가 기능 테스트")
    @Test
    void createJobTest() {
        Long companyIdForNaver = 3L;
        JobRequest jobRequest = JobRequest.builder()
                .companyId(companyIdForNaver)
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobService.create(jobRequest);
        Optional<Job> mayBeFoundJob = jobRepository.findById(createdJob.getId());
        assert mayBeFoundJob.isPresent();
        Job foundJob = mayBeFoundJob.get();
        Assertions
                .assertThat(createdJob)
                .isEqualTo(foundJob);
    }

    @DisplayName("채용공고 검색 기능 테스트")
    @Test
    void searchJobTest() throws JsonProcessingException {
        List<JobSimpleResponse> foundJobs = jobService.search("네이버");
        String foundJobsAsString = objectWriter.writeValueAsString(foundJobs);
        System.out.println(foundJobsAsString);
        Assertions
                .assertThat(foundJobs.size())
                .isEqualTo(2);
    }

    @DisplayName("상세 채용공고 조회 기능 테스트")
    @Test
    void getJobDetailTest() throws JsonProcessingException {
        long jobId = 1L;
        JobResponse jobDetail = jobService.getDetail(jobId);
        String jobDetailAsString = objectWriter.writeValueAsString(jobDetail);
        System.out.println(jobDetailAsString);
    }

    @DisplayName("채용공고 삭제 기능 테스트")
    @Test
    void softDeleteJobByIdTest() {
        long jobId = 1L;
        jobService.softDelete(jobId);

        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        assert mayBeFoundJob.isPresent();
        Job foundJob = mayBeFoundJob.get();
        Assertions
                .assertThat(foundJob.getStatus())
                .isEqualTo(Status.CLOSE);
    }

    @DisplayName(value = "채용공고에 지원하는 기능 테스트")
    @Test
    void applyTest() {
        String username = "0414kjh";
        Long userId = 2L;
        Long jobId = 1L;

        Optional<Job> mayBeFoundJob = jobRepository.findById(jobId);
        assert mayBeFoundJob.isPresent();
        Job foundJob = mayBeFoundJob.get();
        ApplicationResponse createdApplication = jobService.apply(jobId, username);

        Assertions
                .assertThat(createdApplication.getUserId())
                .isEqualTo(userId);
        Assertions
                .assertThat(createdApplication.getJobId())
                .isEqualTo(jobId);
    }

}