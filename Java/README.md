# JAVA

> JAVA는 썬 마이크로시스템즈의 `제임스 고슬링`과 다른 연구원들이 개발한 객체 지향적 프로그래밍 언어이다.
>
> 출처 : [Java 위키 백과](https://ko.wikipedia.org/wiki/%EC%9E%90%EB%B0%94_(%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D_%EC%96%B8%EC%96%B4))



## JVM

#### JVM 이란?

Java Virtual Machine의 약자로 프로그램을 실행 시키기 위해 물리적 머신(컴퓨터)과 비슷하게 소프트웨어로 구현한 것이다.



#### JVM을 사용하는 이유?

우선 Java의 탄생 배경부터 알아야한다.

기존의 C/C++의 경우, 여러가지 하드웨어를 커버하기 위해서는 각 하드웨어에 맞게 전부 다르게 프로그래밍을 해야되는 어려움이 존재하였다.

어느 하드웨어에서나 같은 코드로 돌리기 위해서 JVM이란 것을 고안하게 되었고, 이는 JAVA의 큰 특징인 어느 플랫폼에도 종속적이지 않게 되었다.



#### JVM의 특징

* 스택 기반으로 동작하는 가상 머신
* 기본 자료형을 제외한 모든 타입을 명시적인 메모리 주소 기반의 레퍼런스가 아닌 심볼릭 레퍼런스를 통해 참조
* Garbage Collection이란 것을 사용하여 사용자에 의해 생성된 클래스 인스턴스를 자동으로 파괴
* 기본 자료형을 명확하게 정의하여 플랫폼에 종속적이지 않음을 보장
* 네트워크 바이트 오더(빅 엔디안)를 사용
  엔디엔에 알고싶다면 [여기](https://ko.wikipedia.org/wiki/엔디언) 를 참고하면 된다!



#### JVM의 구조

![jvm_structure](https://d2.naver.com/content/images/2015/06/helloworld-1230-1.png)

​																																			출처 : [JVM Internal](https://d2.naver.com/helloworld/1230)



JVM의 구조는 빨간선으로 되어있는 네모와 같이 생겼다.

일단, 우리가 코딩한 Java 파일을 `Java Compiler`가 Byte code로 컴파일을 진행한다.
Byte code(.class)로 변경하는 이유는 JVM이 해석할 수 있도록 만들기 위함이다.(기계어로 변경하는 이유와 동일)

이렇게 변경된 파일(.class)을 클래스 로더(Class Loader)가 런타임 데이터 영역(Runtime Data Areas)에 로드하며, 실행 엔진(Execution Engine)은 바이트 코드를 기계어로 변경하여 수행을 한다.



#### Class Loader

자바의 경우 컴파일타임에 클래스를 로드하고 참조하는 것이 아닌 런타임에 클래스를 로드& 참조한다. 
이를 동적 로딩이라고 하는데, JVM에서 이를 맞고 있는 부분이 바로 `Class Loader`이다.

클래스 로더는 다음과 같은 몇가지 특징을 가지고 있다.

* 클래스 로더끼리 부모-자식 관계를 이루어 계층 구조로 생성
  최상위 클래스 로더는 부트스트랩 클래스 로더(Bootstrap Class Loader) 
* 계층 구조를 이용하여 클래스 로더끼리 로드를 위임하는 식으로 동작
  * 동작 방식(클래스 로드 시)
    1. 캐시가 되어있는지 확인
    2. 없다면 상위 클래스 로더에 확인(1번 과정 동일)
    3. 2번 과정을 최상위 클래스 로더까지 실행
    4. 최상위 클래스 로더(Bootstrap Class Loader)에도 없다면 요청 받은 클래스 로더가
       파일 시스템에서 해당 클래스를 찾음
* 하위 클래스 로더는 상위 클래스 로더를 찾을 수 있지만, 상위 클래스 로더는 하위 클래스 로더를 찾을 수 없음
* 클래스 로더는 클래스를 로드 할 수 있지만, 그 반대인 언로드는 불가능



#### Class Loader 종류

![class_loader_model](https://d2.naver.com/content/images/2015/06/helloworld-1230-2.png)

​																																				출처 : [JVM Internal](https://d2.naver.com/helloworld/1230)



* 부트스트랩 클래스 로더

  > JVM을 실행할 때 생성되며, Object 클래스를 비롯하여 JAVA API를 로드
  >
  > 다른 로더와 다르게 네이티브 코드로 구현

* 익스텐션 클래스 로더(Extension Class Loader)

  > 기본 JAVA API를 제외한 확장 클래스를 로드

* 시스템 클래스 로더(System Class Loader)

  > 위 두개의 로더가 JVM 자체의 구성 요소를 로드하는 것이라면, 시스템 클래스 로더는 애플리케이션의 클래스를 로드
  >
  > 사용자가 지정한 $CLASSPATH 내의 클래스를 로드

* 사용자 정의 클래스 로더(User-Defined Class Loader)

  > 애플리케이션 사용자가 직접 코드 상에서 생성해서 사용하는 클래스 로더



#### 클래스 로드 과정

1. 로드(Loading)
   클래스를 파일에서 가져와 JVM 메모리에 로드
2. 검증(Verifying)
   읽은 클래스가 자바 언어 명세 및 JVM 명세에 명시된 것처럼 잘 구성되었는지 검사
   로드 과정중 가장 까다로운 과정, 시간을 가장 많이 소모
3. 준비(Preparing)
   클래스가 필요로 하는 메모리를 할당, 클래스에서 정의 된 필드, 메서드, 인터페이스들을 나나태는 데이터 구조 준비
4. 분석(Resolving)
   클래스의 상수 풀 내의 모든 심볼릭 레퍼런스를 다이렉트 레퍼런스로 변경
5. 초기화(Initializing)
   클래스 변수들을 적절한 값으로 초기화



#### Runtime Data Areas

![runtime_data_areas](https://d2.naver.com/content/images/2015/06/helloworld-1230-4.png)

​																																			출처 : [JVM Internal](https://d2.naver.com/helloworld/1230)



런타임 데이터 영역은 JVM이 OS 위에서 실행되며 할당받는 메모리 영역이다.
총 6개의 영역으로 나뉠 수 있는데, 이 중 PC Register, JVM Stack, Native Method Stack의 경우 스레드 마다 하나씩 생성되며 나머지(Heap, Method Area, Runtime Constant Pool)은 모든 스레드가 공유해서 사용한다.

* PC Register

  > 스레드가 시작될 때 생성되며 현재 수행중인 JVM 명령의 주소를 가지고 있음

* JVM Stack

  > 스레드가 시작될 때 생성되며 스택 프레임(Stack Frame)이라는 구조체를 저장하는 스택
  > JVM에서는 이 스택에 push, pop 동작만 수행
  >
  > * 스택 프레임
  >   JVM 내에서 메서드가 수행될 때마다 하나의 스택 프레임이 생성되며 JVM Stack에 추가
  >   메서드가 종료되면 스택 프레임은 제거
  >   스택 프레임 내에는 지역 변수 배열, 피연산자 스택 등을 가짐
  > * 지역 변수 배열
  >   0부터 시작하는 인덱스를 가진 배열
  >   0은 메서드가 속한 클래스 인스턴스의 this 레퍼런스, 그 이후로 부터는 메서드에 전달된 파라미터들이 저장
  >   파라미터 이후에는 지역 변수들이 저장
  > * 피연산자 스택
  >   메서드를 실행하는 실제 공간
  >   각 메서드는 피연산자 스택과 지역 변수 배열 사이에서 데이터를 교환
  >   다른 메서드 호출 결과를 추가(push) 하거나 가져옴(pop)

* Native Method Stack

  > 자바 이외의 언어로 작성된 네이티브 코드를 위한 스택

* Heap

  > 인스턴스 또는 객체를 저장하는 공간
  > JVM 성능에 가장 영향을 미치는 공간으로 Garbage Collection의 대상
  > 힙 구성 방식과 가비지 컬렉션 방법은 JVM 재량

* Method Area

  > JVM이 시작될 때 생성되며 JVM이 읽어들인 각각의 클래스와 인터페이스에 대한 정보들을 보관
  > 오라클의 Hotspot JVM에서는 Permanent Area, Permanent Generation이라고 불림

* Runtime Constant Pool

  > 메서드 영역에 포함되는 영역이나 JVM 동작에서 가장 핵심적인 역할을 수행하는 곳
  > 각 클래스와 인터페이스의 상수와 메서드와 필드에 대한 모든 레퍼런스까지 담고 있는 테이블
  > 즉, 메서드나 필드를 참조할 경우 JVM에서는 런타임 상수 풀을 통해 해당 메서드나 필드의 실제 메모리 주소를 찾아 참조



#### Execution Engine

JVM 내의 런타임 데이터 영역에 배치된 바이트 코드들은 실행 엔진에 의해 실행된다.
실행 엔진은 바이트 코드를 명령어 단위로 읽어 실행한다.(기계어를 하나씩 실행하는 방식과 동일)

바이트 코드의 경우, 기계어가 아니므로 실행 엔진에 의해 기계어로 변경되는데 2가지 방식이 존재한다.

* 인터프리터

  > 바이트 코드를 하나씩 읽어 해석하며 실행
  > 하나하나의 실행은 빠른대신 전체를 실행하는데에는 느리다는 단점 보유

* JIT(Just-In-Time) 컴파일러

  > 인터프리터의 단점을 보완하기위해 도입
  > 인터프리터 방식으로 컴파일을 하다 일정 시점에서 바이트 코드 전체를 컴파일
  > 변경된 네이티브 코드는 캐시에 보관되어 빠르게 수행 가능
  >
  > 하지만, 한번만 실행되는 코드라면 JIT 방식보다는 인터프리터 방식이 유리



#### Reference

* [NAVER D2 - JVM Internal](https://d2.naver.com/helloworld/1230)



## Garbage Collection



#### Garbage Collection 이란?

가비지 컬렉션은 JAVA에서 처음 사용된 것이 아닌 LISP이란 언어에서 처음 도입이 되었다.
이러한 가비지 컬렉션을 하는 가비지 컬렉터의 주 기능은 더 이상 필요없는 객체들을 찾아 없애는 것이다.

C 혹은 C++ 에서는 메모리에 직접 접근하기에 할당 받았던 메모리를 명시적으로 해제해주어야 한다.
하지만 JAVA의 경우, OS에서 할당 받은 메모리를 JVM을 통해 간접적으로 접근한다.
이때, JVM은 가비지 컬렉터란 것을 사용하여 메모리에는 있으나 사용하지 않는 객체들을 찾아 메모리를 해제한다.
따라서 사용자는 메모리 관리를 JVM에게 이임하여 좀 더 편하게 개발을 할 수 있다.
하지만, 완벽하게 이루어지는 것은 아니며 JVM 마다 방식이 다를 수 있다.



#### Stop-the-world

Stop-the-world란, 가비지 컬렉션을 실행하기 위해 JVM이 애플리케이션을 일시 중단하는 것이다.
이 작업이 발생할 시에 GC를 실행하는 스레드를 제외한 나머지 작업들은 멈추게 된다.
어떠한 GC 알고리즘을 사용해도 Stop-the-world는 발생하며, 이를 줄이는 것이 가장 큰 숙제이다.



#### GC Algorithm

가비지 컬렉터가 해당 객체가 쓰레기인지 아닌지를 `Reachable`과 `Unreachable`로 구분한다.
이때, 이를 구분하는 방법은 `Root Set`이라고 하는 참조 사슬의 첫번째에서부터 관계가 있냐 없냐이다.

![marking Reachalbe object](https://plumbr.io/app/uploads/2015/05/Java-GC-mark-and-sweep.png)

​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)



`Root Set`은 다음과 같은 세가지 형태로 나뉜다.

1. JVM Stack 내의 `Local Variable Section`과 `Operand Stack`에서의 참조
2. `Method Area`의 `Runtime Constant Pool`에서의 참조
3. 아직 메모리에 남아있는 `Native Method`로 넘겨진 Object에서 참조

기본적인 GC의 알고리즘 흐름은 대상을 식별한 뒤 식별된 대상을 메모리에서 해제하는 것인데 이를 구현하는 알고리즘은 여러가지가 존재하며, 계속해서 진화하고 있다.

* **Reference Counting Algorithm**

  쓰레기 객체를 찾는것에 초점을 둔 초기 알고리즘으로, 각 객체마다 `Reference Count`를 관리한다.
  해당 값이 0이되면 GC를 수행되기 때문에 꼭 필요한 시기에만 돌아갈 수 있다는 장점이 있으나, 객체마다 계속해서 `Reference Count`를 변경해주어야 하며 연쇄적으로 GC가 발생할 수 있다.
  또한, 순환 참조같은 구조에서는 메모리 누수가 발생할 수 있다.
  ![linked list memory leak](https://miro.medium.com/max/1258/1*FGBQ4XLK57woaihzcrJ1og.png)

  ​																									출처: [[JVM] Garbage Collection Algorithms](https://medium.com/@joongwon/jvm-garbage-collection-algorithms-3869b7b0aa6f)

  

* **Mark-and-Sweep Algorithm**

  `Reference Counting` 알고리즘의 단점을 보완하기 위해 나온 알고리즘으로 위에서 언급한 `Root Set`으로부터 쓰레기 객체를 찾는다.

  이름과 같이 `Mark`, `Sweep`이란 2개의 과정으로 나뉘게 된다.
  `Mark`에서는 살아남아야 할 객체에 Marking을 하게 되는데, 이는 객체의 헤더에 플래그 혹은 별도의 BitmapTable을 사용한다.
  다음으로는 `Sweep`에서는 마킹된 객체를 지우는 작업을 한다.
  모든 작업이 끝나면 모든 객체의 마킹 정보를 초기화 한다.

  GC가 일어나는 동안 객체가 변경이 이러나거나 새로운 객체가 생성될 시에 오류가 발생할 수 있므로 해당 알고리즘이 실행되는 동안은 Heap의 사용이 제한된다.

  ![mark sweep](https://plumbr.io/app/uploads/2015/06/GC-sweep.png)

  ​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)

  

  또한, 위 그림과 같이 지우는 객체와 남길 객체가 뒤죽박죽으로 되어있어 외부 단편화 현상이 발생할 수 있다.

* **Mark-and-Compact Algorithm**

  `Mark-and-Sweep` 알고리즘의 외부 단편화 현상을 없애기 위해 나온 알고리즘으로 `Mark-and-Sweep` 알고리즘 과정에서 한가지가 추가되었다.

  `Mark`,`Sweep` 과정까지는 동일하나 뒤에 `Compact` 과정이 추가 되었다.
  이는 할당, 비할당으로 뒤죽박죽으로 되어있는 메모리를 할당된 객체들만 모아 메모리를 정리하는 과정이다.
  예를 들면, 디스크 조각 모음과 같은 것은 것이다.

  ![mark compact](https://plumbr.io/app/uploads/2015/06/GC-mark-sweep-compact.png)

  ​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)

  

  Compaction 작업이 이루어진 후에 살아남은 객체들을 모두 업데이트를 하는 작업을 해주므로 부가적인 오버헤드가 발생할 수 있다는 단점도 존재한다.

* **Copying Algorithm**

  `Mark-and-Sweep` 알고리즘의 외부 단편화 현상을 업애기 위한 다른 방법으로 고안된 알고리즘이다.

  해당 알고리즘은 Heap을 `Active`, `InActive`라는 두개의 영역으로 나누어 `Active`영역에만 객체를 할당할 수 있게끔한다.
  `Active`영역이 꽉차게 된다면, GC를 수행하여 살아남은 객체들만 `InActive`영역으로 Copy하는 작업을 한다.
  그 후에 `Active`영역에 있는 쓰레기 객체들을 다 지우게 된다.
  마지막으로는 `Active`영역과 `InActive`영역을 바꾸어 계속해서 메모리를 할당한다.
  이를 `Scavenge`라고 하며 `Active`영역과 `InActive`영역은 물리적으로 구분하는 것이 아닌 논리적 구분일 뿐이다.

  ![](https://plumbr.io/app/uploads/2015/06/GC-mark-and-copy-in-Java.png)

  ​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)

  

  위 그림을 보면 GC를 수행하기 전에는 A영역이 Active 영역이며, B가 InActive 영역이다.
  하지만, GC를 수행한뒤의 A영역은 InActive 영역이 되고, B는 Active 영역이 된다.

  해당 알고리즘도 InActive 영역에 Copy하는 과정에 있어 객체의 정보를 업데이트 해주어야하므로 오버헤드가 발생하며, 전체 Heap 영역에 반정도만 사용할 수 밖에 없다는 단점을 가지고 있다.

* **Generational Algorithm**

  `Copying` 알고리즘을 발전시킨 형태로 현재 가장 많이 사용되고 있는 알고리즘중 하나이다.
  해당 알고리즘은 다음과 같은 두가지 가설을 전재로하여 고안된 알고리즘이다.

  * 대부분의 객체는 빠르게 쓰레기 객체가 된다.
  * 오래된 객체에서 젊은 객체로의 참조는 거의 없다.

  위 두개의 가설을 `Weak generational hypothesis`라고 한다.

  이 가설을 기반으로 HotSpot VM에서는 Heap을 `Young`과 `Old` Generation 영역으로 나눈다.

  * Young 영역

    > 새롭게 생성된 객체들은 여기에 위치
    > 대부분의 객체들은 금방 쓰레기 객체가 되므로 많은 객체들이 Young 영역에 생성되었다가 사라짐
    > 해당 영역에서 객체가 사라질 때는 Minor GC가 발생

  * Old 영역

    > 쓰레기 상태가 되지 않은 객체들이 위치
    > Young 영역보다 크게 할당하며 GC는 더 적게 발생한다.
    > 여기서 발생하는 GC를 Major GC

  그렇다면 Old 영역에 있는 객체가 Young 영역의 객체를 참조할 경우는 어떻게 될까?

  이는 Old 영역에 512Byte의 카드 테이블이 존재하여 참조가 가능해진다.
  이러한 카드 테이블에는 Young 영역에 있는 객체를 참조할 때마다 정보가 표시(`write barrier`)가 되므로 Young 영역에서 GC를 실행할 때에는 Old 영역에 있는 모든 객체를 참조하지 않아도 된다는 장점도 생긴다.



#### HotSpot VM에서의 Young Area

앞서 설명한 내용에서 Young 영역은 객체가 가장 먼저 생기며, 많은 객체들이 사라지는 영역이라고 설명했다.
이러한 Young 영역은 3가지 영역으로 나눌 수 있는데, 바로 `Eden`, `Survivor`(2개)이다.

객체가 생성되어 처리되는 절차는 다음과 같다.

1. 새로 생성된 대부분의 객체는 Eden 영역에 위치
2. Eden 영역에서 메모리가 꽉차면 GC가 일어나고 이에 살아남은 객체는 Survivor 영역중 하나로 이동
   및 Age를 기록 +1 
   (나머지 하나는 계속해서 사용 X, `Copying`알고리즘과 동일)
3. 1,2번 과정이 계속해서 일어나 Survivor 영역도 꽉차게 되면, GC가 일어나고 살아남은 객체는 다른 Survivor 영역으로 옮겨짐(GC가 일어난 Survivor 객체는 비워짐, 옮겨간 Survivor 영역에 계속해서 쌓임)
4. 1,2,3번 과정이 계속해서 반복되다 살아남은 객체는 Old 영역으로 이동
   (보통 객체의 Age가 31일때 이동하는데 이는 객체의 헤더에 Age를 기록하는 부분이 6bit)

HotSpot VM에서 보다 빠른 메모리 할당을 위해 두 가지 기술을 사용하는데 다음과 같다.

* Bump-the-pointer

  > Eden 영역에 할당된 마지막 객체를 추적
  > 다음에 생성될 객체가 있다면 마지막 객체를 보고 Eden 영역에 넣기 적당한지 파악

* TLABs(Thread-Local Allocation Buffers)

  > 멀티스레드 환경에서는 여러 스레드에서 사용되는 객체를 Eden 영역에 저장하기 위해서는 Lock이 발생
  > Lock이 발생하면 성능이 매우 떨어지나 TLABs 기술로 해결 가능
  >
  > 해결 방안은 각각의 스레드마다 Eden 영역의 작은 덩어리를 가질 수 있게 하는 것



#### HotSpot VM에서의 Old Area

Old 영역에서 기본적으로 메모리가 꽉차게 되면 GC를 발생하게 되는데 GC의 방식에 따라 처리 절차가 달라진다.
GC 방식은 JDK 7을 기준으로 5개의 방식이 존재한다.

* **Serial GC**

  Young 영역에서는 위에 설명한 것처럼 GC가 동작하고, Old 영역에서 `Mark-and-Compact` 알고리즘을 사용하며, 싱글 스레드로 GC를 진행하기 때문에 Stop-the-world 시간이 다른 GC에 비해 상대적으로 긴편이다.
  따라서 적은 메모리와 CPU 코어 개수가 적을 때 적합한 방식이다.

* **Parallel GC**

  Serial GC와 사용하는 알고리즘은 같으나, GC를 처리하는 스레드가 하나가 아닌 여러개를 사용한다.
  그렇기에 Serial GC보다 빠르게 객체를 처리할 수 있다.
  해당 GC는 메모리가 충분하고 CPU 코어 개수가 많을 때 유리하다.

* **Parallel Old GC**

  앞서 설명한 Parallel GC와 비교하면 Old 영역의 GC만 다르다.
  해당 GC는 Sweep 단계가 아닌 Summary 단계를 거치게 된다.
  Sweep 단계보다는 약간 더 복잡한 단계를 거친다고 생각하면 된다.

* **Concurrent Mark Sweep(CMS) GC**

  `Mark-and-Sweep` 알고리즘과 비슷하나 조금 더 복잡한 동작을 하는 GC이다.

  초기 Initial Mark 단계에서는 클래스 로더에서 가장 가까운 객체 중 살아 있는 객체만 찾는 것으로 끝낸다.
  따라서 Stop-the-world이 너무 짧다.

  다음으로는 Concurrent Mark 단계에서는 Initial Mark 단계에서 확인한 객체에서 참조하고 있는 객체 들을 따라 가며 확인을 하는데 해당 단계에서는 병렬로 처리가 된다.

  Remark 단계에서는 이전 단게에서 새로 추가 되거나 참조가 끊긴 객체를 찾으며, 마지막 Concureent Sweep 단계 에서는 쓰레기 객체를 정리하는 작업을 실시한다.

  이와 같은 단계로 진행되므로 Stop-the-world 시간이 굉장이 짧지만, 다른 GC에 비해 메모리와 CPU를 많이 사용하며 Compaction 단계가 기본적으로 제공되지 않으므로 단편화가 일어날 수 있는 단점이 있다.

  이로 인해 CMS GC는 JDK 9 부터는 deprecated되었다.

* **G1 GC**

  CMS GC를 보완하기 위해 나온 GC로 대량의 힙과 멀티 프로세서 환경에서 사용되도록 만들어졌으며, JDK 7 Update 4 부터 공식적으로 도입되었다.

  G1 GC는 기존 GC들과 다른점이 있는데 바로, 메모리를 페이징 하듯 논리적인 단위로 나눠서 관리한다는 것이다.
  ![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FND99u%2Fbtqvt7WGCt0%2FIYR53llz43qHJaaGzMnixk%2Fimg.png)

  ​																							출처 : [Garbage Collection (Hotspot JVM GC)](https://lazymankook.tistory.com/83)

  
  
  위의 그림처럼 바둑판 같은 공간이 논리적 단위(Region)이다.
  CMS와는 달리 Compaction 단계를 진행하며 단편화 문제를 없앴으며, Stop-the-world의 시간을 예측할 수 있다는 것이 가장 큰 장점중 하나이다.



#### Reference

*  [Garbage Collection (Hotspot JVM GC)](https://lazymankook.tistory.com/83)
*  [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)
*  [[JVM] Garbage Collection Algorithms](https://medium.com/@joongwon/jvm-garbage-collection-algorithms-3869b7b0aa6f)
* [자바 메모리 관리 - 가비지 컬렉션](https://yaboong.github.io/java/2018/06/09/java-garbage-collection/)
* [NAVER D2 - Java Garbage Collection](https://d2.naver.com/helloworld/1329)



