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