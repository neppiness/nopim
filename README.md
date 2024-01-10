# 높임(nopim) | 채용 플랫폼 서비스

## 개요
* 본 서비스는 채용 플랫폼 서비스입니다.
* 사용자는 채용 공고를 생성하거나 지원할 수 있습니다.
* 원티드에서 진행한 '백엔드 프리온보딩 인턴십 10월' 선발과제입니다.

### 주요 기능
1. **일반 회원**
    - 사용자는 회원가입 및 로그인을 할 수 있습니다.
    - 사용자는 회사를 검색할 수 있습니다.
    - 사용자는 회사 상세 정보를 조회할 수 있습니다.
    - 사용자는 채용공고를 검색할 수 있습니다.
    - 사용자는 채용공고 상세 정보를 조회할 수 있습니다.
    - 사용자는 채용공고에 지원할 수 있습니다.

2. **기업 회원(매니저)**
    - 매니저는 본인 회사의 채용공고를 등록, 수정 및 삭제할 수 있습니다.

___

## 목차
* [개요](#개요)
* [기술 스택](#기술-스택)
* [패키지 구조](#패키지-구조)
* [프로젝트 주제](#프로젝트-주제)
* [기능 목록](#기능-목록)
* [DB 설계](#DB-설계)
* [학습 내용](#학습-내용)
* [기타 링크](#기타-링크)
* [저자](#저자)
* [이전 문서](#이전-문서)

___

## 기술 스택
![JAVA](https://img.shields.io/badge/-java-007396?style=for-the-badge)
![SPRING BOOT](https://img.shields.io/badge/-spring_boot-6DB33F?style=for-the-badge&logoColor=ffffff)
![SPRING DATA JPA](https://img.shields.io/badge/-spring_data_jpa-6DB33F?style=for-the-badge&logoColor=ffffff)
![QueryDSL](https://img.shields.io/badge/-query_dsl-028bcf?style=for-the-badge&logoColor=white)
![MySQL](https://img.shields.io/badge/-my_sql-004088?style=for-the-badge&logoColor=white)
___

## 패키지 구조

<details>
<summary> 클릭하여 펼치기 </summary>
<br>

```
root
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─neppiness
    │  │          └─nopim
    │  │              ├─config
    │  │              ├─controller
    │  │              ├─domain
    │  │              ├─dto
    │  │              ├─exception
    │  │              ├─repository
    │  │              │  └─impl
    │  │              ├─resolver
    │  │              ├─service
    │  │              └─util
    │  └─resources
    │      ├─data
    │      ├─static
    │      └─templates
    └─test
        └─java
            └─com
                └─neppiness
                    └─nopim
                        ├─domain
                        ├─repository
                        │  └─impl
                        ├─service
                        └─util
```

</details>

___

## 프로젝트 주제
### 서버 구현
* 스프링 프레임워크를 활용한 REST API 개발
    - REST API 가이드에 맞춘 URI 설계

* JWT 로그인
    - jjwt 라이브러리 활용

* 관계형 데이터베이스 설계
    - QueryDSL을 활용한 쿼리

* ORM 활용
    - JPA Persistence 활용한 엔티티 지정

* 테스트 코드 작성
    - JUnit5를 활용한 단위 테스트
    - Postman을 활용한 API 호출 테스트


___

## 기능 목록
### 1. 사용자 관련 기능
#### 1-1. 회원가입
<details> <summary>클릭하여 상세 보기</summary>

* 누구든 사용자로 계정을 생성할 수 있다.
* HTTP 요청: `POST: /users`
* 요청 인자
    - name: 사용자 이름
    - password: 사용자 비밀번호
* 요청 예시: `POST: /users?name=neppiness&password=Neppiness12!`
* 성공 응답
    - `201 Created`: 생성한 계정 정보
    - 응답 예시

    ```json
    {
        "name": "neppiness",
        "authority": "MEMBER"
    }
    ```

* 실패 응답
    - `400 Bad Request`: 동일한 계정이 있는 경우
</details>


#### 1-2. 로그인
<details> <summary>클릭하여 상세 보기</summary>

* 누구든 사용자로 계정을 생성할 수 있다.
* HTTP 요청: `GET: /users`
* 요청 인자
    - name: 사용자 이름
    - password: 사용자 비밀번호
* 요청 예시: `GET: /users?name=neppiness&password=Neppiness12!`
* 성공 응답
    - `200 OK`: 사용자 정보가 담긴 토큰
    - 응답 예시

    ```json
    {
        "token": "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9
                  .eyJpYXQiOjE3MDQyODkzNjIsImV4cCI6MTcwNDI5MTE2Miwic3ViIjoibmVwcGluZXNzIiwiYXV0aG9yaXR5IjoiTUVNQkVSIn0
                  .6GMkA13dHs_kSM7-xI2LBe5Ib5ERMX0IPoGdvwmD6Jo"
    }
    ```

* 실패 응답
    - `404 Not Found`: 정보가 일치하는 계정이 없는 경우
</details>


#### 1-3. 사용자 권한 상승
<details> <summary>클릭하여 상세 보기</summary>

* 관리자는 사용자의 권한을 상승시킬 수 있다
    - MEMBER -> MANAGER
* HTTP 요청: `POST: /users/promote`
* 요청 예시: `/users/promote?id=1`
* 성공 응답
    - `200 OK`: 권한이 상승된 사용자 정보가 담긴 토큰
    - 응답 예시

    ```json
    {
        "name": "neppiness",
        "authority": "MANAGER"
    }
    ```

* 실패 응답
    - `401 Unauthorized` : 인증이 안 된 사용자의 경우
    - `403 Forbidden` : 권한이 없는 경우
    - `404 Not Found` : 사용자를 찾지 못한 경우

</details>

<br>

### 2. 회사 관련 기능
#### 2-1. 회사 등록
<details> <summary>클릭하여 상세 보기</summary>

* 매니저, 또는 관리자는 회사를 등록할 수 있다.
* HTTP 요청: `POST: /companies`
* 요청 인자
    - name: 회사 이름
    - country: 소속 국가
    - region: 회사가 위치한 지역
* 요청 예시: `/companies?name=원티드코리아&region=부산&country=대한민국`
* 성공 응답
    - `201 Created`: 등록된 회사 정보
    - 응답 예시

    ```json
    {
        "id": 1,
        "name": "원티드코리아",
        "region": "부산",
        "country": "대한민국"
    }
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
    - `403 Forbidden`: 권한이 없는 경우
</details>

#### 2-2. 회사 검색
<details> <summary>클릭하여 상세 보기</summary>

* 인증된 사용자는 회사를 검색할 수 있다.
* HTTP 요청: `GET: /companies/search`
* 요청 인자
    - name: 회사 이름
    - country: 소속 국가
    - region: 회사가 위치한 지역
* 요청 예시
    - `/companies/search?name=원티드코리아&region=부산&country=대한민국`
* 성공 응답
    - `200 OK`: 해당하는 회사 목록
    - 응답 예시

    ```json
    [
        {
            "id": 1,
            "name": "원티드코리아",
            "region": "부산",
            "country": "대한민국"
        }
    ]
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
</details>

#### 2-3. 회사 상세 정보 조회
<details> <summary>클릭하여 상세 보기</summary>

* 인증된 사용자는 회사 상세 정보를 조회할 수 있다.
* HTTP 요청: `GET: /companies/detail/{id}`
* 경로 인자
    - id: 회사 고유 번호
* 요청 예시: `/companies/detail/1`
* 성공 응답
    - `200 OK`: 회사 상세 정보
    - 응답 예시

    ```json
    {
        "id": 1,
        "name": "원티드코리아",
        "region": "부산",
        "country": "대한민국",
        "jobs": [ 1, 2 ]
    }
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
</details>

<br>

### 3. 채용공고 관련 기능
#### 3.1 채용공고 등록
<details> <summary>클릭하여 상세 보기</summary>

* 매니저는 본인 회사의 채용공고를 등록할 수 있다.
* HTTP 요청: `POST: /jobs`
* 요청 인자
    - company-id: 회사 고유번호
    - position: 채용 포지션
    - bounty: 채용 보상금
    - stack: 기술 스택
    - description: 설명
* 요청 예시: `/jobs?company-id=1&position=프론트엔드 시니어 개발자&bounty=1500000&stack=react&description=원티드에서 프론트엔드 시니어 개발자를 채용합니다. 필수스택 리액트.`
* 성공 응답
    - `201 Created`: 등록된 공고 정보
    - 응답 예시

    ```json
    {
        "id": 1,
        "company": {
            "id": 1,
            "name": "원티드코리아",
            "region": "부산",
            "country": "대한민국"
        },
        "position": "프론트엔드 시니어 개발자",
        "bounty": 1500000,
        "stack": "react",
        "description": "원티드에서 시니어 프론트엔드 개발자를 채용합니다. 필수스택 리액트.",
        "status": "OPEN"
    }
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
    - `403 Forbidden`: 권한이 없는 경우
</details>

#### 3.2 채용공고 수정
<details> <summary>클릭하여 상세 보기</summary>

* 매니저는 본인 회사의 채용공고를 수정할 수 있다.
* HTTP 요청: `PUT: /jobs/{id}`
* 경로 인자
    - id: 채용공고 고유번호
* 요청 인자
    - company-id: 회사 고유번호
    - position: 채용 포지션
    - bounty: 채용 보상금
    - stack: 기술 스택
    - description: 설명
* 요청 예시: `/jobs/1?company-id=1&position=백엔드 주니어 개발자&bounty=1000000&stack=Django&description=원티드에서 주니어 백엔드 개발자를 채용합니다. 필수스택 파이썬.`
* 성공 응답
    - `200 OK`: 수정된 공고 정보
    - 응답 예시

    ```json
    {
        "id": 1,
        "company": {
            "id": 1,
            "name": "원티드코리아",
            "region": "부산",
            "country": "대한민국"
        },
        "position": "백엔드 주니어 개발자",
        "bounty": 1000000,
        "stack": "Django",
        "description": "원티드에서 주니어 백엔드 개발자를 채용합니다. 필수스택 파이썬.",
        "status": "OPEN"
    }
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
    - `403 Forbidden`: 권한이 없는 경우
    - `404 Not Found`: 고유번호로 채용공고를 찾을 수 없는 경우
</details>

#### 3.3 채용공고 마감
<details> <summary>클릭하여 상세 보기</summary>

* 매니저는 본인 회사의 채용공고를 마감상태로 변경할 수 있다.
* HTTP 요청: `POST: /jobs/close/{id}`
* 경로 인자
    - id: 채용공고 고유번호
* 요청 예시: `/jobs/close/1`
* 성공 응답
    - `200 OK`: 마감된 공고 정보
    - 응답 예시

    ```json
    {
        "id": 1,
        "company": {
            "id": 1,
            "name": "원티드코리아",
            "region": "부산",
            "country": "대한민국"
        },
        "position": "백엔드 주니어 개발자",
        "bounty": 1000000,
        "stack": "Django",
        "description": "원티드에서 주니어 백엔드 개발자를 채용합니다. 필수스택 파이썬.",
        "status": "CLOSE"
    }
    ```

* 실패 응답
    - `403 Forbidden`: 권한이 없는 경우
    - `404 Not Found`: 고유번호로 채용공고를 찾을 수 없는 경우

</details>

#### 3.4 채용공고 목록 조회
<details> <summary>클릭하여 상세 보기</summary>

* 인증된 사용자는 채용공고 전체 목록을 조회할 수 있다.
* HTTP 요청: `GET: /jobs`
* 성공 응답
    - `200 OK`: 현재 등록된 채용 공고 목록
    - 응답 예시

    ```json
    [
        {
            "id": 1,
            "companyName": "원티드코리아",
            "region": "부산",
            "country": "대한민국",
            "position": "백엔드 주니어 개발자",
            "bounty": 1000000,
            "stack": "Django",
            "status": "CLOSE"
        },
        {
            "id": 2,
            "companyName": "원티드코리아",
            "region": "부산",
            "country": "대한민국",
            "position": "백엔드 주니어 개발자",
            "bounty": 500000,
            "stack": "python",
            "status": "OPEN"
        }
    ]
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
</details>

#### 3.5 채용공고 검색
<details> <summary>클릭하여 상세 보기</summary>

* 인증된 사용자는 채용공고를 검색할 수 있다.
* HTTP 요청: `GET: /jobs/search`
* 요청 인자
    - keyword: 회사명, 지역, 국가, 포지션, 스택에 포함된 문자
* 요청 예시: `/jobs/search?keyword=원티드`
* 성공 응답
    - `200 OK`: 채용 공고 상세 내용
    - 응답 예시

    ```json
    [
        {
            "id": 1,
            "companyName": "원티드코리아",
            "region": "대한민국",
            "country": "부산",
            "position": "백엔드 주니어 개발자",
            "bounty": 1000000,
            "stack": "Django",
            "status": "CLOSE"
        },
        {
            "id": 2,
            "companyName": "원티드코리아",
            "region": "대한민국",
            "country": "부산",
            "position": "백엔드 주니어 개발자",
            "bounty": 500000,
            "stack": "python",
            "status": "OPEN"
        }
    ]
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
</details>

#### 3.6 채용공고 상세 정보 조회
<details> <summary>클릭하여 상세 보기</summary>

* 인증된 사용자는 채용공고 상세 내용을 조회할 수 있다.
* HTTP 요청: `GET: /jobs/detail/{id}`
* 경로 인자
    - id: 채용공고 고유번호
* 요청 예시: `/jobs/detail/1`
* 성공 응답
    - `200 OK`: 채용 공고 상세 내용
    - 응답 예시

    ```json
    {
        "id": 1,
        "companyName": "원티드코리아",
        "country": "대한민국",
        "region": "부산",
        "position": "백엔드 주니어 개발자",
        "bounty": 1000000,
        "stack": "Django",
        "description": "원티드에서 주니어 백엔드 개발자를 채용합니다. 필수스택 파이썬.",
        "otherJobIdsOfCompany": [ 2 ],
        "status": "CLOSE"
    }
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
    - `404 Not Found`: 해당하는 공고가 없는 경우
</details>

#### 3.7 채용공고 지원
<details> <summary>클릭하여 상세 보기</summary>

* 인증된 사용자는 채용 공고에 지원할 수 있다.
* HTTP 요청: `POST: /jobs/apply/{id}`
* 경로 인자
    - id: 채용공고 고유번호
* 요청 예시: `/jobs/apply/1`
* 성공 응답
    - `201 Created`: 지원 내역
    - 응답 예시

    ```json
    {
        "userId": 1,
        "jobId": 1
    }
    ```

* 실패 응답
    - `400 Bad Request`: 이미 지원한 공고의 경우
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
    - `404 Not Found`: 해당하는 공고가 없는 경우
</details>
<br>

### 4. 지원 관련 기능
#### 4.1 지원 목록 조회
<details> <summary>클릭하여 상세 보기</summary>

* 인증된 사용자는 자신의 지원 목록을 확인할 수 있다.
* HTTP 요청: `GET: /applications`
* 성공 응답
    - `200 OK`: 지원 목록
    - 응답 예시

    ```json
    [
        {
            "userId": 1,
            "jobId": 1
        },
        {
            "userId": 1,
            "jobId": 2
        }
    ]
    ```

* 실패 응답
    - `401 Unauthorized`: 인증이 안 된 사용자의 경우
</details>

___

## DB 설계
### 사용자(User) 테이블
<details>
<summary>클릭하여 상세보기</summary>
<br>

| Name      | Type    | NotNull | Default | Primary Key | Foreign Key | Description |
|-----------|---------|---------|---------|-------------|-------------|-------------|
| id        | BIGINT  | O       |         | O           |             | 고유번호        |
| name      | VARCHAR |         |         |             |             | 사용자 이름      |
| password  | VARCHAR |         |         |             |             | 비밀번호        |
| authority | ENUM    |         |         |             |             | 권한          |

</details>

### 회사(Company) 테이블
<details>
<summary>클릭하여 상세보기</summary>
<br>

| Name    | Type    | NotNull | Default | Primary Key | Foreign Key | Description |
|---------|---------|---------|---------|-------------|-------------|-------------|
| id      | BIGINT  | O       |         | O           |             | 고유번호        |
| name    | VARCHAR |         |         |             |             | 회사명         |
| region  | VARCHAR |         |         |             |             | 지역          |
| country | VARCHAR |         |         |             |             | 국가          |

</details>

### 채용공고(Job) 테이블
<details>
<summary>클릭하여 상세보기</summary>
<br>

| Name        | Type    | NotNull | Default | Primary Key | Foreign Key | Description |
|-------------|---------|---------|---------|-------------|-------------|-------------|
| id          | BIGINT  | O       |         | O           |             | 고유번호        |
| company_id  | BIGINT  |         |         |             | O           | 회사 고유번호     |
| position    | VARCHAR |         |         |             |             | 포지션         |
| bounty      | BIGINT  |         |         |             |             | 채용보상금       |
| stack       | VARCHAR |         |         |             |             | 스택          |
| description | VARCHAR |         |         |             |             | 설명          |
| status      | ENUM    |         |         |             |             | 상태          |

</details>

### 채용공고(Application) 테이블
<details>
<summary>클릭하여 상세보기</summary>
<br>

| Name       | Type    | NotNull | Default | Primary Key | Foreign Key | Description |
|------------|---------|---------|---------|-------------|-------------|-------------|
| id         | BIGINT  | O       |         | O           |             | 고유번호        |
| user_id    | BIGINT  |         |         |             | O           | 회사 고유번호     |
| company_id | BIGINT  |         |         |             | O           | 사용자 유번호     |

</details>

___

## 학습 내용
### DataJpaTest 트러블 슈팅
<details>
<summary>클릭하여 상세보기</summary>
<br>

* MySql을 활용해 DataJpaTest을 수행하려는 경우 에러가 발생
* 이는 `@DataJpaTest`가 자동으로 `EmbeddedDatabase`를 활용하기 때문.
* 따라서 `@AutoConfigureTestDatabase`를 활용해 해당 설정이 동작하지 않도록 막아야 함.
    - `replace`라는 값을 `Replace.NONE`으로 설정함.

```java
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class CompanyCustomRepositoryImplTest {
    // 테스트 클래스 코드
}
```

* 참고자료: [https://kangwoojin.github.io/programing/auto-configure-test-database/](https://kangwoojin.github.io/programing/auto-configure-test-database/)

</details>

### 연관관계 갱신이 안 되는 문제 해결
<details>
<summary>클릭하여 상세보기</summary>
<br>

* 채용공고(Job)을 생성할 때 회사(Company)를 기록해도 회사의 채용공고 목록이 갱신되지 않는 문제 발생.
    - ManyToOne: `Job` 객체의 `Company`형 변수 `Job.company` 
    - OneToMany: `User` 객체의 `List<Job>`형 변수 `jobs`
* 양방향 매핑 시에는 하나를 연관관계의 주인으로 정해야 한다.
    - 두 객체 연관관계 중 하나를 정해서 테이블의 외래 키를 관리해야 함.
    - 그때 외래 키를 관리하는 객체가 연관관계의 주인이 됨.
    - ManyToOne-OneToMany 관계에선 ManyToOne이 외래 키를 가지며, 연관관계의 주인이 된다.
* 양방향 관계는 양쪽으로 JOIN을 할 수 있음.
    - 관계는 양쪽에서 설정해주라고 권함.
    - `Job` 객체를 생성할 때 `Company` 객체의 `List<Job> jobs`에도 `Job` 객체 정보를 저장하도록 수정.
    - JobService의 create 메소드 예시
  
```java
@Transactional
public Job create(JobRequest jobRequest) {
    Long companyId = jobRequest.getCompanyId();
    Optional<Company> mayBeFoundCompany = companyRepository.findById(companyId);
    if (mayBeFoundCompany.isEmpty()) {
        throw new ResourceNotFoundException(ResourceNotFoundException.COMPANY_NOT_FOUND);
    }
    Company foundCompany = mayBeFoundCompany.get();
    Job createdJob = Job.builder()
            .company(foundCompany)
            .position(jobRequest.getPosition())
            .bounty(jobRequest.getBounty())
            .stack(jobRequest.getStack())
            .description(jobRequest.getDescription())
            .status(Status.OPEN)
            .build();
    Job savedJob = jobRepository.save(createdJob);
    foundCompany.getJobs().add(savedJob); // 
    return savedJob;
}
```

* 참고자료: [연관관계 매핑 기초](https://velog.io/@tigger/%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84-%EB%A7%A4%ED%95%91-%EA%B8%B0%EC%B4%88)
</details>

### Sql 어노테이션 관련 이슈

<details>
<summary>클릭하여 상세보기</summary>
<br>

* Sql 어노테이션은 클래스에 적용하더라도 각 메소드에 개별적으로 적용됨.
* 이렇게 적용된 경우, 컨텍스트를 정리하지 않으면 AutoIncrement 값이 재설정 되지 않음.
* 이전에는 이를 해결하기 위해 `@DirtiesContext` 어노테이션을 활용했음.
    - 그러나, 이 경우 각 테스트가 스프링 컨텍스트를 새로 불러와 테스트 속도가 저하됨.
* 이를 해결하기 위해서 init.sql 파일과 reset.sql 파일을 따로 작성함.
    - init.sql 파일은 테스트가 동작하기 전에 실행.
    - reset.sql 파일은 테스트가 동작한 뒤 실행.
* `@Sql`에 설정된 스크립트는 기본적으로 테스트가 동작하기 전에 실행됨.
    - 따라서 별도로 `executionPhase`를 `ExecutionPhase.AFTER_TEST_METHOD`로 설정해야 함.
* 이를 통해 작성한 테스트 클래스는 아래와 같음.

```java
@Sql(scripts = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:data/init.sql")
@Transactional
@SpringBootTest
class JobServiceTest {
    // JobService 테스트 코드
}
```

</details>

### QueryDSL 적용

<details>
<summary>클릭하여 상세보기</summary>
<br>

#### 1. Config
* QueryDSL을 활용하기 위해서 JPAQueryFactory를 활용해야 함.
    - 이를 QueryDSL이 적용된 Repository에서 주입받아 사용하기엔 불편함.
    - 따라서, Config을 생성하여 JPAQueryFactory를 Bean으로 등록하고 생성자 주입을 수행할 수 있도록 설정.
* 만약 Config을 하지 않는 경우엔 EntityManager를 내려받아서 매번 새로운 JPAQueryFactory 인스턴스를 만들어야 함.
    - JPA Criteria의 경우엔 CriteriaBuilder를 만들 때 EntityManager를 항상 활용했기 때문에 달리 설정할 방법이 없었음.

```java
@Configuration
public class JpaConfig {

    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
```

#### 2. 적용 결과
* QueryDSL과 JPA Criteria를 비교하면 아래와 같음.이를 적용하면 아래와 같음
    - 먼저 JPA Criteria로 작성됐던 이전 findByName 메소드는 아래와 같음.

```java
@Override
public Optional<User> findByName(String name) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> query = builder.createQuery(User.class);
    Root<User> user = query.from(User.class);

    Predicate hasGivenName = builder.equal(user.get("name"), builder.literal(name));
    query.select(user);
    query.where(hasGivenName);
    return entityManager
            .createQuery(query)
            .getResultStream()
            .findFirst();
}
```

* QueryDSL을 적용하면 아래와 같이 간단히 작성할 수 있음.

```java
@Override
public Optional<User> findByName(String name) {
    return queryFactory
            .selectFrom(user)
            .where(user.name.eq(name))
            .stream()
            .findFirst();
}
```

* 마찬가지로 stream으로 변환한 뒤 findFirst를 활용하면 Optional로 감싼 값을 반환할 수 있음.
    - list를 반환받기 위해선 fetch라는 메소드를 활용함.

#### 3. 클래스 생성자를 활용하여 원하는 객체를 반환하기
* 이전에 builder.construct를 활용했던 것과 동일한 동작을 수행하기 위해 Projections.constructor를 활용해야 함.
    - 예시 코드는 아래와 같음.

```java
query = new JPASQLQuery<Void>(entityManager, templates);
List<CatDTO> catDTOs = query.select(Projections.constructor(CatDTO.class, cat.id, cat.name))
        .from(cat)
        .orderBy(cat.name.asc())
        .fetch();
```

#### 4. DataJpaTest 오류에 대한 트러블슈팅
* SpringBootTest 어노테이션이 붙은 테스트들은 문제없이 동작하였으나, DataJpaTest 어노테이션을 활용한 테스트가 모두 오류가 발생.
* 이는 JPAQueryFactor를 제대로 불러오지 못해 발생하는 문제.
    - 이를 수정하기 위해 아래와 같이 QueryDslTestConfig을 따로 작성함.

```java
@TestConfiguration
public class QueryDslTestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

}
```

* 이를 불러오는 어노테이션을 붙인 테스트는 아래와 같음.
    - org.springframework.context.annotation.Import을 활용하고, 설정한 Config 파일의 클래스를 넣어줌.

```java
@Sql(value = "classpath:data/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "classpath:data/init.sql")
@Import(QueryDslTestConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class CompanyCustomRepositoryImplTest {
    // 테스트 내용
}
```

* 참고 자료
    - [Querydsl - Repository에서 Querydsl 사용하기 | IT 개발자들의 울타리 - 티스토리](https://jddng.tistory.com/341)
    - [2. Tutorials | querydsl.com](http://querydsl.com/static/querydsl/4.0.0/reference/html/ch02.html)
    - [[QueryDsl] @DataJpaTest 에서 @Repository 테스트하기](https://rachel0115.tistory.com/entry/QueryDsl-DataJpaTest-%EC%97%90%EC%84%9C-Repository-%ED%85%8C%EC%8A%A4%ED%8A%B8%ED%95%98%EA%B8%B0)

</details>

___

## 기타 링크
### [nopim | Postman API Network](https://www.postman.com/spacecraft-technologist-86595620/workspace/nopim)
* User, Company, Job, Application 콜렉션
* 상술한 기능들에 대한 HTTP 요청 테스트 용도로 활용

___

## 저자
* <a href="https://github.com/neppiness">김정현(neppiness)</a> (클릭 시 깃허브로 이동)

___

## 이전 문서
* 기능 요구사항: [클릭 시 문서로 이동](./docs/requirement.md)
* 서버 설계: [클릭 시 문서로 이동](./docs/design.md)
* 구현 과정: [클릭 시 문서로 이동](./docs/implementation.md)