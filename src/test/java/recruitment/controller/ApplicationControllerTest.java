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
import recruitment.exception.ResourceNotFound;
import recruitment.repository.ApplicationRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

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
    UserController userController;

    @Autowired
    JobController jobController;

    @Autowired
    CompanyController companyController;

    Company wanted;

    Company naver;

    User user;

    JobSimpleDto jobForWanted;

    JobSimpleDto jobForNaver;

    void userSetup() {
        user = userController.addUser("Kim-Jeonghyun").getBody();
    }

    void companySetup() {
        wanted = companyController.addCompany("원티드", "한국", "서울").getBody();
        naver = companyController.addCompany("네이버", "한국", "분당").getBody();
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
        userController.deleteAllUsers();
        jobController.deleteAllJobs();
        companyController.deleteAllCompanies();
        userSetup();
        companySetup();
        jobSetup();
    }

    @Test
    @DisplayName("유저 ID와 채용공고 ID로 지원내역을 검색하는 기능 테스트")
    void findApplicationByUserIdAndJobIdTest() throws JsonProcessingException {
        applicationController.addApplication(user.getId(), jobForNaver.getId());
        ApplicationDto foundApplicationDto = applicationController.findApplicationByUserIdAndJobId(
                user.getId(),
                jobForNaver.getId()
        ).getBody();

        String foundApplicationDtoInJson = ow.writeValueAsString(foundApplicationDto);
        System.out.println(foundApplicationDtoInJson);

        ApplicationDto expectedApplicationDto = new ApplicationDto();
        expectedApplicationDto.setUserId(user.getId());
        expectedApplicationDto.setJobId(jobForNaver.getId());

        ReflectionEquals re = new ReflectionEquals(foundApplicationDto);
        Assertions.assertThat(re.matches(expectedApplicationDto)).isTrue();
    }

    @Test
    @DisplayName("유저 ID와 채용공고 ID를 통한 지원내역 상세 조회 기능 테스트")
    void findDetailedApplicationByUserIdAndJobId() throws JsonProcessingException {
        applicationController.addApplication(user.getId(), jobForNaver.getId());
        ApplicationDetailedDto foundApplicationDto = applicationController.findDetailedApplicationByUserIdAndJobId(
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

        Collection<ApplicationDto> foundApplicationDtos = applicationController.findApplicationsByUserId(user.getId())
                .getBody();
        String foundApplicationDtoInJson = ow.writeValueAsString(foundApplicationDtos);
        System.out.println(foundApplicationDtoInJson);
        Assertions.assertThat(foundApplicationDtos.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("유저 ID와 채용공고 ID를 통해 지원내역을 삭제하는 기능 테스트")
    void deleteApplicationByUserIdAndJobIdTest() {
        ApplicationDto wantedApplicationDto = applicationController.addApplication(user.getId(), jobForWanted.getId())
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

        Collection<ApplicationDto> foundApplicationDtos = applicationController.findApplicationsByUserId(user.getId())
                .getBody();
        String foundApplicationDtoInJson = ow.writeValueAsString(foundApplicationDtos);
        System.out.println(foundApplicationDtoInJson);
        Assertions.assertThat(foundApplicationDtos.size()).isEqualTo(0);
    }

}
