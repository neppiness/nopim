package recruitment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import recruitment.controller.CompanyController;
import recruitment.controller.JobController;
import recruitment.controller.UserController;
import recruitment.domain.Company;
import recruitment.domain.Job;
import recruitment.domain.User;
import recruitment.dto.CompanyRequest;
import recruitment.dto.JobRequest;
import recruitment.dto.UserRequest;
import recruitment.repository.ApplicationRepository;
import recruitment.repository.CompanyRepository;
import recruitment.repository.JobRepository;
import recruitment.repository.UserRepository;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class RecruitmentApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private JobController jobController;

    @Autowired
    private UserController userController;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private User user;

    private Company wantedLab;

    private Company wanted;

    private Company naver;

    private Company kakao;

    private Job jobOfWantedLab;

    private Job jobOfWanted;

    private Job jobOfNaver;

    private Job jobOfKakao;

    @BeforeEach
    void testSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        applicationRepository.deleteAll();
        userRepository.deleteAll();
        jobRepository.deleteAll();
        companyRepository.deleteAll();

        UserRequest userRequest = UserRequest.builder()
                .name("김정현")
                .password("1234")
                .build();
        user = userController.signUp(userRequest).getBody();

        CompanyRequest companyRequestForWantedLab = CompanyRequest.builder()
                .name("원티드랩")
                .region("서울")
                .country("한국")
                .build();
        wantedLab = companyController.create(companyRequestForWantedLab).getBody();


        CompanyRequest companyRequestForWantedKorea = CompanyRequest.builder()
                .name("원티드코리아")
                .region("부산")
                .country("한국")
                .build();
        wanted = companyController.create(companyRequestForWantedKorea).getBody();

        CompanyRequest companyRequestForNaver = CompanyRequest.builder()
                .name("네이버")
                .region("판교")
                .country("한국")
                .build();
        naver = companyController.create(companyRequestForNaver).getBody();

        CompanyRequest companyRequestForKakao = CompanyRequest.builder()
                .name("카카오")
                .region("판교")
                .country("한국")
                .build();
        kakao = companyController.create(companyRequestForKakao).getBody();

        JobRequest jobRequestForNaver = JobRequest.builder()
                .companyId(naver.getId())
                .position("Django 백엔드 개발자")
                .bounty(1_000_000L)
                .stack("Django")
                .description("네이버에서 백엔드 개발자를 채용합니다.")
                .build();
        jobOfNaver = jobController.create(jobRequestForNaver).getBody();

        JobRequest jobRequestForWanted = JobRequest.builder()
                .companyId(wanted.getId())
                .position("프론트엔드 개발자")
                .bounty(500_000L)
                .stack("javascript")
                .description("원티드코리아에서 프론트엔드 개발자를 채용합니다. 자격요건은..")
                .build();
        jobOfWanted = jobController.create(jobRequestForWanted).getBody();

        JobRequest jobRequestForKakao = JobRequest.builder()
                .companyId(kakao.getId())
                .position("Django 백엔드 개발자")
                .bounty(500_000L)
                .stack("python")
                .description("카카오에서 Django 백엔드 개발자를 채용합니다. 자격요건은..")
                .build();
        jobOfKakao = jobController.create(jobRequestForKakao).getBody();
    }

    @DisplayName(value = "컨텍스트 로드 테스트")
    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("1. 채용공고를 등록합니다.")
    void functionalRequirementsTest1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/jobs")
                        .param("company-id", String.valueOf(wantedLab.getId()))
                        .param("position", "백엔드 주니어 개발자")
                        .param("bounty", "1000000")
                        .param("stack", "Python")
                        .param("description", "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject json = new JSONObject(rawJsonString);
        System.out.println(json.toString(2));
    }

    @Test
    @DisplayName("2. 채용공고를 수정합니다.")
    void functionalRequirementsTest2() throws Exception {
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                get("/jobs/search")
                        .param("keyword", String.valueOf(jobOfWanted.getId()))
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONArray jsonArray = new JSONArray(rawJsonString);
        System.out.println("수정 전");
        System.out.println(jsonArray.toString(2));
        System.out.println();

        mvcResult = mockMvc.perform(
                put("/jobs/" + jobOfWanted.getId())
                        .param("bounty", "1000000")
        ).andReturn();
        rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject json = new JSONObject(rawJsonString);
        System.out.println("1차 수정 후: 채용보상금 변경");
        System.out.println(json.toString(2));
        System.out.println();

        mvcResult = mockMvc.perform(
                put("/jobs/" + jobOfWanted.getId())
                        .param("stack", "react")
        ).andReturn();
        rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        json = new JSONObject(rawJsonString);
        System.out.println("2차 수정 후: 사용기술 변경");
        System.out.println(json.toString(2));
    }

    @Test
    @DisplayName("3. 채용공고를 삭제합니다.")
    void functionalRequirementsTest3() throws Exception {
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                get("/jobs/search")
                        .param("keyword", String.valueOf(jobOfWanted.getId()))
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONArray jsonArray = new JSONArray(rawJsonString);
        System.out.println("삭제 전");
        System.out.println(jsonArray.toString(2));
        System.out.println();

        mvcResult = mockMvc.perform(
                delete("/jobs/" + jobOfWanted.getId())
        ).andReturn();
        System.out.println("삭제 실행");
        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println();

        mvcResult = mockMvc.perform(
                get("/jobs/search")
                        .param("keyword", String.valueOf(jobOfWanted.getId()))
        ).andReturn();
        rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        jsonArray = new JSONArray(rawJsonString);
        System.out.println("삭제 후");
        System.out.println(jsonArray.toString(2));
    }

    @Test
    @DisplayName("4-1. 사용자는 채용공고 목록을 아래와 같이 확인할 수 있습니다.")
    void functionalRequirementsTest4_1() throws Exception {
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                get("/jobs")
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONArray jsonArray = new JSONArray(rawJsonString);
        System.out.println("모든 채용공고 목록 조회");
        System.out.println(jsonArray.toString(2));
    }

    @Test
    @DisplayName("4-2. 채용공고 검색 기능 구현")
    void functionalRequirementsTest4_2() throws Exception {
        MvcResult mvcResult;
        System.out.println("키워드-원티드로 검색");
        mvcResult = mockMvc.perform(
                post("/jobs")
                        .param("company-id", String.valueOf(wantedLab.getId()))
                        .param("position", "백엔드 주니어 개발자")
                        .param("bounty", "1000000")
                        .param("stack", "Python")
                        .param("description", "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
        ).andReturn();

        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject jsonObject = new JSONObject(rawJsonString);
        System.out.println("원티드랩 공고 추가");
        System.out.println(jsonObject.toString(2));
        System.out.println();

        mvcResult = mockMvc.perform(
                get("/jobs/search")
                        .param("keyword", "원티드")
        ).andReturn();
        String rawJsonArrayString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONArray jsonArray = new JSONArray(rawJsonArrayString);
        System.out.println("키워드-원티드로 검색");
        System.out.println(jsonArray.toString(2));
    }

    @Test
    @DisplayName("5. 채용 상세 페이지를 가져옵니다.")
    void functionalRequirementsTest5() throws Exception {
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                get("/jobs/detail/" + jobOfWanted.getId())
        ).andReturn();
        String rawJsonArrayString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject jsonObject = new JSONObject(rawJsonArrayString);
        System.out.println(jsonObject.toString(2));
    }

    @Test
    @DisplayName("6. 사용자는 채용공고에 지원합니다.")
    void functionalRequirementsTest6() throws Exception {
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                post("/jobs/apply/" + jobOfWanted.getId())
                        .param("name", "김정현")
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject jsonObject = new JSONObject(rawJsonString);
        System.out.println(jsonObject.toString(2));
    }

}
