# 📂KnowledgeBase Engine 
- 1단계: Core API

## Project Overview
- **목표**: 효율적인 지식 관리와 버전 제어를 위한 **지식 베이스 플랫폼**의 백엔드 파운데이션 구축.
- **진행 단계** 
  - 1단계
    - 인증(Auth) 및 문서 CRUD 핵심 API 엔진 구축
    - 클라우드 인프라 및 CI/CD 자동화 파이프라인 완성

## Tech Stack
- Language: Java 17
- Framework: Spring Boot 3.5.9
- Database: PostgreSQL
- Infrastructure: Docker, Docker Compose
- Cloud: AWS EC2
- CI/CD: GitHub Actons, SSH (Automated Deployment)

## Key Experience
### 1. CI/CD 파이프라인을 통한 배포 자동화
 - **GitHub Actions와 SSH**를 연동하여 코드 업데이트 시 AWS 서버에 자동으로 반영도록 구축
 - 반복적인 배포 작업을 자동화하여 **개발 사이클을 단축**하고 운영 효율성 높이고자 함

### 2. Docker를 활용한 데이터 인프라 관리
- **Docker 컨테이너로 PostgreSQL을 관리**하여, OS나 로컬 설정에 구애받지 않는 독립적인 개발이 가능한 Ready-to-Code 환경을 구축
- DB 스키마 버전 관리: **Flyway**를 도입하여 SQL 스크립트를 통한 **데이터베이스 형상관리**를 자동화
- Git에 포함된 Migration 히스토리를 통해 팀원 누구나 동일한 DB 스키마 상태를 유지할 수 있게 함으로써, 로컬 설정 차이로 인한 오류를 원천 차단

### 3.확장성을 고려한 서비스 설계
 - 향후 '버전 관리 지식베이스'로의 확장을 염두에 두고, 보안이 강화된 회원가입/로그인(Spring Security 등)과 유연한 문서 관리 API를 우선 구현

## Deployment
- API Endpoint: 13.60.244.84:8080
- API Documentation
  - [Postman Workspace 바로가기](https://www.postman.com/jieun-s-individual/workspace/comprag-aws)
  - 