# 프로젝트 명세서 (Project Specification): VibeApp

이 문서는 **VibeApp** 프로젝트의 현재 구현 현황 및 세부 명세를 정의한다.

---

## 프로젝트 설정 (Project Setup)

| 항목 | 설정 내용 |
| :--- | :--- |
| **JDK** | JDK 25 |
| **Language** | Java |
| **Spring Boot** | 4.0.1 |
| **Build Tool** | Gradle 9.3.0 이상 (Groovy DSL) |
| **설정 파일** | `application.yml` (YAML) |

---

## 플러그인 (Plugins)

| 플러그인 ID | 버전 | 설명 |
| :--- | :--- | :--- |
| `org.springframework.boot` | 4.0.1 | Spring Boot 빌드 플러그인 |
| `io.spring.dependency-management` | 1.1.7 | 의존성 버전 统합 관리 |

---

## 의존성 (Dependencies)

| 의존성 | 버전 | 설명 |
| :--- | :--- | :--- |
| `spring-boot-starter` | 4.0.1 | Spring Boot 기본 스타터 |
| `spring-boot-starter-web` | 4.0.1 | Spring MVC, 내장 Tomcat |
| `spring-boot-starter-thymeleaf` | 4.0.1 | Thymeleaf 템플릿 엔진 |
| `spring-boot-starter-validation` | 4.0.1 | Bean Validation (Jakarta) |
| `spring-boot-starter-jdbc` | 4.0.1 | JDBC 및 트랜잭션 지원 |
| `mybatis-spring-boot-starter` | 4.0.0 | MyBatis ORM 연동 |
| `com.h2database:h2` | 자동 | H2 인메모리/파일 DB |
| `spring-boot-starter-test` | 4.0.1 | 테스트 스타터 (testImplementation) |

---

## 프로젝트 메타데이터 (Project Metadata)

| 항목 | 내용 |
| :--- | :--- |
| **Group** | `com.example` |
| **Artifact** | `vibeapp` |
| **Name** | `vibeapp` |
| **Version** | `0.0.1-SNAPSHOT` |
| **Main Class** | `com.example.vibeapp.VibeApp` |
| **Base Package** | `com.example.vibeapp` |

---

## 데이터베이스 (Database)

### 설정

| 항목 | 내용 |
| :--- | :--- |
| **DB 종류** | H2 (파일 모드) |
| **JDBC URL** | `jdbc:h2:file:./data/testdb;AUTO_SERVER=TRUE;MODE=MySQL` |
| **Username** | `sa` |
| **콘솔 경로** | `/h2-console` |
| **DDL 실행** | `spring.sql.init.mode=always` (`schema.sql`) |
| **초기 데이터** | `data.sql` (애플리케이션 기동 시 자동 삽입) |

### 테이블 정의

#### `POSTS` — 게시글 테이블

| 컬럼명 | 타입 | 제약조건 |
| :--- | :--- | :--- |
| `no` | `BIGINT` | PK, AUTO_INCREMENT |
| `title` | `VARCHAR(200)` | NOT NULL |
| `content` | `CLOB` | NOT NULL |
| `created_at` | `TIMESTAMP` | DEFAULT CURRENT_TIMESTAMP |
| `updated_at` | `TIMESTAMP` | NULL |
| `views` | `INT` | DEFAULT 0 |

#### `POST_TAGS` — 게시글 태그 테이블

| 컬럼명 | 타입 | 제약조건 |
| :--- | :--- | :--- |
| `id` | `BIGINT` | PK, AUTO_INCREMENT |
| `post_no` | `BIGINT` | NOT NULL, FK → `POSTS.no` ON DELETE CASCADE |
| `tag_name` | `VARCHAR(50)` | NOT NULL |

---

## 패키지 구조 (Package Structure)

```
com.example.vibeapp
├── VibeApp.java                        # 메인 클래스 (@SpringBootApplication)
├── config/
│   └── WebConfig.java                  # MyBatis, 트랜잭션, H2 콘솔 설정
├── home/
│   └── HomeController.java             # 홈 화면 컨트롤러
└── post/
    ├── Post.java                       # 게시글 엔티티
    ├── PostTag.java                    # 게시글 태그 엔티티
    ├── PostMapper.java                 # 게시글 MyBatis 매퍼 인터페이스
    ├── PostTagRepository.java          # 게시글 태그 MyBatis 매퍼 인터페이스
    ├── PostService.java                # 게시글 서비스 (비즈니스 로직)
    ├── PostController.java             # 게시글 컨트롤러 (MVC)
    └── dto/
        ├── PostCreateDto.java          # 게시글 등록 DTO (record)
        ├── PostUpdateDto.java          # 게시글 수정 DTO (record)
        ├── PostListDto.java            # 게시글 목록 DTO (record)
        └── PostResponseDto.java        # 게시글 응답 DTO (record, 태그 포함)
```

---

## 설정 파일 구조 (Resource Structure)

```
src/main/resources
├── application.yml                     # 애플리케이션 설정
├── schema.sql                          # DB 테이블 DDL (기동 시 자동 실행)
├── data.sql                            # 초기 샘플 데이터 (기동 시 자동 실행)
├── mapper/
│   ├── PostMapper.xml                  # 게시글 SQL 매핑
│   └── post/
│       └── PostTagMapper.xml           # 게시글 태그 SQL 매핑
└── templates/
    ├── home/
    │   └── index.html                  # 홈 화면
    └── post/
        ├── posts.html                  # 게시글 목록
        ├── post_detail.html            # 게시글 상세 (태그 표시)
        ├── post_new_form.html          # 게시글 등록 폼 (태그 입력)
        └── post_edit_form.html         # 게시글 수정 폼 (태그 입력)
```

---

## URL 목록 (URL Mapping)

| Method | URL | 설명 |
| :--- | :--- | :--- |
| GET | `/` | 홈 화면 |
| GET | `/posts` | 게시글 목록 (페이지네이션) |
| GET | `/posts/{no}` | 게시글 상세 (조회수 증가, 태그 표시) |
| GET | `/posts/new` | 게시글 등록 폼 (태그 입력 포함) |
| POST | `/posts/add` | 게시글 등록 처리 (태그 저장, 트랜잭션) |
| GET | `/posts/{no}/edit` | 게시글 수정 폼 (기존 태그 불러오기) |
| POST | `/posts/{no}/save` | 게시글 수정 처리 (태그 수정, 트랜잭션) |
| GET | `/posts/{no}/delete` | 게시글 삭제 (태그 CASCADE 삭제) |
| GET | `/h2-console/**` | H2 데이터베이스 콘솔 |

---

## 주요 기능 (Feature Summary)

| 기능 | 설명 |
| :--- | :--- |
| 게시글 CRUD | 게시글 등록, 조회, 수정, 삭제 |
| 게시글 목록 | 최신순 정렬, 페이지네이션 (5건/페이지) |
| 조회수 | 게시글 상세 접근 시 자동 증가 |
| 태그 관리 | 쉼표 구분 입력, `POST_TAGS` 테이블에 저장 |
| 태그 표시 | 상세 페이지 제목 하단 배지(Badge) 형태 출력 |
| 입력값 검증 | `@NotBlank`, `@Size` 어노테이션으로 유효성 검증 |
| 트랜잭션 | 게시글+태그 등록/수정을 단일 트랜잭션으로 처리 |
| MyBatis 연동 | XML 매퍼 파일로 SQL 작성, camelCase 자동 변환 |
| H2 콘솔 | 개발/테스트 시 `/h2-console` 에서 DB 직접 조회 가능 |

---

## 트랜잭션 정책 (Transaction Policy)

| 메서드 | 트랜잭션 | 설명 |
| :--- | :--- | :--- |
| `PostService.create()` | `@Transactional` | 게시글 INSERT + 태그 INSERT를 하나의 트랜잭션으로 처리 |
| `PostService.update()` | `@Transactional` | 게시글 UPDATE + 태그 DELETE + 태그 INSERT를 하나의 트랜잭션으로 처리 |

`WebConfig`에 `DataSourceTransactionManager`가 등록되어 있으며, `@EnableTransactionManagement`로 트랜잭션 AOP가 활성화됨.

---

## MyBatis 설정 (MyBatis Configuration)

| 항목 | 설정 |
| :--- | :--- |
| 매퍼 스캔 | `@MapperScan("com.example.vibeapp.post")` |
| XML 매퍼 위치 | `classpath:mapper/**/*.xml` |
| 별칭 패키지 | `com.example.vibeapp.post` |
| camelCase 변환 | `mapUnderscoreToCamelCase=true` |
