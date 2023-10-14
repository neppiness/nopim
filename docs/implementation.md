# 구현 과정

## 1. 프로젝트 초기화
### 1-1. spring initializr 활용
* 프로젝트 설정
    - Project: Gradle - Groovy
    - Spring boot: 3.1.4
    - Java: 17
    - Packaging: Jar
* 의존성
    - Spring Web: 톰캣 등, 웹 서버 개발에 활용되는 기능
    - Spring Data JPA: JPA 활용을 위한 기능
    - MySQL Driver: MySQL 연결을 위한 드라이버
    - Lombok: getter, setter와 같은 보일러플레이트 코드를 간단히 작성
* MySQL 8.0과 Workbench를 활용해 recruitment 스키마 생성
* `application.properties` 설정을 통해 서버와 DB를 연결
    - DB_DATABASE_NAME, DB_USER, DB_PASSWORD는 git을 통해 관리되지 않도록 env.properties에 외부화

```
spring.config.import=env.properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/${DB_DATABASE_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

* [참고자료 | Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### 1-2. IDE 설정
* Lombok 사용을 위해 Annotation Processing 옵션을 활성화함

<br>

## 2. 도메인 설계
* 기능 요구사항에 명시된 필요 모델에 따라 다음 클래스를 생성
    - 회사, 사용자, 채용공고, 지원내역
* 응답 시 활용해야 하는 별도 객체는 DTO 클래스로 구현
    - 채용공고 상세내용: JobDto
    - 채용공고 내용: JobSimpleDto
* 설계 내용은 [docs/design.md](./design.md) 파일을 참고

<br>

## 3. `@ManyToOne`, `@OneToMany` 관계 구현
* 회사(Company) 객체를 가져와서 내부 정보를 필요할 때 출력하도록 설정
    - 이를 위해 채용공고(Job) 필드에 회사(Company) 정보를 가지고 있도록 함
* 또한, 회사(Company) 객체에는 회사가 올린 채용공고를 기록함
* 위 두 가지는 `@ManyToOne`, `@OneToMany`를 활용하여 구현할 수 있음
    - 한 회사는 여러 채용공고를 받아야 함.
        + `@OneToMany` 대응관계가 활용됨
        + 여러 채용공고가 한 회사의 `Set<Job> jobs`에 연결됨
    - 여러 채용공고가 그 채용공고를 올린 하나의 회사 정보로 전달됨
        + `@ManyToOne` 대응관계를 활용
        + `@JoinColumn`에서 열 이름은 'job_company'로 설정
        + 이는 회사(company) 테이블의 'company_id' 열을 참조하여 대응됨
* 구현은 채용공고(Job) 클래스와 회사(Company) 클래스를 참조

<br>

## 4. 트러블 슈팅
* 지금까지 설정된 상태로는 아래와 같은 오류가 발생함.

```text
Unable to determine Dialect without JDBC metadata
(please set 'javax.persistence.jdbc.url', 'hibernate.connection.url', or 'hibernate.dialect')
```

* 이를 해결하기 위해서 [다음 블로그](https://velog.io/@gloom/Spring-데이터베이스-연동-시-Access-to-DialectResolutionInfo-cannot-be-null-when-hibernate.dialect-not-set-오류)를 참조.
    - `application.properties` 파일에 아래와 같은 속성 값을 추가함.

```properties
spring.jpa.database=mysql
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

<br>

## 5. 조건에 따른 채용공고 검색 기능 구현 - 쿼리 처리
* 채용공고에 대한 search 기능을 다음과 같이 작성하고자 함
    - 주어진 position이나 stack에 대해서 쿼리를 수행
* 이를 위해 `@Query` 어노테이션을 인터페이스에 적용하고, 이를 활용하는 방법을 학습함
    - 참고자료: [Spring Data JPA @Query](https://www.baeldung.com/spring-data-jpa-query)
* 아래 코드는 `CrudRepository`를 확장한 `JobRepository`에서 쿼리를 수행하는 메소드를 정의한 코드임

```java
// import statements

public interface JobRepository extends CrudRepository<Job, Long> {

    @Query(value = "SELECT * FROM job j WHERE j.position = :position", nativeQuery = true)
    Collection<Job> findJobsByPosition(
            @Param("position") String position
    );

    @Query(value = "SELECT * FROM job j WHERE j.stack = :stack", nativeQuery = true)
    Collection<Job> findJobsByStack(
            @Param("stack") String stack
    );

    @Query(
            value = "SELECT j.id, j.company_id, j.position, j.bounty, j.stack, j.description, c.name " +
                    "FROM job AS j JOIN company AS c ON j.company_id = c.id " +
                    "WHERE c.name = :companyName",
            nativeQuery = true
    )
    Collection<Job> findJobsByCompanyName(
            @Param("companyName") String companyName
    );
}
```

* 이를 통해서 `Collection<Job>`을 반환받음.
* `@RequestParam`은 기본적으로 `required = true`로 설정함
    - 이를 변경해야만 요청 파라미터로 값이 없어도 문제가 발생하지 않음.
* 원래는 원시형(primitive type) 변수인 `long companyId`를 활용했음
    - 이 경우, `required = false`여서 값이 할당되지 않으면 `companyId = null`이 되어야 함.
    - 그러나, 원시형 변수는 null 값을 가질 수 없어 에러가 발생
    - 따라서 Wrapper 타입인 `Long`으로 설정해줘야 함. 이를 통해 구현한 컨트롤러 로직은 아래와 같음.

```java
@Controller
@RequestMapping(path="/job")
public class JobController {

    // fields and methods
    // ...
    
    @GetMapping(path="/search")
    public @ResponseBody Iterable<JobSimpleDto> searchJob(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String stack
    ) {
        Collection<Job> foundJobs = jobRepository.findJobsByPosition(position);
        List<JobSimpleDto> jobs = new ArrayList<>();
        foundJobs.forEach(job -> jobs.add(job.convertToJobSimpleDto()));
        return jobs;
    }
}
```

* 이는 `position` 변수 값을 통해 탐색하는 `searchJob()` 메소드의 구현임
    - 그러나, 여러 요인에 대해서 쿼리를 수행해야 하는 경우를 충분히 대변할 수 없다고 판단함.
    - 이에 따라 모든 채용공고를 조회한 뒤, 제시된 조건에 대해서 필터링을 수행하는 식으로 구현을 변경하고자 함.

<br>

## 6. 조건에 따른 채용공고 검색 기능 구현 - 필터링

```java
// import statements and annotations
// ...
public class JobController {
    
    // Other methods
    // ...
    @GetMapping(path="/search")
    public @ResponseBody Iterable<JobSimpleDto> searchJob(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String stack
    ) {
        Iterable<Job> allJobs = jobRepository.findAll();
        List<JobSimpleDto> foundJobs = new ArrayList<>();

        allJobs.iterator().forEachRemaining(job -> {
            if (jobId != null && !Objects.equals(job.getId(), jobId)) return;
            if (companyId != null && !Objects.equals(job.getCompanyId(), companyId)) return;
            if (companyName != null && !job.getCompanyName().equals(companyName)) return;
            if (position != null && !job.getPosition().equals(position)) return;
            if (stack != null && !job.getStack().equals(stack)) return;
            foundJobs.add(job.convertToJobSimpleDto());
        });
        return foundJobs;
    }
}
```
* 위같은 방식으로 전달받은 인자가 `null`이 아닌 경우에 해당 인자와 속성 값이 같은지 확인
    - 모든 조건 만족 시 이터러블(Iterable)의 원소인 잡(job)을 JobSimpleDto 형태로 변환 후 반환할 `List<JobSimpleDto> foundJobs`에 추가.

* `@RequestParam(required = false)`
    - 기본값은 `required = true`임. 이는 요청 파라미터가 없는 경우 메소드가 예외를 발생시키도록 구현됨.
    - `required = false`로 두면 요청 파라미터 없이도 메소드가 동작할 수 있게 설정됨.
    - 참고자료: [Spring @RequestParam Annotation](https://www.baeldung.com/spring-request-param)