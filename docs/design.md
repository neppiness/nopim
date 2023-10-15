# 설계 내용

## 도메인
### 회사 | Company
* 테이블 이름: company
* 필드(열 이름)
    - 회사_id(company_id): 숫자, 기본 키
    - 회사명(company_name): 문자열
    - 국가(company_country): 문자열
    - 지역(company_region): 문자열
    - 회사가_올린_다른_채용공고(company_jobs): 
        + 회사가 채용공고를 올리면 갱신.
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
    - 회사(job_company): Company 클래스
        + 이를 통해 회사명, 국가, 지역 정보를 불러옴.
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
* 공통 필드
    - 채용공고_id: 숫자
    - 회사명: 문자열
    - 국가: 문자열
    - 지역: 문자열
    - 채용포지션: 문자열
    - 채용보상금: 숫자
    - 사용기술: 문자열
* JobDto에 대한 추가 상세 정보 필드
    - 채용내용: 문자열
    - 회사가 올린 다른 채용공고
        + 회사(company) 테이블의 회사가_올린_다른_채용공고(company_jobs)를 활용하여 출력
        + 현재 보고 있는 채용공고 ID는 제외

### 지원내역 | Application
* 테이블 이름: application
* 필드(열 이름)
    - 지원내역_id(application_id): 숫자, 기본 키
    - 채용공고_id(application_job_id): 숫자, 외래 키
    - 사용자_id(application_user_id): 숫자, 외래 키

<br>

## 컨트롤러
### 회사(/company)
* 등록(POST, /add)
    - 회사명(name), 국가(country), 지역(region)을 요청 파라미터로 전달하여 수행
* 아이디로 조회(GET, /{companyId})
    - companyId에 해당하는 회사를 찾아 반환한다.
    - 해당하는 회사가 없는 경우, 에러를 발생시킨다.
* 전체 조회(GET, /all)
    - 지금까지 등록된 모든 회사 정보를 반환한다.
* 삭제(DELETE, /{companyId})
    - companyId에 해당하는 회사를 삭제한다.
    - 해당하는 회사가 없는 경우, 에러를 발생시킨다.
* 초기화(DELETE, /all)
    - 데이터베이스 내의 회사를 모두 삭제한다.

### 사용자(/user)
* 등록(POST, /add)
    - 사용자명을 받아 사용자 생성
* 아이디로 조회(GET, /{userId})
    - userId에 해당하는 사용자를 찾아 반환한다.
    - 해당하는 사용자가 없는 경우, 에러를 발생시킨다.
* 전체 조회(GET, /all)
    - 지금까지 등록된 모든 사용자 정보를 반환한다.
* 삭제(DELETE, /{userId})
    - userId에 해당하는 사용자를 삭제
* 초기화(DELETE, /all)
    - 데이터베이스 내의 사용자 정보를 모두 삭제한다.

### 채용공고(/job)
* 채용공고 등록(POST, /add)
    - 회사 id, 채용포지션, 채용보상금, 채용내용, 사용기술을 전달해 공고 등록
* 조건에 부합하는 채용공고 조회(GET, /search)
    - 부여된 조건에 맞는 채용공고 목록을 수집
        + 채용공고 ID, 회사 ID, 회사명, 채용포지션, 사용기술을 통해 검색
    - JobInShort 정보에 맞게 재구성 후 리스트 반환
* 채용공고 상세정보 조회(GET, /detail/{job_id})
    - 채용공고 ID를 인자로 전달 받고 해당하는 채용공고의 모든 필드를 반환
        + 기존 데이터에서 '회사가 올린 다른 채용공고', '채용상세' 추가
* 모든 채용공고 조회(GET, /all)
    - JobSimpleDto 필드에 맞게 재구성 후 목록 반환
* 채용공고 수정(PUT, /update/{job_id})
    - 경로 변수 job_id를 통해 채용공고 조회
    - 수정 가능한 인자: 채용포지션, 채용보상금, 채용내용, 사용기술
    - 전달받은 인자에 해당하는 내용을 업데이트
        + job_id는 수정할 수 없음
* 채용공고 삭제(DELETE, /delete/{job_id})
    - 채용공고 id를 전달받고, 채용공고를 삭제
* 채용공고 초기화(DELETE, /delete/all)
    - 모든 채용공고를 삭제

### 지원내역(/application)
* 등록(POST, /add)
    - 정적메소드, 사용자 ID와 채용공고 ID를 전달받고 해당하는 지원내역을 반환
    - 사용자가 '지원' 메소드를 사용하는 경우 지원이력이 있는지 확인.
    - 지원 이력이 있다면 지원내역을 생성하지 않음.