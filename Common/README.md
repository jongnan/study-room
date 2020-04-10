# Common

<br>

## REST

> `Representational State Transfer`의 약자로 우리가 잘 알고 있는 월드 와이드 웹(WWW) 같은 분산 하이퍼미디어 시스템을 위한 소프트웨어 아키텍처의 하나이며, 로이 필딩(Roy Fielding)의 2000년 박사학위 논문에서 처음으로 소개 되었다.
>
> 출처 : [위키 백과](https://ko.wikipedia.org/wiki/REST) 

<br>

#### REST?

우리가 웹 개발을 할때, 자주 사용하는 용어인 REST API의 REST가 위에 설명한 REST이다.
REST가 과연 무엇일까?

이제 이름에서 REST가 무엇인지 힌트를 얻어보자!
Representational State Transfer를 한글로 해석하면 **대표적인 상태 전송**이다.
대표적인.. 상태..? 전혀 힌트를 얻지 못하였다...
왜 이렇게 이름을 지었을까 의문이 든다.

위키 백과에 나온 내용을 계속해서 보면 

> 엄격한 의미로 **REST**는 네트워크 아키텍처 원리의 모음이다. 여기서 '네트워크 아키텍처 원리'란 자원을 정의하고 자원에 대한 주소를 지정하는 방법 전반을 일컫는다. 간단한 의미로는, 웹 상의 자료를 [HTTP](https://ko.wikipedia.org/wiki/HTTP)위에서 [SOAP](https://ko.wikipedia.org/wiki/SOAP)이나 쿠키를 통한 세션 트랙킹 같은 별도의 전송 계층 없이 전송하기 위한 아주 간단한 인터페이스를 말한다.

라고 한다.

즉, **필요하거나 가지고있는 자원(문서, 그림 등등)을 자신만의 이름으로 정의하고, 정의된 자원에 대해 네트워크 상에서 상태를 주고 받는 행위를 하기 위한 약속**이라고 해석을 할 수 있을것 같다.

<br>

#### REST의 특징

위에 언급했듯이 REST는 **네트워크 아키텍처 원리의 모음**이라고 설명이 되어있다.
즉, **REST하게 상호간에 정보를 공유했다**라고 하는것은 **정의된 규칙들을 잘 지키며 정보를 주고 받았다**라는 것과 같다.

그렇다면 REST에서 정의된 규칙(특징)들은 무엇이 있을까?

1. **Server-Client(서버-클라이언트 구조)**

   자원을 가지고 있는 곳(Server)과 자원이 필요한 곳(Client)가 존재해야 한다.
   즉, 일관적인 인터페이스로 분리가 되어야한다.

2. **Stateless**

   요청에 있어서 클라이언트의 정보(쿠키, 세션...)가 서버에 저장되어서는 안된다.

3. **Cache**

   WWW에서와 같이 Client는 Server에서 주는 응답을 캐싱할 수 있어야 한다.

4. **Layered System**

   Client는 직접 Server와 통신하고 있는지 확인을 할 수 없다.
   즉, Client는 정해진 호출만 Server에게 하며 응답만 받을 뿐이다.
   이 때, Server측에서는 여러 개의 계층을 두어 보안, 로드밸런싱, 사용자 인증 등을 추가하여 구조상의 유연성을 가질 수 있다.

5. **Code on demand(Optional)**

   Server 측에서 코드(Javascript...)를 제공한다면, Client에서 이를 실행할 수 있어야 한다.
   해당 조건은 꼭 지켜야하는 것은 아니다.

6. **Uniform Interface**

   아키텍처를 단순화시키고 작은 단위로 분리하므로 Server와 Client가 각각 독립적으로 진화(개선)를 할 수 있도록 한다.

이렇게 REST는 6가지의 특징을 가지고 있다.

그렇다면 우리가 자주 사용했던 REST API는 이 6가지의 특징을 잘 나타내고 있을까?
1~5번까지는 우리가 사용하는 HTTP를 통해 굉장히 쉽게 지켜질 수 있다.

왜 6번은 지켜지지 않는 것인가?
6번 항목에 대해 자세히 알아보자.

<br>

#### Uniform Interface

해당 조건에 대해서는 세부 조건 4가지가 존재하고 있다.

1. Identification of resource

   > 리소스가 URI로 식별

2. Manipulation of resource through representations

   > 리소스를 생성, 수정, 추가하고자 할 때, 메시지에 표현

3. Self-descriptive messages

   > 메시지는 스스로 설명이 가능

4. HATEOAS(Hypermedia as the engine of application state)

   > 애플리케이션의 상태는 Hyperlink를 통해 전이

   

보통 REST API를 설계 할 때, 사용자 ID를 통해 정보를 얻고자 한다면 /users/{ID} 와 같은 Endpoint를 사용할 것이다.(1번)
또한, 위의 요청은 단순 정보 검색이므로 HTTP Method를 GET을 사용하여 요청을 보낸다.(2번)

위의 예시와 같이 1번과 2번 조건은 REST API에서도 잘 지켜지고 있다.
하지만, 3번과 4번은 잘 지켜지지 않고 있다.

보통 웹 페이지에서는 3번과 4번도 잘 지켜지고 있어 REST하다고 할 수 있다.
왜 API는 잘 지켜지지 않으며 웹 페이지의 경우는 잘 지켜지는 것인지 둘의 비교를 통해 알아보자.

<br>

#### "REST 하다" VS "REST 하지 않다"

**Web Page VS API**

|              | Web Page  | HTTP API  |
| ------------ | --------- | --------- |
| Protocol     | HTTP      | HTTP      |
| Comunication | 사람-기계 | 기계-기계 |
| Media Type   | HTML      | JSON      |

출처 : [그런 REST API로 괜찮은가 - 영상](https://www.youtube.com/watch?v=RP_f5dMoHFc&feature=youtu.be&t=0s)

위의 표에서 나온것처럼 Web Page, API 모두 HTTP 프로토콜을 사용하여 주고 받는다.
하지만, 웹 페이지의 경우 사람이 읽으며 API는 기계 즉, API를 요청한 클라이언트가 이를 읽어서 사용한다.
따라서 Media type 때문에 "REST하다"와 "REST 하지 않다"가 판거름 난다 라고 볼 수 있다.

그럼 이제 HTML과 JSON을 비교해보자!

**HTML VS JSON**

|                  | HTML             | JSON             |
| ---------------- | ---------------- | ---------------- |
| Hyperlink        | 가능 (a 태그)    | 정의 되어 있지 X |
| Self-descriptive | 가능 (HTML 명세) | 불완전           |

출처 : [그런 REST API로 괜찮은가 - 영상](https://www.youtube.com/watch?v=RP_f5dMoHFc&feature=youtu.be&t=0s)

예시 HTML을 보며 3번과 4번 세부 조건이 가능한 이유를 보자.

**간단한 Todo HTML**

```html
//요청
GET /todos HTTP/1.1
Host: todos.com

//응답
HTTP/1.1 200 OK
Content-Type: text/html

<html>
  <body>
    <a herf="https://todos/1">Java 공부하기</a>
    <a herf="https://todos/2">네트워크 공부하기</a>
  </body>
</html> 
```

우선 3번째 조건인 `Self-descriptive messages`이다.

1. 응답 메시지에서 Content-Type을 보고 media type이 text/html인 것을 확인 가능
2. HTTP 명세에 media type은 IANA에 등록되어있다고 명시
3. IANA에 text/html의 설명을 찾음
4. IANA에서는 text/html의 명세는 http://www.w3.org/TR/html 에 정의되어 있다고 명시
5. 해당 링크를 찾아가 명세를 해석
6. 명세에는 모든 태그의 해석 방법이 있으므로 위의 메시지만으로도 해석이 가능하다는 것을 알 수 있으므로 해당 메시지는 Self-descriptive 함

4번째 조건인 `HATEOAS`는 다음과 같다.

a 태그를 통해 링크의 이동이 가능하다.
따라서 다음 상태로 전이가 될 수 있으며, HATEOAS를 만족한다.



이번에는 JSON의 경우를 보자.

```json
//요청
GET /todos HTTP/1.1
Host: todos.com

//응답
HTTP/1.1 200 OK
Content-Type: application/json

[
  {"id": 1, "title": "Java 공부하기"},
  {"id": 2, "title": "네트워크 공부하기"}
]
```

우선 `Self-descriptive` 한지를 확인해보자.

1. 응답 메시지에서 Content-Type을 보고 media type이 application/json인 것을 확인 가능
2. HTTP 명세에 media type은 IANA에 등록되어있다고 명시
3. IANA에 application/json의 설명을 찾음
4. IANA에서는 application/json의 명세는 draft-ietf-jsonbis-rfc7159bis-04 에 정의되어 있다고 명시
5. 해당 링크를 찾아가 명세를 해석
6. 명세에 JSON 문서를 파싱하는 방법이 명시 되어 파싱은 가능하나, "id"와 "title"이 무엇을 의미하는지에 대해서는 알 방법이 없으므로 Self-descriptive 하지 않음

또한, 다음 상태로 전이할 링크가 존재하지 않으므로 `HATEOAS` 조건도 만족하지 못한다.

따라서, 우리는 현재 많이 사용하고 있는 API를 REST API라고 부르고는 있지만 실제로 REST한 API는 아니다.
그렇다면 REST API는 만들 수 없는 것인가?

<br>

#### REST API는 절대 만들지 못하는가?

위 대답은 "NO" 이며 REST한 API는 만들 수 있다.

그 방법은 다음과 같다.

**Media type 정의하기**

1. 미디어 타입 문서를 작성
   해당 문서에는 응답에 실릴 key값들이 무엇인지 의미를 정의
2. IANA에 해당 미디어 타입을 등록

위와 같은 방법을 통해 메시지를 보는 사람은 해당 명세를 찾아가 기존에 알지 못햇던 key들의 의미를 알 수 있으므로 해석이 가능하다.
따라서, 해당 메시지는 Self-descriptive하다.
하지만 다음과 같은 방법은 매번 등록을 해야하므로 굉장히 번거롭다.

**Profile 이용하기**

1. 응답에 실릴 key가 무엇인지의 의미를 정의한 명세를 작성
2. Link 헤더에 profile relation으로 해당 명세를 링크

```json
//응답
HTTP/1.1 200 OK
Content-Type: application/json
Link: <https://example.com/docs/todos>; rel="profile"
```

위와 같은 방법도 있으나, 해당 방법은 클라이언트에서 Link 헤더와 profile을 이해해야 한다는 단점이 있다.
또한, [Content negotiation](https://developer.mozilla.org/ko/docs/Web/HTTP/Content_negotiation)을 할 수 없다.

**Data에 표현**

data에 다양한 방법으로 직접 하이퍼링크를 표현한다.

```json
HTTP/1.1 200 OK
Content-Type: application/json
Link: <https://example.com/docs/todos>; rel="profile"

//방법 1 : 직접 정의
[
  {"link" : "https://example.com/todos/1", "title": "Java 공부하기"},
  {"link" : "https://example.com/todos/2", "title": "네트워크 공부하기"}
]
//방법 2 : 직접 정의
{
  "links" : {
    "todo" : "https://example.com/todos/{id}"
  },
  "data" : [{
    "id" : 1,
    "title": "Java 공부하기"
  },{
  	"id" : 2,
    "title": "네트워크 공부하기"
  }]
}

//방법 3 : JSON으로 하이퍼링크를 표현하는 방법을 정의한 명세들을 이용
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json
Link: <https://example.com/docs/todos>; rel="profile"

{
  "data" : [{
    "type" : "todo",
    "id" : "1",
    "attributes" : {"title": "Java 공부하기"},
    "links": {"self": "https://example.com/todos/1"}
  },{
    "type" : "todo",
    "id" : "2",
    "attributes" : {"title": "네트워크 공부하기"},
    "links": {"self": "https://example.com/todos/2"}
  }]
}
```

다음과 같이 직접 링크를 걸어 `HATEOAS`를 만족할 수 있다.
하지만, 방법 1과 2는 직접 정의를 해야된다는 까다로움이 존재하며, 방법 3은 기존의 API를 많이 고쳐야한다는 단점이 있다.

**HTTP 헤더**

Link, Location 등의 헤더로 링크를 표현한다.

```json
HTTP/1.1 200 OK
Content-Type: application/json
Location: /todos/1
Link: </todos/>; rel="collection"

[
  {"id": 1, "title": "Java 공부하기"}
]
```

다음과 같이 헤더에 링크를 거는 방법 또한  `HATEOAS`를 만족할 수 있다.
하지만, 표현의 한계가 있다는 단점이 존재한다.

이렇게 진짜 REST한 API를 만들 수 있다.

<br>

#### REST API 꼭 써야만 하는것인가?

위에서 진짜 REST한 API를 만드는 방법에 대해 알아보았다.
REST가 등장하고 사람들은 REST한 API가 SOAP API에 비해 굉장히 짧고 쉽다고 생각했기에 이와 같은 인기를 얻을 수 있다고 생각한다.
하지만, 이는 REST를 제작한 로이 필딩의 의도와는 다르게 사용되고 있었으며, 다른 방법에 비해 쉽게 느껴졌을 뿐인 것이다.

여기서 꼭 REST한 API만을 고수해야되는가? 라는 의문을 던질 수 있다.
될 수 있는 한 REST하다는 것이 좋을 것이다.
하지만 여기서의 포인트는 **될 수 있는 한**이다.
매번 개발을 하면서 드는 생각이지만 정해진 답은 없다.
자신의 환경에 따라 진짜 REST API를 사용해도 괜찮고, REST하진 않지만 그와 비슷한 API를 사용해도 되는것이다.
다만, REST 하지 않은 API를 REST API라고 부르진 말자.

