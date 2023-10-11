# 설계 내용

## 도메인
### 회사 | Company
* 테이블 이름: company
* 필드(열 이름)
    - 회사_id(company_id): 숫자, 기본 키
    - 회사명(company_name): 문자열
    - 국가(company_country): 문자열
    - 지역(company_region): 문자열
* 국가와 지역에 대한 정보를 회사 클래스에 기록

### 사용자 | User
* 테이블 이름: user
* 필드(열 이름)
    - 사용자_id(user_id): 숫자, 기본 키
    - 사용자_이름(user_name): 문자열

### 채용공고 상세 | Job
* 테이블 이름: job
* 필드(열 이름)
    - 채용공고_id(job_id): 숫자, 기본 키
    - 회사_id(job_company_id): 숫자, 외래키
        + 이를 통해 회사명, 국가, 지역 정보를 불러옴.
    - 회사(job_company): Company 클래스
    - 채용포지션(job_position): 문자열
    - 채용보상금(job_bounty): 숫자
    - 사용기술(job_stack): 문자열
    - 채용내용(job_description): 문자열
* 메소드
    - `JobDto convertToJobDto()`
        + 채용공고 상세(Job) 인스턴스를 JobDto 클래스의 인스턴스로 변환하는 메소드
    - `JobSimpleDto convertToJobSimpleDto()`
        + 채용공고 상세(Job) 인스턴스를 JobSimpleDto 클래스의 인스턴스로 변환하는 메소드

### 채용공고 및 채용공고 상세 데이터 전송 객체 | JobSimpleDto, JobDto
* 테이블 이름: job
* 공통 필드
    - 채용공고_id: 숫자
    - 회사명: 문자열
    - 국가: 문자열
    - 지역: 문자열
    - 채용포지션: 문자열
    - 채용보상금: 숫자
    - 사용기술: 문자열
* 추가 상세 정보 필드
    - 채용내용: 문자열
    - 회사가 올린 다른 채용공고
        + 리스트, 출력이 필요할 때만 DB 정보를 활용해 값을 얻음

### 지원내역 | Application
* 테이블 이름: application
* 필드(열 이름)
    - 지원내역_id(application_id): 숫자, 기본 키
    - 채용공고_id(application_job_id): 숫자, 외래 키
    - 사용자_id(application_user_id): 숫자, 외래 키

<br>

## 컨트롤러
### 회사(/company)
* 등록(/add)
    - 회사명, 국가, 지역을 요청 파라미터로 전달하여 수행
* 삭제(/delete)
    - 전달받은 id의 회사를 삭제한다.
* 리셋(/reset)
    - 데이터베이스 내의 회사를 모두 삭제한다.

### 사용자(/user)
* 등록(/add)
    - 사용자명을 받아 사용자 생성
* 삭제(/delete)
    - 전달받은 id를 가진 사용자 삭제
* 리셋(/reset)
    - 데이터베이스 내의 사용자를 모두 삭제한다

### 채용공고(/job)
* 모든 채용공고 불러오기(GET, /)
    - JobInShort 정보에 맞게 재구성 후 리스트 반환
* 조건에 따른 채용공고 불러오기(GET, /)
    - 부여한 조건에 맞는 채용공고 목록을 JobInShort 정보에 맞게 재구성 후 리스트 반환 
        + 효율적으로 처리하기 위해선 전체 정보를 다 가져온 뒤 처리하는 게 아님
        + 데이터베이스에서 조건에 맞는 것들만 불러와서 작업하는 게 좋음
* 채용공고 등록(POST, /add)
    - 회사 id, 채용포지션, 채용보상금, 채용내용, 사용기술을 전달해 공고 등록
    - Job 클래스를 저장
* 채용공고 수정(PUT, /update)
    - 전달 받은 키에 대한 수정 수행
    - id는 수정할 수 없도록 구현
* 채용공고 삭제(DELETE, /delete)
    - 채용공고 id를 전달받고, 채용공고를 삭제함
* 채용공고 상세(GET, /detail/)
    - 채용 데이터에 회사가 올린 다른 채용공고 추가
    - 회사 ID를 인자로 전달 받고 ID 리스트 

### 지원내역(/application)
* 등록(POST, /add)
    - 정적메소드, 사용자 ID와 채용공고 ID를 전달받고 해당하는 지원내역을 반환
    - 사용자가 '지원' 메소드를 사용하는 경우 지원이력이 있는지 확인.
    - 지원 이력이 있다면 지원내역을 생성하지 않음.