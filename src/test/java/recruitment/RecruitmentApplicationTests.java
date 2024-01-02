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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:data/init.sql")
@Transactional
@SpringBootTest
class RecruitmentApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void testSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @DisplayName(value = "컨텍스트 로드 테스트")
    @Test
    void contextLoads() {
    }

    @DisplayName("1. 채용공고를 등록합니다.")
    @Test
    void functionalRequirementsTest1() throws Exception {
        Long wantedLabId = 1L;
        MvcResult mvcResult = mockMvc.perform(
                post("/jobs")
                        .param("company-id", String.valueOf(wantedLabId))
                        .param("position", "백엔드 주니어 개발자")
                        .param("bounty", "1000000")
                        .param("stack", "Python")
                        .param("description", "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject json = new JSONObject(rawJsonString);
        System.out.println(json.toString(2));
    }

    @DisplayName("2. 채용공고를 수정합니다.")
    @Test
    void functionalRequirementsTest2() throws Exception {
        String idOfJobForWantedAsString = "1";
        String keyword = "원티드코리아";
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                get("/jobs/search")
                        .param("keyword", keyword)
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONArray jsonArray = new JSONArray(rawJsonString);
        System.out.println("수정 전");
        System.out.println(jsonArray.toString(2));
        System.out.println();

        mvcResult = mockMvc.perform(
                put("/jobs/" + idOfJobForWantedAsString)
                        .param("bounty", "1000000")
        ).andReturn();
        rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject jsonAfter1stModify = new JSONObject(rawJsonString);
        System.out.println("1차 수정 후: 채용보상금 변경");
        System.out.println(jsonAfter1stModify.toString(2));
        System.out.println();

        mvcResult = mockMvc.perform(
                put("/jobs/" + idOfJobForWantedAsString)
                        .param("stack", "react")
        ).andReturn();
        rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject jsonAfter2ndModify = new JSONObject(rawJsonString);
        System.out.println("2차 수정 후: 사용기술 변경");
        System.out.println(jsonAfter2ndModify.toString(2));
    }

    @DisplayName("3. 채용공고를 삭제합니다.")
    @Test
    void functionalRequirementsTest3() throws Exception {
        String idOfJobForWantedAsString = "1";
        String keyword = "원티드코리아";
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                get("/jobs/search")
                        .param("keyword", keyword)
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONArray jsonArray = new JSONArray(rawJsonString);
        System.out.println("삭제 전");
        System.out.println(jsonArray.toString(2));
        System.out.println();

        mvcResult = mockMvc.perform(
                delete("/jobs/" + idOfJobForWantedAsString)
        ).andReturn();
        System.out.println("삭제 실행");
        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println();

        mvcResult = mockMvc.perform(
                get("/jobs/search")
                        .param("keyword", keyword)
        ).andReturn();
        rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        jsonArray = new JSONArray(rawJsonString);
        System.out.println("삭제 후");
        System.out.println(jsonArray.toString(2));
    }

    @DisplayName("4-1. 사용자는 채용공고 목록을 아래와 같이 확인할 수 있습니다.")
    @Test
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

    @DisplayName("4-2. 채용공고 검색 기능 구현")
    @Test
    void functionalRequirementsTest4_2() throws Exception {
        String idOfWantedLabAsString = "1";
        System.out.println("키워드-원티드로 검색");

        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                post("/jobs")
                        .param("company-id", idOfWantedLabAsString)
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

    @DisplayName("5. 채용 상세 페이지를 가져옵니다.")
    @Test
    void functionalRequirementsTest5() throws Exception {
        String idOfJobForWanted = "1";
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                get("/jobs/detail/" + idOfJobForWanted)
        ).andReturn();
        String rawJsonArrayString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject jsonObject = new JSONObject(rawJsonArrayString);
        System.out.println(jsonObject.toString(2));
    }

    @DisplayName("6. 사용자는 채용공고에 지원합니다.")
    @Test
    void functionalRequirementsTest6() throws Exception {
        String idOfJobForWanted = "1";
        String username = "0414kjh";
        MvcResult mvcResult;
        mvcResult = mockMvc.perform(
                post("/jobs/apply/" + idOfJobForWanted)
                        .param("name", username)
        ).andReturn();
        String rawJsonString = mvcResult.getResponse().getContentAsString(Charset.defaultCharset());
        JSONObject jsonObject = new JSONObject(rawJsonString);
        System.out.println(jsonObject.toString(2));
    }

}
