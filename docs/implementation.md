# 구현 과정

## 0. 프로젝트 초기화
### spring initializr 활용
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

<br>

## 1. 채용공고 등록 기능
*

## 2. 채용공고 수정 기능
*

## 3. 채용공고 삭제 기능
*

## 4. 채용공고 목록 조회
### 4.1. 모든 채용공고 목록 조회
* 

### 4.2. 기준에 부합하는 채용공고 검색 기능
* 

## 5. 채용 상세 페이지 조회 기능
*

## 6. 사용자의 채용공고 지원 기능
*