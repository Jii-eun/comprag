## 📚 KnowledgeBase Engine

Spring Boot 기반 REST API 서버를 구현하고, 개발 환경 통일과 배포 자동화까지 구성한 개인 프로젝트입니다.
이 프로젝트는 문서 CRUD와 AI를 활용한 지식 베이스(Knowledge Base) 플랫폼 구축을 목표로 합니다.

개인적으로 DB 스키마 충돌과 개발 환경 차이로 인해 반복적인 재작업을 경험했습니다.
이러한 문제를 직접 해결해 보고, 향후 AI 기반 지식 검색까지 확장할 수 있는 백엔드 구조를 만들어 보기 위해 시작했습니다.

향후에는 저장된 문서를 AI가 검색·요약·질의응답에 활용할 수 있도록 LLM 기반 지식 검색(RAG) 기능을 단계적으로 확장할 계획입니다.

---

## 🎯 프로젝트 목표

현재 구현 범위(Core)

* 회원 및 인증 시스템 구축
* REST API 기반 문서 CRUD
* Docker 기반 개발 환경 통일
* Flyway 기반 DB 형상관리
* GitHub Actions 기반 자동 배포

향후 확장 계획

* AI 기반 문서 검색 및 질의응답(RAG)
* 문서 임베딩 및 Vector DB 연동
* 자연어 검색 기능
* AI 기반 문서 요약
* 버전 관리 기능을 포함한 Knowledge Base 서비스 구축

---

## 📌 Tech Stack

#### Backend

* Java 17
* Spring Boot
* Spring Security (JWT)

#### Database

* PostgreSQL
* Flyway

#### Infrastructure

* Docker
* Docker Compose
* AWS EC2

#### CI/CD

* GitHub Actions
* SSH

---

## ✨ 주요 기능

* 회원가입 / 로그인 (JWT 인증)
* REST API 기반 문서 CRUD
* PostgreSQL 연동
* Flyway 기반 DB Migration 관리
* Docker Compose 기반 개발 환경 구성
* GitHub Actions를 이용한 자동 배포

---

## 🔧 프로젝트에서 해결하고자 했던 문제

### 1. 개발 환경 차이

개발 환경마다 발생하는 실행 환경 차이를 줄이기 위해 Docker Compose 기반 개발 환경을 구성했습니다.

#### 결과

* 동일한 개발 환경 제공
* 로컬 환경 설정 시간 감소
* 실행 환경 차이 최소화

---

### 2. DB 스키마 불일치

실무에서 경험한 DB 스키마 불일치 문제를 바탕으로 Flyway를 적용하여 스키마 변경 이력을 관리했습니다.

#### 결과

* Migration 이력 관리
* 동일한 DB 스키마 유지
* 스키마 충돌 감소

---

### 3. 반복적인 수동 배포

GitHub Actions를 활용하여 코드 Push 시 자동으로 EC2 서버에 배포되도록 구성했습니다.

#### 결과

* Push만으로 자동 배포
* 반복 작업 감소
* 배포 과정 단순화

---

## 📂 프로젝트 구조

```text
com.proj.comprag
├── config          # Spring 및 환경설정
├── common          # 공통 응답, 예외 처리, 유틸
├── security
│   └── jwt         # JWT 인증/인가
│
├── domain          # Entity
├── dto             # Request / Response DTO
├── service         # 비즈니스 로직
├── indexing        # 문서 인덱싱 (향후 AI 검색 확장)
│
├── web
│   ├── auth        # 회원 인증 API
│   ├── document    # 문서 CRUD API
│   ├── admin       # 관리자 API
│   ├── rag         # AI 검색 API (예정)
│   └── debug       # 테스트 API
│
└── CompragApplication
```

---

## 🚀 실행 방법

```bash
docker-compose up -d
```

이후 Spring Boot 서버를 실행합니다.

---

