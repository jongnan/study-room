![javascript](image/javascript.png)

<br>

# Javascript 동작 원리 - 런타임편

우리가 작성한 Javascript 코드는 어떻게 돌아가는 것일까?  
Javascript가 런타임에서는 어떻게 동작하는지 알아보자.

---

### 싱글 스레드와 논 블로킹

시작하기에 앞서 Javascript는 **Single Thread**, **Non-Blocking**, **Asynchronous**라는 특징을 가지고 동작한다.  
이는 Javascript 언어가 가진 고유의 특징이 아닌, 동작시 특징으로 런타임 환경(브라우저, Node.js 등)의 특징이라고 할 수 있다.

* Single Thread

  싱글 스레드란 말은 하나의 스레드로 동작을 한다는 것이며, **하나의 스택 영역과 힙 영역**을 가진다.  
  즉, 동시에 두 가지 일은 하지 못하는 것이다.

* Non-Blocking

  위에서 Javascript는 Single Thread이기 때문에 한가지의 일밖에 하지 못한다고 했다.

  만약 웹에서 한 가지 일만 한다면 어떤 일이 발생할까?
  예시로 어떤 버튼을 누르면 서버에 여러 데이터를 요청한다고 해보자.  
  '데이터 요청을 하고 응답받기'라는 한가지의 일을 하고 있기 때문에 사용자는 다른 버튼을 눌러도 아무런 일이 발생하지 않는다.  
  이렇게 어떠한 일을 할 때, 실행되던 코드가 멈추는 것을 "**Blocking**" 상태라고 한다.

  이를 방지하기 위해 여러 Javascript 런타임에서는 "**Non-Blocking**" 상태를 가능하게 하도록 지원을 하고 있다.

* Asynchronous(비동기)

  Non-Blocking과 같은 이유로 가지고 있는 특징이다.  
  우선 **동기(Synchronous) **의 의미를 알아보면, 어떤 작업이 완료될 때까지 기다리고 있다가 해당 응답을 받아온다.  
  이와는 반대로 **비동기**는 작업이 완료가 안 돼도 다른 작업을 수행할 수 있으며, 기존 작업이 완료될 시에 응답에 대한 처리를 따로 할 수 있게 된다.  

어떻게 보면 Non-Blocking과 비동기가 똑같은 일을 하고 있다고 생각할 수 있지만 다르다.  
이 부분은 동기와 Blocking까지 해서 정리를 할 예정이다.

---

### 엔진 구성

Javascript 코드가 엔진에서 어떻게 동작하게 되는지에 대해서는 따로 [Javascript 동작 원리 - 엔진편](javascript_mechanism_engine.md)에 정리해 두었다.

우리가 잘 알고 있는 Chrome의 V8 엔진에는 두 가지의 주요 구성 요소로 이루어져 있다.

* Memory Heap

  > 변수화 객체에 대한 메모리 할당이 이루어지는 곳이며, 구조화되지 않은 넓은 메모리 영역이다.

* Call Stack

  > 콜 스택은 메모리에 존재하고 있는 공간 중에 하나로, 여러 개의 함수들이 호출되고 반환되는 실행 환경에서 함수들을 기록하고 이를 추적할 수 있도록 도와주는 데이터 구조이다.
  >
  > 코드가 실행 중에 에러가 발생하면 현재 에러가 난 시점의 콜 스택을 보여준다.
  >
  > 함수를 너무 많이 호출해도 에러가 발생하는데 이를 **Stack Overflow**라고 한다.

---

### 런타임

그렇다면 Javascript는 엔진에 있는 두 가지 요소로만 모든 동작을 하는 것일까?  
예상하지만, 바로 "**아니다**"란것을 알 수 있을 것이다.

![eventloop](image/eventloop.png)

​																																		출처 : [How JavaScript works](https://blog.sessionstack.com/how-does-javascript-actually-work-part-1-b0bacc073cf)



왼쪽 사각형 안이 위에서 설명한 Javascript 엔진이며 오른쪽은 Chrome으로 브라우저(런타임 환경)이다.  
Javascript 엔진을 통해 실행되는 코드를 트래킹하는 것과 메모리 할당만 할 뿐 나머지 동작은 런타임 환경에서 하게 된다.

앞서서 말한 Javascript의 특징들을 보며 두 가지 의문점을 던질 수 있다.

1. 비동기와 Non-Blocking은 어떻게 구현되는 것인가?

   > 비동기와 Non-Blocking은 엔진에서 하는 것이 아니라 **Event Loop**이라는 특별한 구현체에서 하게 된다.  
   > Event Loop은 엔진 안에 구현이 되어있지 않고 브라우저 혹은 Node.js인 런타임 환경에 구현이 되어있으며, Event Loop에서 엔진을 사용하여 코드를 실행한다.

2. 싱글 스레드가 어디서 어떻게 돌아가는 것인가?

   > 결론부터 내리면 런타임 환경 자체가 싱글 스레드로 동작을 하게 된다.  
   >
   > Event Loop는 메인 스레드에서 실행되며 코드를 실행하거나 비동기 처리를 하는 등 전반적인 일을 모두 하고 있다.  
   > 그렇다고 하나의 스레드로 모든 작업을 하지 않는다.  
   > 만약 Event Loop가 Blocking이 될 작업(API Call, DB Read/Write)을 한다면 Worker Thread Pool에 있는 Worker Thread에 작업을 이임한다.

전반적인 동작에 있어서 Event Loop은 굉장히 중요한 역할을 하고 있다는 것을 알게 되었다.  
그렇다면 Event Loop을 간단하게 알아보자.

#### Event Loop

Event Loop은 싱글 스레드인 Javascript가 Non-Blocking과 비동기적으로 동작할 수 있도록 도와주며 엔진을 통해 코드를 실행하는 등의 굉장히 중요한 역할을 하고 있다.  
위에 그림 혹은 구글에서 찾을 수 있는 Event Loop의 모습은 굉장히 단순하게 **순환 화살표**로만 표시해놓아 단순하게 동작할 것 으로 보이지만 굉장한 착각이다.  
Event Loop은 Javascript를 효율적으로 동작시키기 위해 굉장히 복잡한 동작 과정을 하게 되는데 이번 글에서는 간단하게만 짚고 넘어가고 자세한 내용은 다음 글에 쓰도록 하겠다.

예시로 브라우저 API에 있는 setTimeout(일정 시간 후에 특정 함수를 돌려주는 비동기 메소드)을 들어보자.  
일반적인 코드를 쭉 읽어 내려가다 setTimeout을 만나게 되면, Event Loop는 이를 Blocking이 될 메소드로 간주하고 다른 스레드에 작업을 이임하게 된다.  
새로운 스레드는 해당 작업(일정 시간 세기)을 완료하면 이를 Callback Queue에 콜백 함수를 넣고 Pool에 다시 들어간다.  
Call Stack에 있는 함수들을 모두 실행한 뒤 처리할 함수가 없을 때, Event Loop은 Callback Queue에 있는 콜백 함수를 넘기게 된다.  
이후 다시 Call Stack에 있는 함수를 실행하게 된다.

이를 정리해보면 다음과 같이 나타낼 수 있다.

1. 실행이 Blocking 될 만한 작업(API Call, DB Read/Write)을 만남
2. Thread Pool에 있는 다른 Worker Thread에 해당 작업을 이임
3. Worker Thread는 해당 작업을 마치고 Callback Queue에 콜백 함수를 넣음
4. Call Stack에 쌓여있는 함수들이 없을 때까지 함수들을 실행
5. 더 실행할 함수가 없을 때, Callback Queue에서 콜백 함수를 Call Stack으로 넘김
6. 콜백 함수 실행

여기서 중요한 점은 Event Loop은 '**Call Stack과 Callback Queue를 항상 주시하고 있다**'이다.  
그래야 콜백 함수들을 그때그때 가져와 실행할 수 있기 때문이다.

하지만, 이가 정확하게 개발자가 생각한 타이밍에 함수가 실행이 안 될 수 있다.  
위에서 예를 든 setTimeout을 보면 일정 시간을 기다리다 완료되면 Callback Queue에 넣게 되는데, 만약 쌓여있는 함수들이 많다면 Queue라는 자료구조 특성상 시간이 어느 정도 밀릴 수 있으며, CPU가 작업하다 조금의 시차가 생겨날 수도 있다.  
그렇기 때문에 완벽에 가깝게 비동기 작업들을 수행하기 위해서는 Event Loop의 내부 동작 과정에 대해 자세히 알아야 할 필요가 있다.

#### Web APIs

Web APIs는 브라우저에서 자체로 지원하는 API로 DOM 조작 혹은 AJAX, setTimeout 등의 비동기 작업들을 수행할 수 있도록 도와준다.  
이는 웹에서 사용되는 API들이며, Node.js의 경우 Node.js에서 지원하는 라이브러리나 API를 사용할 수 있다.

#### Callback Queue(Task Queue)

비동기 작업들이 끝나고 콜백 함수들을 넣는 곳으로 Event Loop가 항상 이를 주시하고 있다.  
Queue라는 자료구조를 사용하고 있기 때문에 먼저 들어온 함수가 먼저 Call Stack으로 넘겨지는 동작을 한다.  
위의 그림에서 하나의 Queue로 그려졌지만, 실제로 여러개의 Queue로 구성되어있다.

