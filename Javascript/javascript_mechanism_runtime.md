![javascript](image/javascript.png)

<br>

# Javascript 동작 원리 - 런타임편

시작하기 앞서  Javascript는 "<u>**Run-to-Completion**</u>"이라는 특징을 가지며 동작을 한다.  
Run-to-Completion이란, 하나의 작업이 시작되면 끝나기 전까지는 어떤 작업도 끼어들지 못하는 것을 말한다.

그렇다면 우리가 쉽게 접근 할 수 있는 웹에서 Javascript 코드를 실행하게 될 때, 다른 아무런 작업을 할 수 없을까?  
쉽게 예를 들 수 있는 [네이버 메인](https://www.naver.com/)을 보자.  
검색창에 검색어를 치는 동안 검색어 추천이 뜨며, 그 옆에서는 급상승 검색어들이 순서대로 보여지고 있다.  
하나의 일만 하는 것이 아닌 다른 작업도 동시에 일어난다.  
다른 작업이 동시에 처리가 될 수 없는데 어떻게 이런 동작을 보여주는 것일까?

---

### Single Thread

Javascript가 Run-to-Completion으로 동작하는 이유는 바로 하나의 스레드로만 실행이 되기 때문이다. 
싱글 스레드로 동작하는 방식이기에 <u>**하나의 스택 영역과 힙 영역**</u>을 가진다.

이 스택과 힙은 Javascript 엔진 안에 존재하며 스택은 **Call Stack**, 힙은 **Memory Heap** 이라고 불린다.

#### Memory Heap

변수화 객체에 대한 메모리 할당이 이루어지는 곳이며, 구조화되지 않은 넓은 메모리 영역이다.

#### Call Stack

메모리에 존재하고 있는 공간 중에 하나로, 여러 개의 함수들이 호출되고 반환되는 실행 환경에서 함수들을 기록하고 이를 추적할 수 있도록 도와주는 데이터 구조이다.

Call Stack은 이름 그대로 Stack이란 자료구조를 사용하므로 LIFO(Last In First Out)로 동작한다. 

* 동작 방식
  1. Javascript 코드 실행
  2. 함수( ex - console.log() or 사용자 정의 함수)를 만남
  3. 해당 함수 호출
  4. 함수가 Call Stack에 쌓임
  5. 또 다른 함수를 만나면 2~4번 작업이 반복
  6. 함수가 반환되면 Call Stack에서 제거

이와 같은 방식으로 동작을 하기 때문에 Javascript는 Run-to-Completion으로 동작하는 것이다.

그렇다면 <u>**동시성 문제**</u>는 어떻게 해결하는 것일까?  
이는 Javascript가 실행되는 환경을 살펴보면 알 수 있다.

---

### 런타임 환경

> 해당 글의 런타임 환경은 브라우저 기준으로 하고 있으며, 다른 런타임 환경과는 다를 수 있음을 알립니다.

![eventloop](image/runtimeEnv.png)

​																																		출처 : [How JavaScript works](https://blog.sessionstack.com/how-does-javascript-actually-work-part-1-b0bacc073cf)

위 그림은 Javascript의 런타임 환경(브라우저)을 나타낸 그림이다.  
Call Stack과 Memory Heap이 있는 것을 보면 왼쪽 사각형은 Javascript 엔진인 것을 알 수 있다.

여기서 **Event Loop**가 등장하는데 이가 동시성 문제를 해결해주는 가장 중요한 키포인트이다.  

#### Event Loop

싱글 스레드인 Javascript가 **Non-Blocking**,  **비동기**적으로 동작할 수 있도록 도와주며 엔진을 통해 코드를 실행하는 등의 많은 일을 관장하고 있다.

* Blocking / Non-Blocking

  > 코드가 실행되다 함수를 만나게 되었을 때, 해당 함수에게 제어권을 넘겨 아무런 작업을 하지 못하는 상태를 "**Blocking** 되었다." 라고 한다.
  >
  > 이와 반대로 제어권을 바로 반환 받고 다음 작업을 할 수 있는 상태를 "**Non-Blocking**"라고 한다.
* 동기(Synchronous) / 비동기(Asychronous)

  > **동기(Synchronous) **는 호출한 함수가 반환이 되었나 계속해서 신경을 쓰고 있는 것을 말한다.
  >
  > 그 반대로 **비동기(Asychronous) **는 호출한 함수가 반환이 되었는지 신경을 쓰지 않고 자신의 일만 하는 것을 말한다.  
  > 신경을 쓰지 않기 때문에 호출 되어진 함수는 자신이 끝났다라는 신호(Callback)를 주어야 한다.

위에 그림에서 Event Loop를 단순하게 표시해 놓아 단순하게 동작할 것으로 보이지만, 실제로는 Javascript를 효율적으로 동작하기 위해 굉장히 복잡한 동작 과정을 하고 있다.  
모든 동작을 설명하기에 글이 길어질 수 있으므로 여기서는 간단하게 설명하고 넘어가도록 하겠다.

* 동작 방식
  1. 엔진을 통해 코드를 수행
  2. Blocking 상태가 될만한 함수(API Call, DB Read/Write 등)를 만남
  3. Thread Pool에 존재하는 Worker Thread에 해당 작업을 이임
  4. Worker Thread는 해당 작업을 마치고 Callback Queue에 콜백 함수를 push
  5. Call Stack에 남아있는 작업 확인
  6. 남아있다면 해당 작업을 수행
  7. 남아 있지 않다면 Callback Queue 안에 있는 함수들을 순서대로 Call Stack으로 넘김

여기서 중요한 점은 **Event Loop는 <u>항상</u> Call Stack과 Callback Queue를 주시한다** 라는 것이다.  
이는 [MDN](https://developer.mozilla.org/ko/docs/Web/JavaScript/EventLoop#Event_loop) 에서 설명한 Event Loop의 코드를 보면 왜 이름에 Loop가 들어갔는지 알 수 있다.

```javascript
while(queue.waitForMessage()){
  queue.processNextMessage();
}
```

위 코드를 보면 `waitForMessage` 메소드를 통해 무한 루프를 돌며 메시지를 기다리며, 메시지가 있을 경우에는 해당 메시지를 처리한다는 것이다.  
즉, 위에서 언급한 동작 방식의 1~7번 과정이 메시지가 있을 경우이며 이가 계속해서 반복되어 지고, 메시지가 없을 때는 동기적으로 무한정 기다린다.

그렇다면 비동기로 동작한 함수가 우리가 원하는대로 동작을 할까?

**<u>당연하게도 원하는대로만 동작하지 않는다.</u>**

다시한번 동작 과정을 살펴보자.  
중간에 Call Stack에 쌓여있는 함수들이 있는지 확인을 한다.  
없다면 콜백 함수를 실행하지만, 만약 쌓여있는 함수중에 오래 걸리는 작업이 있다면 해당 콜백 함수는 언제 실행될 지 모른다.  
Call Stack 뿐만 아니라 Callback Queue에서 먼저 빠져나간 함수들 중에서도 이런 상황이 벌어질 수 도 있다.  
그렇기 때문에 우리가 최대한으로 원하는대로 동작하기 위해 Event Loop 내부 과정들을 상세히 살펴보고 이를 회피해 나갈 적절한 방법을 모색해야 한다.

#### Web APIs

Web APIs는 브라우저에서 자체로 지원하는 API로 DOM 조작 혹은 AJAX, setTimeout 등의 비동기 작업들을 수행할 수 있도록 도와준다.  
이는 웹에서 사용되는 API들이며, Node.js의 경우 Node.js에서 지원하는 라이브러리나 API를 사용할 수 있다.

#### Callback Queue(Task Queue)

비동기 작업시에는 이 Task Queue에 들어가게 되는데 이러한 Task Queue를 V8 용어로는 **Macrotask Queue** 라고 한다.  

만약 외부 스크립트를 처리하는 도중에 사용자가 마우스 이벤트를 활성화 하고, 이와 동시에 setTimeout에서 설정한 시간이 지났다고 가정해보자.  
Macrotask Queue에는 해당 작업들이 차례대로 들어오게 되며, 들어온 순서대로 처리가 된다.

그렇다면 작업이 진행되는 동안 렌더링이 일어날까?  
특정 테스크를 처리하는 동안에는 **절대 렌더링이 일어나지 않으며**, 처리가 끝나면 DOM 변경을 화면에 반영한다.

만약, 작업을 처리하는데 많은 시간이 걸린다면?  
**해당 작업이 끝날 때까지 뒤에 작업을 처리할 수 없다.**  
가끔씩 '응답 없는 페이지'라는 알람(얼럿)창이 뜨게 되는데 이가 복잡한 연산을 필요로 하거나 에러로 인해 무한 루프에 빠졌을 경우 사용자를 통해 해당 작업을 취소시킬지를 확인하는 것이다.

**<u>작업들을 처리하기 전이나 혹은 렌더링 전에 다른 작업을 넣어서 좀 더 적절하게 관리를 할 수 있지 않을까?</u>** 하는 생각에 ECMA에서는 **PromiseJobs**라는 내부 큐를 명시를 했다.  
이 내부 큐는 V8 엔진에서 **Microtask Queue** 라고 불렸으며 이가 좀 더 널리 알려져있다.

Microtask Queue는 Event Loop가 Macrotask들을 처리하기 전 혹은 후에 그리고 렌더링 전에 큐 안에 있는 모든 작업들을 처리한다.  
이러한 Microtask를 만들기 위해서는 ES6에서 새롭게 등장한 **Promise** 혹은 `queueMicrotask(func)`를 통해 만들 수 있다.

---

<br>

<br>

### Reference

* [nodejs의 내부 동작 원리](https://sjh836.tistory.com/149)
* [Javascript 동작원리 (Single thread, Event loop, Asynchronous)](https://medium.com/@vdongbin/javascript-작동원리-single-thread-event-loop-asynchronous-e47e07b24d1c)
* [javascript 동작 원리 by programmer-seo](https://velog.io/@namezin/javascript-동작-원리)
* [로우 레벨로 살펴보는 Node.js 이벤트 루프](https://evan-moon.github.io/2019/08/01/nodejs-event-loop-workflow/)
* [Don’t block the event loop! 매끄러운 경험을 위한 JavaScript 비동기 처리](https://engineering.linecorp.com/ko/blog/dont-block-the-event-loop/)
* [이벤트 루프와 매크로·마이크로태스크](https://ko.javascript.info/event-loop)

