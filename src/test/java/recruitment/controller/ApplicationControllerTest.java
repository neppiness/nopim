package recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.*;
import recruitment.dto.ApplicationResponse;
import recruitment.repository.ApplicationRepository;

import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

@Transactional
@SpringBootTest
public class ApplicationControllerTest {

    private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    ApplicationController applicationController;

    @Autowired
    CompanyController companyController;

    @Autowired
    JobController jobController;

    @Autowired
    UserController userController;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    UserRepository userRepository;

    Company wanted;

    Company naver;

    User user;

    Job jobForWanted;

    Job jobForNaver;

    @BeforeEach
    void jobControllerTestSetup() {
        applicationRepository.deleteAll();
        userRepository.deleteAll();
        jobRepository.deleteAll();
        companyRepository.deleteAll();
        userSetup();
        companySetup();
        jobSetup();
    }

    void userSetup() {
        user = userController.signUp("Kim-Jeonghyun", "1234").getBody();
    }

    void companySetup() {
        wanted = companyController.create("원티드", "한국", "서울").getBody();
        naver = companyController.create("네이버", "한국", "분당").getBody();
    }

    void jobSetup() {
        jobForWanted = jobController.create(
                wanted.getId(),
                "백엔드 주니어 개발자",
                500_000L,
                "Django",
                "원티드에서 백엔드 주니어 개발자를 채용합니다. 우대사항 - Django 사용 경험자."
        ).getBody();
        jobForNaver = jobController.create(
                naver.getId(),
                "프론트엔드 시니어 개발자",
                1_500_000L,
                "react",
                "네이버에서 프론트엔드 시니어 개발자를 채용합니다. 필수사항 - react 활용 개발 경력 5년 이상"
        ).getBody();
    }

    @Test
    @DisplayName("유저 ID로 지원내역을 검색하는 기능 테스트")
    void findApplicationsByUserIdTest() throws JsonProcessingException {
        Application applicationForWanted = Application.builder()
                .user(user)
                .job(jobForWanted)
                .build();
        applicationRepository.save(applicationForWanted);
        user.getApplications().add(applicationForWanted);

        Application applicationForNaver = Application.builder()
                .user(user)
                .job(jobForNaver)
                .build();
        applicationRepository.save(applicationForNaver);
        user.getApplications().add(applicationForNaver);

        List<ApplicationResponse> foundApplicationResponses = applicationController
                .getByUsername(user.getName())
                .getBody();
        assert foundApplicationResponses != null;

        String foundApplicationDtoInJson = objectWriter.writeValueAsString(foundApplicationResponses);
        System.out.println(foundApplicationDtoInJson);
        Assertions
                .assertThat(foundApplicationResponses.size())
                .isEqualTo(2);
    }

}
