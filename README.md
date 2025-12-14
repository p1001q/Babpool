<div align="center">

<!-- logo -->
<img src="" width="400"/>

### BabPool 🍚  
### 위치 기반 음식점 추천 & 리뷰 웹 서비스

</div>

---

## 📝 소개

**BabPool(밥풀)** 은  
사용자의 위치와 취향을 기반으로 음식점을 탐색하고  
리뷰, 북마크, 랜덤 추천 기능을 제공하는 **음식점 추천 & 리뷰 웹 서비스**입니다  

단순한 목록형 검색이 아니라  
- 카테고리 / 태그 기반 분류
- 지도 마커 연동
- 랜덤 음식점 추천
- 관리자 전용 관리 페이지

를 통해 **실제 서비스 흐름을 고려한 웹 애플리케이션**으로 구현했습니다

---

## 🧩 주요 기능

### 👤 사용자 기능
- 회원가입 / 로그인 / 로그아웃
- 자동 로그인(Filter 기반)
- 마이페이지 (프로필 이미지 업로드, 비밀번호 변경)
- 음식점 목록 조회 및 상세 페이지 확인
- 리뷰 작성 및 삭제 (이미지 포함)
- 음식점 북마크 토글
- 랜덤 음식점 추천 기능

### 🛠 관리자 기능
- 사용자 전체 조회 / 상세 조회 / 수정 / 삭제
- 음식점 CRUD 관리
- 메뉴 CRUD 관리
- 리뷰 조회 / 수정 / 삭제
- 지도 마커 관리
- 카테고리 / 태그 관리 및 매핑

---

## 🖥 화면 구성

> 실제 서비스 화면 캡처 또는 GIF를 첨부합니다

|Main Page|Detail Page|
|:---:|:---:|
|<img src="" width="400"/>|<img src="" width="400"/>|

---

## 🗂️ APIs

본 프로젝트는 **Servlet 기반 MVC 구조**로 구현되었으며  
기능 단위로 URL을 명확히 분리하여 설계했습니다

👉🏻 [API 상세 문서 바로가기](/backend/APIs.md)

---

## ⚙ 기술 스택

### Back-end
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Mysql.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/JSP.png?raw=true" width="80">
</div>

### Front-end
- JSP
- HTML / CSS
- JavaScript
- Bootstrap

### Infra / Library
- Apache Tomcat
- MySQL Connector
- JSTL
- Jakarta Mail (이메일 인증)
- Commons FileUpload (파일 업로드)

---

## 🛠️ 프로젝트 아키텍처

![no-image]()

### 구조 설명
- **Controller (Servlet)**  
  요청을 받아 비즈니스 로직 수행 후 JSP로 포워딩
- **DAO**  
  JDBC 기반 DB 접근 로직 분리
- **DTO**  
  계층 간 데이터 전달 객체
- **Filter**  
  자동 로그인, 로그 파일 기록
- **JSP(View)**  
  사용자 화면 렌더링

---

## 🧱 DB / ERD 설계

https://www.erdcloud.com/d/S3fkBFGf7DZbjzDfq

- User / Store / Review / Menu / Bookmark
- Category / Tag / Marker
- 다대다 관계를 매핑 테이블로 분리하여 정규화
- SQL 스크립트 기반 초기 데이터 구성

> `/webapp/resource/sql` 경로에 테이블 생성 및 초기 데이터 스크립트 포함

---

## 🤔 기술적 이슈와 해결 과정

### 1️⃣ 자동 로그인 구현
- 문제  
  로그인 상태 유지가 불가능함
- 해결  
  Filter를 이용해 세션 및 쿠키 기반 자동 로그인 처리
- 결과  
  사용자 UX 개선 및 인증 로직 분리

---

### 2️⃣ 리뷰 이미지 업로드 처리
- 문제  
  다중 이미지 업로드 시 서버 처리 오류 발생
- 해결  
  Commons FileUpload 라이브러리 활용  
  파일 저장 로직과 DB 저장 로직 분리
- 결과  
  안정적인 리뷰 이미지 업로드 구현

---

### 3️⃣ 관리자 / 사용자 기능 분리
- 문제  
  동일 URL에서 권한 구분이 어려움
- 해결  
  URL 패턴을 기준으로 관리자 전용 서블릿 분리
- 결과  
  유지보수성과 보안성 향상

---

## 📁 프로젝트 구조

```text
BabPool
 ├─ controller
 │   ├─ user
 │   └─ admin
 ├─ dao
 ├─ dto
 ├─ filter
 ├─ utils
 ├─ webapp
 │   ├─ jsp
 │   ├─ css
 │   ├─ js
 │   ├─ images
 │   └─ sql
 └─ logs
