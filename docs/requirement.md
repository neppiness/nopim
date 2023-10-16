# 기능적 요구사항 구현 내용

## 1. 채용공고를 등록합니다.
* 구현 메소드: JobController 클래스 addJob 메소드
* 요청 메소드: POST
* 요청 URI: `/job/add`
* 요청 인자(request parameter)
    - 회사 ID(companyId)
    - 채용포지션(position)
    - 채용보상금(bounty)
    - 채용내용(description)
    - 사용기술(stack)
* 호출 예시
    + `localhost:8080/job/add?companyId=1&position=백엔드 주니어 개발자&bounty=500000&stack=python&description=원티드에서 주니어 백엔드 개발자를 채용합니다. 필수스택 파이썬.`
* 결과 예시
```json
{
    "채용공고_id": 52,
    "회사명": "wanted",
    "국가": "republic-of-korea",
    "지역": "seoul",
    "채용포지션": "백엔드 주니어 개발자",
    "채용보상금": 500000,
    "사용기술": "python"
}
```

## 2. 채용공고를 수정합니다.
* 구현 메소드: JobController 클래스 updateJob 메소드
* 요청 메소드: PUT
* 요청 URI: `/job/update/{jobId}`
* 경로 인자(path variable)
    - 채용공고 ID(jobId)
* 요청 인자(request parameter)
    - 채용포지션(position)
    - 채용보상금(bounty)
    - 채용내용(description)
    - 사용기술(stack)
* 요청 인자에 대해서만 수정 가능하도록 구현
* 호출 예시
    - `localhost:8080/job/update/52?position=백엔드 시니어 개발&bounty=1000000&stack=spring&description=원티드에서 백엔드 시니어를 모십니다. 필수스택 스프링.`
* 결과 예시
```json
{
    "채용공고_id": 52,
    "회사명": "wanted",
    "국가": "republic-of-korea",
    "지역": "seoul",
    "채용포지션": "백엔드 시니어 개발",
    "채용보상금": 1000000,
    "사용기술": "spring"
}
```

## 3. 채용공고를 삭제합니다.
* 구현 메소드: JobController 클래스 deleteJobById 메소드
* 요청 메소드: DELETE
* 요청 URI: `/job/delete/{jobId}`
* 경로 인자(path variable)
    - 채용공고 ID(jobId)
* 호출 예시
    - `localhost:8080/job/delete/52`
* 결과 예시
```text
The job is deleted (jobId: 52)
```

## 4. 채용공고 목록을 가져옵니다.
### 4.1. 채용공고 목록 조회 기능
* 구현 메소드: JobController 클래스 findAllJobs 메소드
* 요청메소드: GET
* 요청 URI: `/job/all`
* 호출
    - `localhost:8080/job/all`
* 결과 예시
```json
[
    {
        "채용공고_id": 1,
        "회사명": "naver",
        "국가": "republic-of-korea",
        "지역": "bundang",
        "채용포지션": "백엔드 주니어 개발자",
        "채용보상금": 500000,
        "사용기술": "spring"
    },
    {
        "채용공고_id": 2,
        "회사명": "wanted",
        "국가": "republic-of-korea",
        "지역": "seoul",
        "채용포지션": "백엔드 주니어 개발자",
        "채용보상금": 500000,
        "사용기술": "python"
    }
]
```

### 4.2. 채용공고 검색 기능 구현
* 구현 메소드: JobController 클래스 searchJob 메소드
* 요청메소드: GET
* 요청 URI: `/job/search`
* 요청 인자(request parameter)
    - 채용공고 ID(jobId)
    - 채용보상금(bounty)
    - 채용내용(description)
    - 사용기술(stack)
* 호출 예시
    - `localhost:8080/job/search?companyName=naver&position=백엔드 주니어 개발자&stack=spring` 
* 결과 예시
```json
[
    {
        "채용공고_id": 1,
        "회사명": "naver",
        "국가": "republic-of-korea",
        "지역": "bundang",
        "채용포지션": "백엔드 주니어 개발자",
        "채용보상금": 500000,
        "사용기술": "spring"
    }
]
```

## 5. 채용 상세 페이지를 가져옵니다.
* 구현 메소드: JobController 클래스 getJobDetail 메소드
* 요청메소드: GET
* 요청 URI: `/job/detail/{jobId}`
* 경로 인자(path variable)
    - 채용공고 ID(jobId)
* 호출 예시
    - `localhost:8080/job/detail/1` 
* 결과 예시
```json
{
    "채용공고_id": 1,
    "회사명": "naver",
    "국가": "republic-of-korea",
    "지역": "bundang",
    "채용포지션": "백엔드 주니어 개발자",
    "채용보상금": 500000,
    "사용기술": "spring",
    "채용내용": "네이버에서 주니어 백엔드 개발자를 채용합니다. 필수적으로 스프링을 활용할 줄 아셔야 합니다.",
    "회사가_올린_다른_채용공고": [
        52,
        53
    ]
}
```

## 6. 사용자는 채용공고에 지원합니다.
* 구현 메소드: ApplicationController 클래스의 addApplication 메소드
* 요청메소드: POST
* 요청 URI: `/application/{userId}/add`
* 경로 인자(path variable)
    - 사용자 ID(userId)
* 요청 인자(request parameter)
    - 채용공고 ID(jobId)
* 호출 예시
    - `localhost:8080/application/1/add?jobId=3`
* 결과 예시
```json
{
    "사용자_id": 1,
    "채용공고_id": 2
}
```
