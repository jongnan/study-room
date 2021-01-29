# 3장. 데이터로 작업하기

> 해당 글은 크레이그 월즈님이 쓰고, 심재철님이 옮긴 Spring in Action 제 5판을 정리한 내용임을 미리 알립니다.

### 👨🏽‍💻 배우는 내용

* 스프링 JdbcTemaplate 사용하기
* SimpleJdbcInsert를 사용해서 데이터 추가하기
* 스프링 데이터를 사용해서 JPA 선언하고 사용하기

## 3.1 JDBC를 사용해서 데이터 읽고 쓰기

수십 년간 관계형 데이터 베이스, SQL은 데이터 퍼시스턴스의 최우선 선택으로 자리를 지켜왔고 지금 또한 최선의 방법중 하나이다. 이러한 관계형 데이터 베이스를 사용하는 여러 방법중에 많이 사용하는 것은 JDBC와 JPA가 존재하고 스프링에서는 이 두가지 모두 지원한다.

### JdbcTemplate

JDBC 지원의 경우 JdbcTemplate 클래스의 기반을 둔다. JdbcTemplate는 JDBC를 사용할 때 요구되는 형식적인 코드들(데이터 베이스 연결 생성, 명령문, 결과 세트, 클린업)을 줄여주고 생산성을 높게 해준다. 

이 JdbcTemplate를 사용하기 위해서는 JDBC 스타터를 의존성에 추가해 주자.

```java
//build.gradle
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
```

`@Repository` 애노테이션을 사용하여 해당 클래스가 레포지토리란 것을 알린다. 이는 `@Controller`와 `@Component` 외에 몇 안 되는 스테레오 타입 애노테이션중 하나이다. 이렇게 지정된 클래스의 경우, 스프링 컴포넌트 검색에서 자동으로 찾아 빈으로 생성해준다.

```java
@Repository
@RequiredArgsConstructor
public class JdbcOOORepository {
	private final JdbcTemplate jdbc;
  
  ...
}
```

JdbcTemplate에는 굉장히 많은 메서드들이 존재하고 이를 통해 DB에서 데이터를 조회하거나 수정, 삭제할 수 있다.

#### 책에서의 오류 사항

TacoRepository 클래스에서 타코를 저장하고 그에 대한 Id를 가져오는 부분이 있다. 여기서 `PreparedStatementCreator` 객체와 `KeyHolder` 객체를 사용하여 `update()` 메서드를 실행하고 있다. 

```java
private long saveTacoInfo(Taco taco) {
  taco.setCreatedAt(new Date());
  PreparedStatementCreator pcs = 
    new PreparedStatementCretorFactory("select ....",
                                       Types.VARCHAR,
                                       Types.TIMESTAMP)
    .newPreparedStatementCreator(
    		Array.asList(taco.getname(), 
                     new Timestamp(taco.getCreatedAt().getTime())
    		)
  	);
  KeyHodler keyHolder = new GeneratedKeyHolder();
  jdbc.update(pcs, keyHolder);
  return keyHolder.getKey().longValue();
}
```

마지막 반환 부분에서 계속해서 `nullPointException`이 발생하였다. 이를 찾아본 결과, 중간에 `PreparedStatementCreatorFactory` 객체를 생성하고 `PreparedStatementCreator` 로 만드는 부분이 있는데 중간에 `returnGeneratedKeys`를 `true`로 설정하는 코드를 넣어야 한다.(기본적으로 `false`로 되어있어 키를 생성하지 않아 `null`로 나온것으로 추정)

따라서 수정된 코드는 다음과 같다.([참고](https://stackoverflow.com/questions/53655693/keyholder-getkey-return-null))

```java
private long saveTacoInfo(Taco taco) {
  taco.setCreatedAt(new Date());
  
  PreparedStatementCretorFactory factory = 
    new PreparedStatementCretorFactory("select ....", Types.VARCHAR, Types.TIMESTAMP);
  factory.setReturnGeneratedKeys(true);
  
  PreparedStatementCreator pcs = factory.newPreparedStatementCreator(
    		Array.asList(taco.getname(),new Timestamp(taco.getCreatedAt().getTime())));
  
  KeyHodler keyHolder = new GeneratedKeyHolder();
  jdbc.update(pcs, keyHolder);
  return keyHolder.getKey().longValue();
}
```

### SimpleJdbcInsert

JdbcTemplate를 사용하여 DB에 데이터를 저장하고 그 저장된 정보의 ID를 가져오려면 KeyHolder와 PreparedStatementCreator를 사용하여 얻어야만 한다. 이러한 복잡한 과정을 SimpleJdbcInsert(JdbcTemplate를 래핑한 객체)

<br>

## 3.2 스프링 데이터 JPA를 사용해서 데이터 저장하고 사용하기

스프링 데이터 프로젝트의 경우, 여러 개의 하위 프로젝트로 구성되어있으며 이는 다양한 데이터베이스 유형을 사용한 데이터 퍼시스턴스에 초점을 맞추고 있다. 가장 많이 알려진 스프링 데이터 프로젝트는 다음과 같다.

* 스프링 데이터 JPA
* 스프링 데이터 MongoDB
* 스프링 데이터 Neo4
* 스프링 데이터 레디스
* 스프링 데이터 카산드라

이러한 스프링 데이터에서는 레퍼지토리 인터페이스를 기반으로 이를 구현하는 레포지토리를 자동으로 생성한다.

### 스프링 데이터 JPA

우리는 위에 설명한 것 중에 JPA를 사용해야 하기에 JPA 스타터를 디펜던시에 추가한다.

```java
dependencies {
	api(project(":spring-boot-project:spring-boot-starters:spring-boot-starter-aop"))
	api(project(":spring-boot-project:spring-boot-starters:spring-boot-starter-jdbc"))
	api("jakarta.transaction:jakarta.transaction-api")
	api("jakarta.persistence:jakarta.persistence-api")
	api("org.hibernate:hibernate-core") {
		exclude group: "javax.activation", module: "javax.activation-api"
		exclude group: "javax.persistence", module: "javax.persistence-api"
		exclude group: "javax.xml.bind", module: "jaxb-api"
		exclude group: "org.jboss.spec.javax.transaction", module: "jboss-transaction-api_1.2_spec"
	}
	api("org.springframework.data:spring-data-jpa") {
		exclude group: "org.aspectj", module: "aspectjrt"
	}
	api("org.springframework:spring-aspects")
}
```

위는 `spring-boot-stater-data-jpa` 빌드 명세이다. 안에 보면 기본적으로 Hibernate를 사용하고 있다.

### 도메인 매핑

이제 JPA 매핑 애노테이션을 도메인 객체에 추가해보자.

```java
@Data
@RequiredArgsConstructor
@NoArgsContructor(access = AccessLevel.PROTECTED, force = ture)
@Entity
public class OOOdomain {
  @Id
  private Long id;
  ...
}
```

`@Entity`가 바로 매핑 애노테이션이며, 해당 클래스를 JPA 개체로 선언하기 위해서는 반드시 추가해야 된다. 이때, id 속성에 반드시 `@id` 애노테이션을 붙혀 고유하게 식별한다는 것을 나타내야 한다.

또 상단에 보면 `@NoArgsContructor`가 추가된 것을 볼 수 있다. 이는 JPA에서 개체(Entity)는 인자가 없는 생성자를 반드시 가져야만 한다. 이는 Hibenate 내부에서 인수가 없는 생성자를 사용하여 Java 리플렉션 API를 통해 개체를 채우기 때문이다.  자바에선 기본적으로 컴파일러가 생성자가 존재하지 않는다면 인자 없는 생성자를 생성한다. 하지만, 우리는 롬복을 통해 초기화가 필요한 변수들을 생성자를 통해 넣어주므로 인자 없는 생성자는 직접 만들어야 한다. 

여기서 `@NoArgsContructor` 를 추가하게 된다면, `@RequiredArgsConstructor` 를 다시 추가해줘야 한다. `@NoArgsContructor`에서는 외부에서 접근할 수 없도록 설정(책에서는 `private`만, 오류 발생 따라서 `protected`로 변경)해주고, final 키워드가 존재한다면 초기화가 필요하므로 `force` 속성을 `true`로 변경해준다.

### JPA 레포지토리 선언

레포지토리에서 제공하는 메서드르 명시적으로 선언한 JDBC와는 다르게 스프링 데이터에서는 `CrudRepository` 인터페이스를 상속하면 여러가지 CRUD(Create, Read, Update, Delete) 메서드들을 사용할 수 있다.

```java
public interface OOORepository extends CrudRepository<OOO, Long> {}
```

다음 코드와 같이 한 줄로 findAll 혹은 findById 등 여러가지 메서드들을 사용할 수 있다. CrudRpository는 매개변수화 타입이며, **첫 번째 매개변수에는 레포지토리에 저장되는 개체 타입, 두 번째 매개변수는 개체의 ID 타입**이다.

### JPA 레포지토리 커스터마이징

CrudRpository에서 제공하는 기본적인 메소드 외에 추가하여 다른 메서드가 필요하다면, 다음과 같이 선언하면 쉽게 해결된다.

```java
List<Order> findByDeleveryZip(String deliveryZip);
```

이는 특정 우편번호로 배달된 주문들을 가져와야 할 때 사용할 수 있는 메서드이다. 이렇게 스프링 데이터는 **해당 레포지토리 인터페이스에 정의된 메서드들을 찾아서 이름을 분석하고 용도가 무엇인지 파악**한다. 위의 메서드말고도 더욱 복잡한 이름을 가진 메소드도 처리가 가능하다. 메서드의 이름은 [동사 + 생략 가능한 처리 대상 + By + 서술어]로 구성된다.

만약 이로도 처리가 되지 않는 경우에는 `@query` 애노테이션을 통해서 직접 쿼리를 짤 수 있다. 아래 코드는 시애틀에 배달된 모든 주문을 요청하는 쿼리이다.

```java
@Query("Order o where o.deliveryCity='Seattle'")
List<Order> readOrdersDeliveredInSeattle();
```

