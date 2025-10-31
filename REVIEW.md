## REVIEW

---

### 1. Domain

#### 1-1. Product 생성 시 Category 연관관계 설정 로직 개선

**문제**  
Product 생성 시 Category 정보가 단순 문자열로만 표현되어 확장성이 떨어짐.

**개선 내용**
- Category를 엔티티로 분리하여 상태와 식별자를 가질 수 있도록 개선함.
- Product 생성 시 Category 엔티티를 참조하도록 하여 도메인 간 관계를 명확히 표현함.

---

### 2. Controller

#### 2-1. RESTful API 설계 원칙 위반

**문제**  
HTTP Method와 URI 설계가 RESTful 원칙에 어긋남.  
예를 들어, 자원 삭제 기능에 DELETE 대신 POST를 사용하는 등 자원과 행위가 혼용되어 있음.  
또한 메서드명에 `by` 등의 행위 표현이 포함되어 URI 일관성이 떨어짐.

**개선 내용**
- RESTful하게 URI 및 Method를 재설계함.
    - `GET /categories/{categoryId}/products` : 특정 카테고리의 상품 조회
    - `GET /categories` : 카테고리 목록 조회

**Trade-Off**
- PUT(전체 수정) vs PATCH(일부 수정) 비교 결과,  
  일부 수정에도 PUT을 선택하여 클라이언트-서버 간 데이터 전달 형식의 일관성을 유지함.

---

#### 2-2. HTTP 응답 상태 코드 일관성 부족

**문제**  
모든 API가 `ResponseEntity.ok()`를 반환함.  
특히 `createProduct()`의 경우 201 Created를 반환해야 하나, 200 OK로 처리되어 RESTful 규칙에 어긋남.

**개선 내용**
- 201 Created 상태 코드와 함께 생성된 리소스의 URI를 반환하도록 개선함.
- 조회, 수정, 삭제 등 행위별로 적절한 상태 코드를 사용하도록 수정함.

---

#### 2-3. Entity 직접 반환 문제

**문제**  
Entity를 그대로 JSON으로 변환할 경우,  
`Product(1) → Category(N) → Product(1)` 형태의 순환 참조가 발생할 수 있음.

**개선 내용**
- Entity 대신 DTO를 반환하도록 수정하여 필요한 데이터만 전달함.
- 순환 참조 방지 및 응답 구조 단순화를 달성함.

---

#### 2-4. 메서드 네이밍 개선 필요

**문제**  
메서드명이 데이터 접근 중심(`get`, `findBy...`)으로 되어 있어 행위 중심의 표현이 부족함.

**개선 내용**
- 도메인 행위 중심으로 메서드명을 개선함.  
  예: `createProduct` → `registerProduct`, `deleteProduct` → `removeProduct`.

---

### 3. Service

#### 3-1. 하드코딩된 값 사용

**문제**  
코드 내부에 상수나 설정값이 직접 하드코딩되어 있음.  
수정 시 코드를 직접 찾아 변경해야 하므로 유지보수가 어렵고 확장성도 저하됨.

**개선 내용**
- 하드코딩된 값을 상수(`const val` / `static final`) 또는 설정 파일(`application.yml`)로 분리함.
- 환경별 설정이 필요한 값은 `@Value` 또는 `ConfigurationProperties`를 통해 주입받도록 개선함.

---

#### 3-2. 메서드 네이밍 개선 필요

**문제**  
`getProductById` 메서드명이 데이터 접근 중심으로 작성되어 객체지향적이지 않음.

**개선 내용**
- 메서드 명을 행위 중심으로 개선함.  
  예: `getProductById` → `findProduct`, `loadProductDetail` 등
- 메서드명만 보고도 도메인 동작 의도가 명확히 드러나도록 개선함.

---

#### 3-3. 가독성이 떨어지는 조건문

**문제**  
`!productOptional.isPresent()` 표현이 직관적이지 않아 가독성이 떨어짐.

**개선 내용**
- `productOptional.isEmpty()`로 변경하여 의미를 명확히 전달함.
- 조건문의 의도를 쉽게 이해할 수 있도록 코드 표현을 단순화함.

---

#### 3-4. Service에서 Controller로 Entity 직접 반환

**문제**  
Service 계층에서 Entity를 Controller로 직접 반환하고 있음.  
Controller는 비즈니스 로직을 처리하지 않으므로 Entity를 노출할 필요가 없음.  
Entity 노출 시 보안, 캡슐화, 유지보수 측면에서 문제가 발생할 수 있음.

**개선 내용**
- Service → Controller 간 데이터 전달 시 DTO를 사용하도록 수정함.
- DTO를 통해 필요한 데이터만 반환하여 계층 간 의존성을 줄이고 응답 구조를 명확히 유지함.

---

### 4. DTO

#### 4-1. DTO 설계 개선

**문제**  
DTO가 단순 데이터 전달용임에도 일반 class로 작성되어  
필드, 생성자, getter 등의 보일러플레이트 코드가 반복됨.

**개선 내용**
- `record`를 사용하여 데이터 전달 역할에 집중하도록 개선함.
- 불변성을 보장하여 코드의 간결성과 유지보수성을 향상시킴.

---

### 5. 성능 개선

#### 5-1. Repository에서 Entity → DTO 직접 반환

**테스트 시나리오**  
| 항목 | 설명 |
|------|------|
| 카테고리 수 | 1,000개 |
| 카테고리당 상품 수 | 10개 |
| 비교 방식 | (1) 엔티티 조회 후 DTO 변환 vs (2) JPQL로 DTO 직접 조회 |

**테스트 결과**  
| 조회 방식 | 평균 수행 시간 |
|------------|----------------|
| Entity 조회 후 DTO 변환 | 403ms |
| DTO 직접 조회 (JPQL) | 124ms |

**결론**
- DTO 직접 조회 시 약 3.2배 성능 향상.
- 대용량 조회에서는 엔티티 변환 비용을 피하기 위해 DTO Projection 전략을 사용하는 것이 유리함.

---

### 6. 종합 의견

- 도메인 간 관계 명확화, RESTful 규칙 준수, Service-Controller 계층 분리, 성능 개선을 전반적으로 달성함.
- 향후 개선 방향: 예외 처리 일관성 확보 및 `ApiResponse<T>` 기반 응답 포맷 통일.
