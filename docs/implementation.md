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