package recruitment.service;

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
import recruitment.domain.Authority;
import recruitment.domain.Company;
import recruitment.domain.Job;
import recruitment.domain.Status;
import recruitment.domain.User;
import recruitment.dto.ApplicationResponse;
import recruitment.dto.CompanyRequest;
import recruitment.dto.JobRequest;
import recruitment.dto.JobResponse;
import recruitment.dto.JobSimpleResponse;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

@Transactional
@SpringBootTest
class JobServiceTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    private Company wanted;

    private Company naver;

    @BeforeEach
    void jobControllerTestSetup() {
        applicationRepository.deleteAll();
        userRepository.deleteAll();
        jobRepository.deleteAll();
        companyRepository.deleteAll();

        CompanyRequest companyRequestForWanted = CompanyRequest.builder()
                .name("원티드")
                .region("서울")
                .country("한국")
                .build();
        wanted = companyService.create(companyRequestForWanted);

        CompanyRequest companyRequestForNaver = CompanyRequest.builder()
                .name("네이버")
                .region("분당")
                .country("한국")
                .build();
        naver = companyService.create(companyRequestForNaver);

        JobRequest jobRequestForWanted = JobRequest.builder()
                .companyId(wanted.getId())
                .position("백엔드 주니어 개발자")
                .bounty(500_000L)
                .stack("Django")
                .description("원티드에서 백엔드 주니어 개발자를 채용합니다. 우대사항 - Django 사용 경험자.")
                .build();
        jobService.create(jobRequestForWanted);

        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("프론트엔드 시니어 개발자")
                .bounty(1_500_000L)
                .stack("react")
                .description("네이버에서 프론트엔드 시니어 개발자를 채용합니다. 필수사항 - react 활용 개발 경력 5년 이상")
                .build();
        jobService.create(jobRequestForNaver);
    }

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
                .isEqualTo(2);
    }

    @DisplayName("채용공고 추가 및 검색 기능 테스트")
    @Test
    void addJobAndSearchJobTest() throws JsonProcessingException {
        JobRequest jobRequest = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        jobService.create(jobRequest);

        List<JobSimpleResponse> foundJobs = jobService.search("tensorflow");
        for (JobSimpleResponse jobSimpleResponse : foundJobs) {
            String jobSimpleResponseAsString = objectWriter.writeValueAsString(jobSimpleResponse);
            System.out.println(jobSimpleResponseAsString);
        }
    }

    @DisplayName("채용공고 추가 및 검색 기능 테스트2")
    @Test
    void addJobAndSearchJobTest2() throws JsonProcessingException {
        JobRequest jobRequest = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        jobService.create(jobRequest);

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
        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobService.create(jobRequestForNaver);

        long jobId = createdJob.getId();
        JobResponse jobDetail = jobService.getDetail(jobId);
        String jobDetailAsString = objectWriter.writeValueAsString(jobDetail);
        System.out.println(jobDetailAsString);
    }

    @DisplayName("채용공고 ID를 통해 채용공고 삭제하는 기능 테스트")
    @Test
    void deleteJobByIdTest() {
        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobService.create(jobRequestForNaver);

        long jobId = createdJob.getId();
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
        User user = User.builder()
                .name("Kim-jeonghyun")
                .password("1234")
                .authority(Authority.MEMBER)
                .build();
        User createdUser = userRepository.save(user);

        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobService.create(jobRequestForNaver);
        ApplicationResponse createdApplication = jobService.apply(createdJob.getId(), "Kim-jeonghyun");

        Long createdUserId = createdUser.getId();
        Long createdJobId = createdJob.getId();
        Assertions
                .assertThat(createdApplication.getUserId())
                .isEqualTo(createdUserId);
        Assertions
                .assertThat(createdApplication.getJobId())
                .isEqualTo(createdJobId);
    }

}