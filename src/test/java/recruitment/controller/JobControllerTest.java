package recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import recruitment.domain.Company;
import recruitment.domain.JobDto;
import recruitment.domain.JobSimpleDto;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class JobControllerTest {

    static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobController jobController;

    @Autowired
    CompanyController companyController;

    Company wanted;
    Company naver;

    @BeforeEach
    void jobControllerTestSetup() {
        jobController.deleteAllJobs();
        companyController.deleteAllCompanies();
        wanted = companyController.addCompany("원티드", "한국", "서울");
        naver = companyController.addCompany("네이버", "한국", "분당");
        jobController.addJob(
                wanted.getId(),
               "백엔드 주니어 개발자",
                500_000L,
                "Django",
                "원티드에서 백엔드 주니어 개발자를 채용합니다. 우대사항 - Django 사용 경험자."
        );
        jobController.addJob(
                naver.getId(),
                "프론트엔드 시니어 개발자",
                1_500_000L,
                "react",
                "네이버에서 프론트엔드 시니어 개발자를 채용합니다. 필수사항 - react 활용 개발 경력 5년 이상"
        );
    }

    @Test
    @DisplayName("모든 채용공고 조회 기능 테스트")
    void findAllJobsTest() throws JsonProcessingException {
        Iterable<JobSimpleDto> allJobs = jobController.findAllJobs();
        int count = 0;
        for (JobSimpleDto job : allJobs) {
            count++;
        }
        String jobDetailInJson = ow.writeValueAsString(allJobs);
        System.out.println(jobDetailInJson);
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("채용공고 추가 및 검색 기능 테스트")
    void addJobAndSearchJobTest() throws JsonProcessingException {
        JobSimpleDto addedJobInSimpleDto = jobController.addJob(
                naver.getId(),
                "머신러닝 주니어 개발자",
                500_000L,
                "tensorflow",
                "네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우"
        );

        ReflectionEquals re = new ReflectionEquals(addedJobInSimpleDto);
        Iterable<JobSimpleDto> foundJobs = jobController
                .searchJob("tensorflow");
        for (JobSimpleDto jobSimpleDto : foundJobs) {
            String json = ow.writeValueAsString(jobSimpleDto);
            System.out.println(json);
            assertThat(re.matches(jobSimpleDto)).isTrue();
        }
    }

    @Test
    @DisplayName("채용공고 추가 및 검색 기능 테스트2")
    void addJobAndSearchJobTest2() throws JsonProcessingException {
        JobSimpleDto addedJobInSimpleDto = jobController.addJob(
                naver.getId(),
                "머신러닝 주니어 개발자",
                500_000L,
                "tensorflow",
                "네이버에서 딥러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우"
        );

        Iterable<JobSimpleDto> foundJobs = jobController
                .searchJob("네이버");
        String json = ow.writeValueAsString(foundJobs);
        System.out.println(json);

        int count = 0;
        for (JobSimpleDto jobSimpleDto : foundJobs) { count++; }
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("상세 채용공고 조회 기능 테스트")
    void getJobDetailTest() throws Exception {
        JobSimpleDto addedJobInSimpleDto = jobController.addJob(
                naver.getId(),
                "머신러닝 주니어 개발자",
                500_000L,
                "tensorflow",
                "네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우"
        );
        long jobId = addedJobInSimpleDto.getId();
        JobDto jobDetail = jobController.getJobDetail(jobId);
        String jobDetailInJson = ow.writeValueAsString(jobDetail);
        System.out.println(jobDetailInJson);
    }

    @Test
    @DisplayName("채용공고 ID를 통해 채용공고 삭제하는 기능 테스트")
    void deleteJobByIdTest() {
        JobSimpleDto addedJobInSimpleDto = jobController.addJob(
                naver.getId(),
                "머신러닝 주니어 개발자",
                500_000L,
                "tensorflow",
                "네이버에서 머신러닝 주니어 개발자를 채용합니다. 필수사항 - 텐서플로우"
        );
        long jobId = addedJobInSimpleDto.getId();
        jobController.deleteJobById(jobId);
        Iterable<JobSimpleDto> foundJobs = jobController.searchJob(String.valueOf(jobId));
        int count = 0;
        for (JobSimpleDto foundJob : foundJobs) {
            count++;
        }
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("모든 채용공고 삭제 기능 테스트")
    void deleteAllJobsTest() {
        Iterable<JobSimpleDto> allJobs = jobController.findAllJobs();
        jobController.deleteAllJobs();
        Iterable<JobSimpleDto> allRemainingJobs = jobController.findAllJobs();
        int count = 0;
        for (JobSimpleDto foundJob : allRemainingJobs) {
            count++;
        }
        assertThat(count).isEqualTo(0);
    }
}
