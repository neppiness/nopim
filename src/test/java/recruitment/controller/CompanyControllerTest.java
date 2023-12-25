package recruitment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Company;
import recruitment.repository.ApplicationRepository;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class CompanyControllerTest {

    final static String senvexName = "센벡스";

    final static String senvexCountry = "한국";

    final static String senvexRegion = "서울 당산";

    @Autowired
    CompanyController companyController;

    @Autowired
    JobController jobController;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    UserController userController;

    @BeforeEach
    void companyTestSetup() {
        applicationRepository.deleteAll();
        userController.deleteAllUsers();
        jobController.deleteAllJobs();
        companyController.deleteAllCompanies();

        jobController.deleteAllJobs();
        companyController.deleteAllCompanies();
        companyController.addCompany("원티드", "한국", "서울");
        companyController.addCompany("네이버", "한국", "분당");
    }

    @Test
    @DisplayName("회사 추가 및 회사 ID로 회사 검색 기능 테스트")
    void addCompanyAndFindCompanyByIdTest() {
        Company company = companyController.addCompany(senvexName, senvexCountry, senvexRegion).getBody();
        long companyId = company.getId();
        Company foundCompany = companyController.findCompanyById(companyId).getBody();
        assertThat(company).isEqualTo(foundCompany);
    }

    @Test
    @DisplayName("등록된 모든 회사 조회 기능 테스트")
    void getAllCompaniesTest() {
        AtomicInteger count = new AtomicInteger();
        Iterable<Company> allCompanies = companyController.getAllCompanies().getBody();
        allCompanies.forEach(company -> {
            StringBuilder sb = new StringBuilder();
            count.getAndIncrement();
            sb.append("company.getId()      = ").append(company.getId()).append('\n');
            sb.append("company.getName()    = ").append(company.getName()).append('\n');
            sb.append("company.getCountry() = ").append(company.getCountry()).append('\n');
            sb.append("company.getRegion()  = ").append(company.getRegion()).append('\n');
            System.out.println(sb);
        });
        assertThat(count.intValue()).isEqualTo(2);
    }

    @Test
    @DisplayName("회사 ID로 객체 삭제 기능 테스트")
    void deleteByCompanyIdTest() {
        Company company = companyController.addCompany(senvexName, senvexCountry, senvexRegion).getBody();
        long id = company.getId();
        companyController.deleteCompanyById(id);
        assertThatThrownBy(() -> {
            companyController.findCompanyById(id);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("등록된 모든 회사 삭제 기능 테스트")
    void deleteAllCompaniesTest() {
        System.out.println(companyController.deleteAllCompanies());
        AtomicInteger count = new AtomicInteger();
        Iterable<Company> allCompanies = companyController.getAllCompanies().getBody();
        allCompanies.forEach(company -> count.getAndIncrement());
        assertThat(count.intValue()).isEqualTo(0);
    }

}
