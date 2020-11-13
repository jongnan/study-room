# URL에서 요청 정보 얻기 feat. Spring

> 클라이언트에서 서버로 요청을 보낼 때, URL에 정보를 담거나 Body 내부에 정보를 담는다는 것을 알 것이다.  
> 두 방식 다 URL 혹은 Body를 파싱하여 정보를 습득하고 원하는 로직을 수행하여야 한다.
>
> 이번 글에서는 URL에 담긴 정보를 Spring에서는 어떻게 처리해야 되는지 알아보도록 하자.

<br>

### URL에 포함되어 넘겨온 정보

보통 URL에 정보를 포함할 때는 두가지 형식으로 넘어올 수 있다.

1. `localhost:8080/api/users?id=jongnan`

   > Query String으로 데이터를 전달하는 형태

2. `localhost:8080/api/users/jongnan`

   > URL Path에 데이터를 전달하는 형태

직접 URL을 파싱하는 작업을 해도 되지만, 이미 다른 개발자들이 잘 만들어 놓은 라이브러리가 존재하니 그것을 사용하는 것이 더 생산성과 정신 건강에 좋을것이다.

이제 저 두가지 형태의 URL을 어떻게 변수나 객체에 매핑할 것인지 알아보도록 하자.

<br>

### @RequestParam

> Spring에서는 `@RequestParam`이라는 어노테이션을 제공한다.  
> 이는 URL의 쿼리 스트링에 존재하는 값과 변수에 매핑을 도와주는 역할을 한다.

#### # 간단하게 사용해 보기

```java
@GetMapping("/users")
public String getParam(@RequestParam String id) {
  return "ID : " + id;
}
//localhost:8080/users?id=jongnan 요청!
//ID : jongnan
```

다음과 같은 메소드가 컨트롤러에 존재할 때, `localhost:8080/users?id=jongnan`이란 요청이 들어온다면 메소드 인자로 설정되어있는 `String id` 에 값이 매핑이 되어 사용이 가능하다.

#### # 1개 이상의 값들 매핑하기

다음으로 여러개를 넘겼을 경우에는 어떻게 사용할까?
단순하게 매핑할 인자들을 늘려주고 `@RequestParam` 어노테이션을 붙혀주기만 해도 된다.

```java
@GetMapping("/users")
public String getParam(@RequestParam String id, @RequestParam String name) {
  return "ID : " + id + ", NAME : " + name;
}
//localhost:8080/users?id=jongnan&name=종난 요청!
//ID : jongnan, NAME : 종난
```

하지만 `/users?uId=jongnan&uName=종난`과 같이 자동 매핑이 될 값들과 이름이 완전 다르다면 어떻게 될까?  
예상한대로 에러 페이지를 볼 수 있다.

#### # 다른 이름으로 매핑하기

그렇다면 다른 이름으로는 매핑이 불가능할까?  
역시 똑똑한 분들이 만든만큼 그정도 기능은 당연히 존재한다.  
`@RequestParam(name="id")` 와 같이 name이란 어트리뷰트를 주면 특정 이름과 매핑을 시킬 수 있다.

```java
@GetMapping("/users")
public String getParam(@RequestParam(name="id") String uId, 
                       @RequestParam(name="name") String uName) {
  return "ID : " + uId + ", NAME : " + uName;
}
//localhost:8080/users?id=jongnan&name=종난 요청!
//ID : jongnan, NAME : 종난
```

이 외에도 `@RequestParam(value="id")` 혹은 `@RequestParam("id")` 와 같이 표현이 가능하다.

#### # Null 값 받기

또 궁금증이 발생할 수 있다.  
`id` 그리고 `name` 중에 하나의 값이라도 파라미터로 제공되지 않았을 경우엔 어떻게 될까?  
이 때도 똑같이 에러 페이지가 나타나는 것을 볼 수 있다.  
이 때를 대비하여 `required` 어트리뷰트를 설정하는 방법이 존재한다.

```java
@GetMapping("/users")
public String getParam(@RequestParam(required=false) String id, 
                       @RequestParam String name) {
  return "ID : " + id + ", NAME : " + name;
}
//localhost:8080/users?name=종난 요청!
//ID : null, NAME : 종난
```

위와 같이 `required=false` 로 설정하고 `id` 파라미터를 주지 않는다면 `null` 값으로 자동 매핑이 된다.

#### # 기본 값 주기

해당 파라미터가 주어지지 않았을 경우 `null` 말고 기본 값을 주고 싶을 때는 `defaultValue`란 어트리뷰트를 설정하면 된다.

```java
@GetMapping("/users")
public String getParam(@RequestParam(defaultValue=jong) String id, 
                       @RequestParam String name) {
  return "ID : " + id + ", NAME : " + name;
}
//localhost:8080/users?name=종난 요청!
//ID : jong, NAME : 종난
```

#### # 여러 파라미터를 하나의 객체로 매핑하기

여러개의 파라미터를 보면 뭔가 하나로 줄이고 싶은 생각이 들지 않는가..?  
하나로 줄여 조금 더 깔끔한 모습으로 나타내고 싶은 욕심이 생길 수 있다.  
이 때는 컬렉션즈에서 제공되는 `Map` 과 `List` 를 사용하여 여러개의 파라미터를 하나의 객체로 매핑시킬 수 있다.

* **Map**

  Key-Value 형태로 이루어진 구조로 파라미터들에서 `=`를 기준으로 앞은 Key로 뒤는 Value로 자동으로 매핑된다.

  ```java
  @GetMapping("/users")
  public String getParam(@RequestParam Map<String, String> params) {
    return params.toString();
  }
  //localhost:8080/users?name=jongnan&name=종난 요청!
  //{name=jongnan}
  ```
  
URL에서 `?name=jongnan&name=종난`과 같이 파라미터가 중복으로 표현이 가능한데 Map을 사용하면 이를 다 표시하지 못한다.  
  또한 Map을 사용해 중복된 Key가 들어오면 기존 Value가 새로운 Value로 치환되는 특성을 가지고 있지만, <u>위 와 같은 쿼리 스트링이 주어졌을 때 뒤에 온 `종난`으로 치환 될 것 같지만 `name`은 `jongnan`으로 바뀌지 않는다.</u>
  
* **List**

  위에 중복된 파라미터가 들어온다면 다 받지 못하는 단점이 있었지만 List의 경우 이를 커버할 수 있다.  
  대신 리스트는 한가지 종류의 파라미터만 받을 수 있다.

  ```java
  @GetMapping("/users")
  public String getParam(@RequestParam List<String> name) {
    return name.toString();
  }
  //localhost:8080/users?name=jongnan&name=종난 요청!
  //[jongnan,종난]
  ```

* **MultiValueMap**

  Spring에서 제공하는 MultiValueMap은 Map의 특성과 List의 특성이 둘 다 들어가 있다.  
  즉, Value가 List 형태로 제공되고 여러개의 파라미터 종류를 커버 할 수 있다.

  ```java
  @GetMapping("/users")
  public String getParam(@RequestParam MultiValueMap<String, String> params) {
    return params.toString();
  }
  //localhost:8080/users?id=whdsksdl&name=jongnan&name=종난 요청!
  //{id=[whdsksdl],name=[jongnan,종난]}
  ```

<br>

### @PathVariable

> Spring에서는 `@PathVariable` 이란 어노테이션을 사용하여 Path를 동적으로 받아 다르게 처리할 수 있다.  
> 위에 설명한 `@RequestParam`과 대부분 동일한 기능을 제공한다.

#### # 간단하게 사용해 보기

```java
@GetMapping("/users/{id}")
public String getParam(@PathVariable String id) {
  return "ID : " + id;
}
//http://localhost:8080/users/jongnan 요청!
//ID : jongnan
```

URL Path에 이름을 `{OOO}` 로 설정한다면, `@PathVariable` 이 붙은 변수의 이름을 똑같이 설정했을 때 자동으로 매핑이 되어 바로 사용할 수 있다.

여기서 `OOO` 과 다른 이름으로 매핑을 하고 싶다면, 다음과 같이 할 수 있다.

```java
@GetMapping("/users/{id}")
public String getParam(@PathVariable("id") String userId) {
  return "ID : " + userId;
}
```

#### # 1개 이상의 파라미터 매핑하기

`@PathVarialbe` 은 `@RequestParam`과 똑같은 방식으로 여러개의 Path의 값과 매핑시킬 수 있다. 

```java
@GetMapping("/users/{id}/{name}")
public String getParam(@PathVariable String id, @PathVariable String name) {
  return "ID : " + id + " NAME : " + name;
}
//http://localhost:8080/users/jongnan/김종난 요청!
//ID : jongnan NAME : 김종난
```

또한 `Map`을 사용해서 하나의 객체에 매핑도 가능하다.

```java
@GetMapping("/users/{id}/{name}")
public String getParam(@PathVariable Map<String, String> params) {
  return "ID : " + params.get("id") + " NAME : " + params.get("name");
}
```

#### # Null값 받기

Path에 아무런 값이 존재하지 않는다면 에러 페이지가 뜨거나 원하지 않는 페이지로 매핑이 될 수 있다.  
따라서 `@PathVariable` 어노테이션의 `required` 어트리뷰트를 사용하여 Null 값을 받아 처리할 수 도 있다.

```java
@GetMapping(value = { "/users", "/users/{id}"})
public String getParam(@PathVariable(required = false) String id) {
  if(id == null) return "ID miss...";
  return "ID : " + id;
}
```

하지만 주의해야할 점은 메소드를 매핑하는데 있어 2개의 URL에 모두 매핑을 시켜두었다.  
이는 당연하게 `id` Path가 존재하지 않게 된다면 `localhost:8080/users`로 요청을 하게 된다.  
`Handler Mapping`은 당연히 `"/users" ` 를 찾게 되고 `"/users/{id}"` 만 존재한다면 매핑된 메소드가 없기 때문에 에러 페이지를 보여준다.  
따라서 위의 예제와 같이 옵셔널하게 URL을 처리하고 싶다면 URL을 함께 써준다.

#### # 기본값 설정하기

`@PathVariable`에서는 기본값을 설정하는 어트리뷰트는 존재하지 않는다.  
따라서 위에 Null을 처리하는 방식에서 기본 값을 설정하는 로직을 추가해 기본값을 설정할 수 있다.

```java
@GetMapping(value = { "/users", "/users/{id}"})
public String getParam(@PathVariable(required = false) String id) {
  if(id == null) return "ID : Default ID";
  return "ID : " + id;
}
```