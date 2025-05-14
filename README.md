# Witty Wave

## 프로젝트 소개
**Witty Wave**는 팀 협업의 효율성을 극대화하는 통합 그룹웨어 시스템입니다. 주로 협업이 많은 IT 회사들을 대상으로, 업무 효율성을 향상시키기 위한 다양한 기능들을 제공합니다.

**기간**: 2023.12.20 ~ 2024.02.28  

**팀원**: 5명
<table>
  <tr>
     <td align="center">
      <img src="https://github.com/user-attachments/assets/b2d917e7-0a1f-4849-98f0-05a9c0d3ca7f" width="100"/><br/>
      🐳 이준경<br/>
      <code>INTP</code>
    </td>
     <td align="center">
      <img src="https://github.com/user-attachments/assets/f8af518a-a347-4523-a6d5-10374a80a9c9" width="100"/><br/>
      🍀 차윤하<br/>
      <code>ISFP</code>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/b6634003-09f8-4f21-8066-9189fb6a842b" width="100"/><br/>
      🤛 정지섭<br/>
      <code>ENTP</code>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/f4b3d442-b719-4602-8c81-e43fa30bef22" width="100"/><br/>
      🐾 김성민<br/>
      <code>ISFJ</code>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/610cdbd5-c99f-4ec7-8dd5-ddea467869fa" width="100"/><br/>
      😊 서보혜<br/>
      <code>INFP</code>
    </td>
  </tr>
</table>


---

## 주요 기능
- **WebSocket을 이용한 실시간 메신저**: 실시간으로 팀원들과 소통할 수 있는 메신저
- **프로젝트 및 업무 관리 시스템**: 프로젝트와 업무의 효율적인 관리 시스템
- **전자 결재 기능**: 결재 요청 및 승인을 위한 시스템
- **사내 게시판**: 공지 및 정보 공유를 위한 게시판 기능
- **근태 관리**: 출퇴근 시간 및 근태 기록 관리

---

## 기술 스택

![Web App Reference Architecture (1)](https://github.com/user-attachments/assets/e9b84943-7239-4ddf-8cae-85694498cf27)

---

## 와이어프레임
[Figma](https://www.figma.com/design/Dhd2HPbpUeezBRoI42CjSF/Witty-Wave-%EA%B7%B8%EB%A3%B9%EC%9B%A8%EC%96%B4?m=auto&t=s3R2goQeoJUGu5fj-6)

---

## ERD
- **게시판** 데이터베이스 구조 ERD

![Witt-Wave 게시판ERD](https://github.com/user-attachments/assets/e5f569fd-8d93-4d76-a8ea-169be7940399)


---

## 주요 작업

#### 1. **JWT-Token 인증 방식 로그인**
- **Spring Security와 JWT**를 활용하여 로그인 인증 기능을 구현했습니다.

![JWT 로그인](https://github.com/user-attachments/assets/3d74088a-7f62-4a5a-b3e0-5720ebc1b7a8)

#### 2. **구글메일 인증을 통한 비밀번호 찾기**
- 사용자가 **구글 메일 인증**을 통해 비밀번호를 찾을 수 있는 기능을 구현했습니다.

![구글메일 인증](https://github.com/user-attachments/assets/302a021a-996e-42b0-af7d-061ca867549e)

#### 3. **Tree view 형태의 조직도 관리**
- **js-tree**를 활용하여 조직도를 트리 뷰 형태로 관리할 수 있도록 구현했습니다.

![조직도](https://github.com/user-attachments/assets/1dba6869-af02-42b4-a258-a3675c79e10e)

#### 4. **WebSocket을 이용한 실시간 메신저**
- **WebSocket**을 이용해 실시간으로 팀원 간 소통할 수 있는 메신저 기능을 구현했습니다.

![메신저](https://github.com/user-attachments/assets/1bff2589-9381-4551-bbbe-423abfdc3500)

#### 5. **프로젝트 관리 시스템**
- 팀원 간 효율적인 프로젝트 관리와 업무 분담을 위한 시스템을 구축했습니다.

![프로젝트 관리](https://github.com/user-attachments/assets/166b8452-13a7-48f8-9bdf-f896e5ece66f)

#### 6. **사내 메일 기능**
- **사내 메일 시스템**을 통해 팀원 간 원활한 의사소통과 정보 공유가 가능합니다.

![사내 메일](https://github.com/user-attachments/assets/ee7eb237-d076-4314-b9ff-ef44aed08d4d)

---

## ⚙️ 중요한 설정값
프로젝트의 중요한 설정값은 [**이 사이트**](https://github.com/Witty-Puppy/Backend-settings)에서 확인할 수 있습니다.  
*(사이트는 private로, 팀 멤버만 접근 가능합니다.)*

