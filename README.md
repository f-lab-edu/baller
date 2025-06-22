# Baller
동호회별로 경기를 생성하고 참여할 수 있으며, 실시간으로 점수 및 경기 상황을 전송하고 관리할 수 있는 플랫폼

--------------

## 주요 기능
- 회원가입 / 로그인 (JWT 기반 인증)
- 동호회 간 경기 생성 및 참가 요청 기능
- 경기 시작/종료 제어 및 실시간 점수 입력
- 경기 결과 기록 저장 및 조회
- SSE(Server-Sent Events)를 활용한 실시간 점수 중계
- 동아리 멤버 권한 관리 (일반/운영자)

---------------

## 사용 기술
- Language : Java 17
- Framework : Spring Boot, Spring Security
- DB : Postgresql, MyBatis
- Others : JWT, WebSocket, STOMP, MultipartFile
- Containerization: Docker
- Monitoring : Prometheus + Grafana
