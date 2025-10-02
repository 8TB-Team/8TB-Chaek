# 📚 ChaekChaek : 도서 관리 시스템

## 프로젝트 개요
- 인기 도서 조회, 도서 대여/반납 및 예약이 가능한 도서 관리 시스템

## 프로젝트 기술 스택 & 환경
- 언어: Java 17
- 프레임워크: Spring Boot 3, Spring Data JPA, Spring Security
- DB: MySQL
- 캐시: Redis, Caffeine (In-Memory)
- 빌드 도구: Gradle
- API 테스트: Postman
- 기타: Redis 분산 락, 동시성 테스트

## 팀 역할
- 정직한 : `/reviews`, `/event` (팀장)  
- 서성경 : `/rentals`, `/search`  
- 유석진 : `/books`  
- 이동재 : `/reservation`  
- 이승희 : `/auth`, `/users`  
- **공동 작업** : 캐싱, 동시성 제어, 인덱싱 최적화

## 프로젝트 상세 내용
**구현 내용**  
- 도서 검색
  - 키워드 검색
  - 인기 검색어 조회 기능
- 도서 대여/반납
  - 대여 가능 여부 체크, 반납 처리
  - 대여 중인 도서 재대여 불가
- 도서 예약 (선착순 예약 기능)
  - Redis 분산 락으로 동시 예약 처리
- 도서 리뷰/평점
  - 사용자 리뷰 작성 및 조회
  - 평점 계산 및 평균 제공
- 선착순 이벤트
  - 이벤트 참여 인원 제한
  - 비관적 락 적용으로 동시성 처리
 
**핵심 포인트**
- 검색 API 성능 개선: Redis Cache 적용 → 평균 응답속도 40ms → 5ms
- 동시성 제어: 비관적 락 + Redis 분산 락 적용 → 데이터 무결성 보장
- 인덱스 최적화: B-Tree 인덱스 적용 → WHERE, JOIN, ORDER BY 성능 개선

---

## Redis 캐시 적용

### No Cache vs Redis Cache 
- 책 검색 키워드 / 인기 검색어 검색
- 데이터: 약 5만 건
- Postman 요청
- 요청 url: `localhost:8080/api/v1/books?keyword=행복` / `localhost:8080/api/v2/books?keyword=행복`

|            | 평균 응답속도 |
|---------------------|---------------|
| **No Cache** (DB 조회) | 약 40ms        |
| **Redis Cache**      | 약 5ms         | 

**V1 - No Cache**
- 매 요청마다 DB 조회 후 결과를 바로 반환
- 인기 검색어(`localhost:8080/api/v1/search/popular`)는 요청할 때마다 DB에서 키워드 카운트를 계산
    - 꾸준하게 7~12ms로 실시간 검색어 가져옴

**V2 - Redis Cache**
- 첫 번째 요청→ 캐시가 아직 없는 상태에서 DB 조회 후 결과를 Redis에 저장
- 이후 요청: 3~7ms → Redis에서 바로 조회 DB 접근 없음
- 인기 검색어(`localhost:8080/api/v2/search/popular`)도 Redis를 이용해 실시간 조회
    - 첫 요청 후 2~4ms로 실시간 검색어 가져옴

---

## 동시성 제어

### 비관적 락 (Pessimistic Lock)

- **비관적 락 적용 전**
  - 평균 테스트 실행 시간 : **0.1초 (2000 TPS)**
  - 신청된 사용자 수: **52~57명** (정원 50명 초과)
  - 결과: 동시성 제어가 되지 않아 중복 신청 발생
    

  ```bash
  Hibernate: select count(eum1_0.id) from event_user_map eum1_0 where eum1_0.event_map_id=?
  예외 발생: 해당 이벤트에 인원이 모두 찼습니다.
  ...
  테스트 실행 시간: 0.099초
  신청된 사용자 수: 52
  ```

- **비관적 락 적용 후**
  - 평균 테스트 실행 시간 : 0.25초 (1000 TPS)
  - 신청된 사용자 수: 50명 (정원 유지)
  - 결과: 동시성 제어 성공, 데이터 무결성 보장
    

  ```bash
  Hibernate: select count(eum1_0.id) from event_user_map eum1_0 where eum1_0.event_map_id=?
  예외 발생: 해당 이벤트에 인원이 모두 찼습니다.
  ...
  테스트 실행 시간: 0.25초
  신청된 사용자 수: 50
  ```

### 분산 락 (Redis + Spin Lock)

- **분산 락 적용 전**

동일한 도서 예약 시, 모든 요청이 같은 우선순위(1) 로 저장됨 → 동시성 문제 발생

성공한 예약 수: 10
고유한 우선순위 수: 1
우선순위 목록: [1]

- **분산 락 적용 후**

요청 순서에 따라 우선순위가 올바르게 부여됨

성공한 예약 수: 10
고유한 우선순위 수: 10
우선순위 목록: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

- **과부하 테스트** (50명 동시 예약)
  - 50명의 사용자가 동시에 예약 요청
  - ExecutorService + CountDownLatch 활용
  - 우선순위 정합성과 성공 횟수 검증

```bash
테스트 결과
=== 과부하 테스트 결과 ===
총 요청 수: 50
성공 수: 50
소요 시간: 775ms
초당 처리량: 64.5 TPS
과부하 동시성 테스트 통과
```

**결론**
Redis 분산 락 적용 후, 고부하 상황에서도 우선순위 정합성과 데이터 무결성 유지

---

## 인덱스 (Index)

### 개요
- `WHERE`, `JOIN`, `ORDER BY`, `GROUP BY` 수행 시 **전체 스캔(Full Scan)을 줄여 조회 성능 향상**
- 추가적인 **쓰기 연산과 저장 공간**을 활용하여 테이블 검색 속도를 높이는 자료구조
- 특히 **카디널리티(고유값 개수)** 가 많은 컬럼일수록 효과적

### B-Tree 인덱스
- 이진 트리를 확장한 구조 (한 노드가 여러 자식을 가질 수 있음)
- 데이터가 항상 **정렬된 상태** 유지
- 시간복잡도: **O(logN)**
- 낮은 트리 높이 + 균형 잡힌 구조 → **빠른 검색, 삽입, 삭제** 가능

### 장점
- 테이블 조회 속도 및 전반적 **쿼리 성능 향상**
- 시스템 전체 부하 감소

### 단점
- **쓰기 성능 저하**  
  - `INSERT`, `UPDATE`, `DELETE` 시 인덱스 갱신 필요
- **저장 공간 추가 소모** (DB 용량의 약 10% 정도 필요)

---

## 참고자료 / 트러블슈팅
- [Lock으로 동시성 처리 방법 및 테스트](https://www.notion.so/teamsparta/Lock-27e2dc3ef51480418a61ea860a7a9e47?source=copy_link)
- [In-Memory Cache](https://www.notion.so/teamsparta/In-Memory-Cache-27e2dc3ef51480e9aea5c83f182784f3?source=copy_link)
- [DB - Index](https://www.notion.so/teamsparta/8-8TB-2692dc3ef514800ea29bf4575af7fcc4?p=27e2dc3ef5148077bdb5fa649a0ad19d&pm=s)
- [Redis / 분산락 적용](https://www.notion.so/teamsparta/8-8TB-2692dc3ef514800ea29bf4575af7fcc4?p=27e2dc3ef51480d6911cf5d5c75394be&pm=s)
- [No Cache vs Redis Cache](https://www.notion.so/teamsparta/8-8TB-2692dc3ef514800ea29bf4575af7fcc4?p=27f2dc3ef51480a0bfcac2366b9824b9&pm=s)
- [Redis Sorted Set](https://www.notion.so/teamsparta/8-8TB-2692dc3ef514800ea29bf4575af7fcc4?p=27f2dc3ef51480c185ededfd17e5bac1&pm=s)
- [Reservation - Redis 분산 Lock 적용](https://www.notion.so/teamsparta/8-8TB-2692dc3ef514800ea29bf4575af7fcc4?p=27f2dc3ef514803b94c2d78d5609360a&pm=s)
- [Redis SENTEX & RedLock](https://www.notion.so/teamsparta/8-8TB-2692dc3ef514800ea29bf4575af7fcc4?p=2802dc3ef51480938011e5bf44d59368&pm=s)
  
