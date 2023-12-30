package recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
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

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class JobControllerTest {

    static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyController companyController;

    @Autowired
    JobController jobController;

    Company wanted;

    Company naver;

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
        wanted = companyController.create(companyRequestForWanted).getBody();

        CompanyRequest companyRequestForNaver = CompanyRequest.builder()
                .name("네이버")
                .region("분당")
                .country("한국")
                .build();
        naver = companyController.create(companyRequestForNaver).getBody();

        JobRequest jobRequestForWanted = JobRequest.builder()
                .companyId(wanted.getId())
                .position("백엔드 주니어 개발자")
                .bounty(500_000L)
                .stack("Django")
                .description("원티드에서 백엔드 주니어 개발자를 채용합니다. 우대사항 - Django 사용 경험자.")
                .build();
        jobController.create(jobRequestForWanted).getBody();
        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("프론트엔드 시니어 개발자")
                .bounty(1_500_000L)
                .stack("react")
                .description("네이버에서 프론트엔드 시니어 개발자를 채용합니다. 필수사항 - react 활용 개발 경력 5년 이상")
                .build();
        jobController.create(jobRequestForNaver).getBody();
    }

    @Test
    @DisplayName("모든 채용공고 조회 기능 테스트")
    void findAllJobsTest() throws JsonProcessingException {
        Iterable<Job> allJobs = jobRepository.findAll();
        int count = 0;
        for (Job job : allJobs) {
            count++;
        }
        Assertions
                .assertThat(count)
                .isEqualTo(2);
    }

    @Test
    @DisplayName("채용공고 추가 및 검색 기능 테스트")
    void addJobAndSearchJobTest() throws JsonProcessingException {
        JobRequest jobRequest = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobController.create(jobRequest).getBody();

        Iterable<JobSimpleResponse> foundJobs = jobController
                .search("tensorflow").getBody();
        assert foundJobs != null;
        for (JobSimpleResponse jobSimpleResponse : foundJobs) {
            String json = ow.writeValueAsString(jobSimpleResponse);
            System.out.println(json);
        }
    }

    @Test
    @DisplayName("채용공고 추가 및 검색 기능 테스트2")
    void addJobAndSearchJobTest2() throws JsonProcessingException {
        JobRequest jobRequest = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobController.create(jobRequest).getBody();

        Iterable<JobSimpleResponse> foundJobs = jobController
                .search("네이버").getBody();
        assert foundJobs != null;
        String json = ow.writeValueAsString(foundJobs);
        System.out.println(json);

        int count = 0;
        for (JobSimpleResponse jobSimpleResponse : foundJobs) {
            count++;
        }
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("상세 채용공고 조회 기능 테스트")
    void getJobDetailTest() throws Exception {
        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobController.create(jobRequestForNaver).getBody();
        assert createdJob != null;
        long jobId = createdJob.getId();
        JobResponse jobDetail = jobController.getDetail(jobId).getBody();
        String jobDetailInJson = ow.writeValueAsString(jobDetail);
        System.out.println(jobDetailInJson);
    }

    @Test
    @DisplayName("채용공고 ID를 통해 채용공고 삭제하는 기능 테스트")
    void deleteJobByIdTest() {
        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("머신러닝 주니어 개발자")
                .bounty(500_000L)
                .stack("tensorflow")
                .description("네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우")
                .build();
        Job createdJob = jobController.create(jobRequestForNaver).getBody();
        assert createdJob != null;
        long jobId = createdJob.getId();
        jobController.softDelete(jobId);
        Iterable<JobSimpleResponse> foundJobs = jobController.search("tensorflow").getBody();
        assert foundJobs != null;
        for (JobSimpleResponse foundJob : foundJobs) {
            Assertions
                    .assertThat(foundJob.getStatus())
                    .isEqualTo(Status.CLOSE);
        }
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
        Job createdJob = jobController.create(jobRequestForNaver).getBody();
        assert createdJob != null;
        ApplicationResponse createdApplication = jobController
                .apply(createdJob.getId(), "Kim-jeonghyun")
                .getBody();
        assert createdApplication != null;

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
