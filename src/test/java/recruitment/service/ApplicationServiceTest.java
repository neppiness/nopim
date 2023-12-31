package recruitment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Application;
import recruitment.domain.Company;
import recruitment.domain.Job;
import recruitment.domain.User;
import recruitment.dto.ApplicationResponse;
import recruitment.dto.CompanyRequest;
import recruitment.dto.JobRequest;
import recruitment.dto.UserRequest;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

@Transactional
@SpringBootTest
class ApplicationServiceTest {

    private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

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

    private User user;

    private Job jobForWanted;

    private Job jobForNaver;

    @BeforeEach
    void jobServiceTestSetup() {
        applicationRepository.deleteAll();
        userRepository.deleteAll();
        jobRepository.deleteAll();
        companyRepository.deleteAll();
        userSetup();
        companySetup();
        jobSetup();
    }

    void userSetup() {
        UserRequest userRequest = UserRequest.builder()
                .name("Kim-Jeonghyun")
                .password("1234")
                .build();
        user = userService.signUp(userRequest);
    }

    void companySetup() {
        CompanyRequest companyRequestForWanted = CompanyRequest.builder()
                .name("원티드")
                .country("한국")
                .region("서울")
                .build();
        wanted = companyService.create(companyRequestForWanted);

        CompanyRequest companyRequestForNaver = CompanyRequest.builder()
                .name("네이버")
                .country("한국")
                .region("분당")
                .build();
        naver = companyService.create(companyRequestForNaver);
    }

    void jobSetup() {
        JobRequest jobRequestForWanted = JobRequest.builder()
                .companyId(wanted.getId())
                .position("백엔드 주니어 개발자")
                .bounty(500_000L)
                .stack("Django")
                .description("원티드에서 백엔드 주니어 개발자를 채용합니다. 우대사항 - Django 사용 경험자.")
                .build();
        jobForWanted = jobService.create(jobRequestForWanted);

        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("프론트엔드 시니어 개발자")
                .bounty(1_500_000L)
                .stack("react")
                .description("네이버에서 프론트엔드 시니어 개발자를 채용합니다. 필수사항 - react 활용 개발 경력 5년 이상")
                .build();
        jobForNaver = jobService.create(jobRequestForNaver);
    }

    @DisplayName("유저 ID로 지원내역을 검색하는 기능 테스트")
    @Test
    void findApplicationsByUserIdTest() throws JsonProcessingException {
        jobService.apply(jobForWanted.getId(), user.getName());
        jobService.apply(jobForNaver.getId(), user.getName());

        List<ApplicationResponse> foundApplicationResponses = applicationService.getByUsername(user.getName());
        assert foundApplicationResponses != null;

        String foundApplicationDtoInJson = objectWriter.writeValueAsString(foundApplicationResponses);
        System.out.println(foundApplicationDtoInJson);
        Assertions
                .assertThat(foundApplicationResponses.size())
                .isEqualTo(2);
    }

}