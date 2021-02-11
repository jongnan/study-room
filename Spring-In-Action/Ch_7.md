# 7장. REST 서비스 사용하기

### 🕵🏼‍♂️ 배우는 내용

* RestTemplate를 사용해서 REST API 사용
* Traverson을 사용해서 하이퍼미디어 API 이동

## 7.1 RestTemplate으로 REST 엔드포인트 사용하기

클라이언트 입장에서 REST 리소스와 상호작용을 하기 위해서는 코드가 장황해진다. 이러한 장황한 코드를 피하기 위해서 스프링에서는 **RestTemplate** 를 제공한다. JDBC를 사용할 때 번거로운 작업(커넥션 생성, 맺기, 닫기)을 JdbcTemplate에서 처리하듯 RestTemplate는 REST 리소스를 사용하는데 번거로운 작업을 처리한다.

RestTemplate는 총 41개의 메서드를 제공하나, 고유한 작업을 하는 메서드는 12개이며 나머지는 오버로딩된 것들이다.

| 메서드          | 기능 설명                                                    |
| --------------- | ------------------------------------------------------------ |
| delete          | 지정된 URL에 DELETE 요청 수행                                |
| exchange        | 지정된 메서드를 URL에 실행, 응답 바디와 연결되는 객체를 포함하는 ResponseEntity 반환 |
| excute          | 지정된 메서드를 URL에 실행, 응답 바디와 연결되는 객체를 반환 |
| getForEntity    | GET 요청을 전송, 응답 바디와 연결되는 객체를 포함하는 ResponseEntity 반환 |
| getForObject    | GET 요청을 전송, 응답 바디와 연결되는 객체를 반환            |
| headForHeaders  | HEAD 요청을 전송, 지정된 리소스 URL의 헤더를 반환            |
| optionsForAllow | OPTIONS 요청을 전송, 지정된 URL의 Allow 헤더를 반환          |
| patchForObject  | PATCH 요청을 전송, 응답 바디와 연결된 결과 객체 반환         |
| postForEntity   | URL에 데이터를 POST, 응답 바디와 연결된 객체를 포함한 ResponseEntity를 반환 |
| postForLocation | URL에 데이터를 POST, 새로 생성된 리소스의 URL을 반환         |
| postForObject   | URL에 데이터를 POST, 응답 바디와 연결된 객체를 반환          |
| put             | 리소스 데이터를 지정된 URL에 PUT                             |

### 사용 방법

RestTemplate를 사용하기 위해서는 필요한 시점에 인스턴스를 생성하거나 빈으로 선언하고 주입할 수 도 있다.

```java
// 인스턴스 생성
RestTemplate rest = new RestTemplate();

// 빈 선언
@Bean
public RestTemplate restTemplate() {
  return new RestTemplate();
}
```

생성된 인스턴스를 가지고 위에 나열된 메서드를 사용하면 된다.

<br>

## 7.2 Traverson으로 REST API 사용하기

Traverson은 **스프링 데이터 HATEOAS에 같이 제공되며, 스프링 애플리케이션에서 하이퍼미디어 API를 사용할 수 있는 솔루션**이다. 이는 자바 기반의 라이브러리이며, 유사한 기능이 있는 자바스크립트 라이브러리로부터 영감을 얻어 만들어지게 되었다.

### 사용 방법

Traverson을 사용하려면 해당 API의 기본 URI를 갖는 객체를 생성해야 한다.

```java
Traverson traverson = 
  new Traverson(URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON);
```

이처럼 Traverson에는 해당 URL만 지정하면 되며, 이후부터는 각 링크의 관계 이름으로 API를 사용한다. 또한, Traverson 생성자에는 해당 API가 HAL 스타일의 하이퍼링크를 갖는 JSON 응답을 생성하는 것을 인자로 지정할 수 있다. 이는 **수신되는 리소스 데이터 분석 방법을 Traverson이 알 수 있게하기** 위해서다.

```java
traverson.follow("ingredients").toObject(ingredientType);
```

위 코드에서 `follow()` 메서드를 호출한다면 리소스 링크의 관계 이름인 `ingredients`인 리소스로 이동 할 수 있다. 이후, `toObject()` 호출하여 해당 리소스 컨텐츠를 가져와야 한다. 이 때, 인자에는  데이터를 읽어 들이는 객체의 타입을 지정해야 한다. 