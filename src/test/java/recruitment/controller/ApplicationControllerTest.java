package recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import recruitment.domain.*;
import recruitment.dto.ApplicationDetailResponse;
import recruitment.dto.ApplicationResponse;
import recruitment.dto.JobSimpleResponse;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.ApplicationRepository;

import java.util.Collection;
import recruitment.repository.CompanyRepository;
import recruitment.repository.UserRepository;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class ApplicationControllerTest {

    static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    ApplicationController applicationController;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    JobController jobController;

    @Autowired
    CompanyController companyController;

    @Autowired
    CompanyRepository companyRepository;

    Company wanted;

    Company naver;

    User user;

    JobSimpleResponse jobForWanted;

    JobSimpleResponse jobForNaver;

    void userSetup() {
        user = userController.signUp("Kim-Jeonghyun", "1234").getBody();
    }

    void companySetup() {
        wanted = companyController.create("원티드", "한국", "서울").getBody();
        naver = companyController.create("네이버", "한국", "분당").getBody();
    }

    void jobSetup() {
        jobForWanted = jobController.addJob(
                wanted.getId(),
                "백엔드 주니어 개발자",
                500_000L,
                "Django",
                "원티드에서 백엔드 주니어 개발자를 채용합니다. 우대사항 - Django 사용 경험자."
        ).getBody();
        jobForNaver = jobController.addJob(
                naver.getId(),
                "프론트엔드 시니어 개발자",
                1_500_000L,
                "react",
                "네이버에서 프론트엔드 시니어 개발자를 채용합니다. 필수사항 - react 활용 개발 경력 5년 이상"
        ).getBody();
    }

    @BeforeEach
    void jobControllerTestSetup() {
        applicationRepository.deleteAll();
        userRepository.deleteAll();
        jobController.deleteAllJobs();
        companyRepository.deleteAll();
        userSetup();
        companySetup();
        jobSetup();
    }

    @Test
    @DisplayName("유저 ID와 채용공고 ID로 지원내역을 검색하는 기능 테스트")
    void findApplicationByUserIdAndJobIdTest() throws JsonProcessingException {
        applicationController.addApplication(user.getId(), jobForNaver.getId());
        ApplicationResponse foundApplicationResponse = applicationController.findApplicationByUserIdAndJobId(
                user.getId(),
                jobForNaver.getId()
        ).getBody();

        String foundApplicationDtoInJson = ow.writeValueAsString(foundApplicationResponse);
        System.out.println(foundApplicationDtoInJson);

        ApplicationResponse expectedApplicationResponse = ApplicationResponse.builder()
                .userId(user.getId())
                .jobId(jobForNaver.getId())
                .build();

        ReflectionEquals re = new ReflectionEquals(foundApplicationResponse);
        Assertions.assertThat(re.matches(expectedApplicationResponse)).isTrue();
    }

    @Test
    @DisplayName("유저 ID와 채용공고 ID를 통한 지원내역 상세 조회 기능 테스트")
    void findDetailedApplicationByUserIdAndJobId() throws JsonProcessingException {
        applicationController.addApplication(user.getId(), jobForNaver.getId());
        ApplicationDetailResponse foundApplicationDto = applicationController.findDetailedApplicationByUserIdAndJobId(
                user.getId(),
                jobForNaver.getId()
        ).getBody();
        String foundApplicationDtoInJson = ow.writeValueAsString(foundApplicationDto);
        System.out.println(foundApplicationDtoInJson);
    }

    @Test
    @DisplayName("유저 ID로 지원내역을 검색하는 기능 테스트")
    void findApplicationsByUserIdTest() throws JsonProcessingException {
        applicationController.addApplication(user.getId(), jobForNaver.getId());
        applicationController.addApplication(user.getId(), jobForWanted.getId());

        Collection<ApplicationResponse> foundApplicationResponses = applicationController.findApplicationsByUserId(user.getId())
                .getBody();
        String foundApplicationDtoInJson = ow.writeValueAsString(foundApplicationResponses);
        System.out.println(foundApplicationDtoInJson);
        Assertions.assertThat(foundApplicationResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("유저 ID와 채용공고 ID를 통해 지원내역을 삭제하는 기능 테스트")
    void deleteApplicationByUserIdAndJobIdTest() {
        ApplicationResponse wantedApplicationResponse = applicationController.addApplication(user.getId(), jobForWanted.getId())
                .getBody();
        applicationController.deleteApplicationByUserIdAndJobId(user.getId(), jobForWanted.getId());

        Assertions.assertThatThrownBy(() -> {
            applicationController.findApplicationByUserIdAndJobId(user.getId(), jobForWanted.getId());
        }).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    @DisplayName("유저 ID에 해당하는 모든 지원내역을 삭제하는 기능 테스트")
    void deleteAllApplicationsByUserIdTest() throws JsonProcessingException {
        applicationController.addApplication(user.getId(), jobForNaver.getId());
        applicationController.addApplication(user.getId(), jobForWanted.getId());
        applicationController.deleteAllApplicationsByUserId(user.getId());

        Collection<ApplicationResponse> foundApplicationResponses = applicationController.findApplicationsByUserId(user.getId())
                .getBody();
        String foundApplicationDtoInJson = ow.writeValueAsString(foundApplicationResponses);
        System.out.println(foundApplicationDtoInJson);
        Assertions.assertThat(foundApplicationResponses.size()).isEqualTo(0);
    }

}
