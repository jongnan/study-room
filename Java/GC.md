# Garbage Collection

> JVM에서 굉장히 중요한 개념인 GC에 대해 알아보도록 하자.

<br>

## Garbage Collection 이란?

가비지 컬렉션은 JAVA에서 처음 사용된 것이 아닌 LISP이란 언어에서 처음 도입이 되었다. 이러한 가비지 컬렉션을 하는 가비지 컬렉터의 주 기능은 더 이상 필요없는 객체들을 찾아 없애는 것이다.

C 혹은 C++ 에서는 메모리에 직접 접근하기에 할당 받았던 메모리를 명시적으로 해제해주어야 한다. 하지만 JAVA의 경우, OS에서 할당 받은 메모리를 JVM을 통해 간접적으로 접근한다. 이때, JVM은 가비지 컬렉터란 것을 사용하여 메모리에는 있으나 사용하지 않는 객체들을 찾아 메모리를 해제한다. 따라서 사용자는 메모리 관리를 JVM에게 이임하여 좀 더 편하게 개발을 할 수 있다. 하지만, 완벽하게 이루어지는 것은 아니며 JVM 마다 방식이 다를 수 있다.  

<br>

## Stop-the-world?

Stop-the-world란, 가비지 컬렉션을 실행하기 위해 **JVM이 애플리케이션을 일시 중단하는 것**이다. 이 작업이 발생할 시에 GC를 실행하는 스레드를 제외한 나머지 작업들은 멈추게 된다. 어떠한 GC 알고리즘을 사용해도 Stop-the-world는 발생하며, **이를 줄이는 것이 가장 큰 숙제**이다.

<br>

## GC Algorithm

가비지 컬렉터가 해당 객체가 쓰레기인지 아닌지를 `Reachable`과 `Unreachable`로 구분한다.  
이때, 이를 구분하는 방법은 `Root Set`이라고 하는 참조 사슬의 첫번째에서부터 관계가 있냐 없냐이다.

![marking Reachalbe object](https://plumbr.io/app/uploads/2015/05/Java-GC-mark-and-sweep.png)

​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)

`Root Set`은 다음과 같은 세가지 형태로 나뉜다.

1. JVM Stack 내의 `Local Variable Section`과 `Operand Stack`에서의 참조
2. `Method Area`의 `Runtime Constant Pool`에서의 참조
3. 아직 메모리에 남아있는 `Native Method`로 넘겨진 Object에서 참조

기본적인 GC의 알고리즘 흐름은 대상을 식별한 뒤 식별된 대상을 메모리에서 해제하는 것인데 이를 구현하는 알고리즘은 여러가지가 존재하며, 계속해서 진화하고 있다.

### Reference Counting Algorithm

쓰레기 객체를 찾는것에 초점을 둔 초기 알고리즘으로, 각 객체마다 `Reference Count`를 관리한다. 해당 값이 0이되면 GC를 수행되기 때문에 꼭 필요한 시기에만 돌아갈 수 있다는 장점이 있으나, 객체마다 계속해서 `Reference Count`를 변경해주어야 하며 연쇄적으로 GC가 발생할 수 있다. 또한, 순환 참조같은 구조에서는 메모리 누수가 발생할 수 있다.

<img src="https://miro.medium.com/max/1258/1*FGBQ4XLK57woaihzcrJ1og.png" width="400px">

​																									출처: [[JVM] Garbage Collection Algorithms](https://medium.com/@joongwon/jvm-garbage-collection-algorithms-3869b7b0aa6f)

### Mark-and-Sweep Algorithm

`Reference Counting` 알고리즘의 단점을 보완하기 위해 나온 알고리즘으로 위에서 언급한 `Root Set`으로부터 쓰레기 객체를 찾는다.

이름과 같이 `Mark`, `Sweep`이란 2개의 과정으로 나뉘게 된다.`Mark`에서는 살아남아야 할 객체에 Marking을 하게 되는데, 이는 객체의 헤더에 플래그 혹은 별도의 BitmapTable을 사용한다. 다음으로는 `Sweep`에서는 마킹된 객체를 지우는 작업을 한다. 모든 작업이 끝나면 모든 객체의 마킹 정보를 초기화 한다.

GC가 일어나는 동안 객체가 변경이 이러나거나 새로운 객체가 생성될 시에 오류가 발생할 수 있므로 해당 알고리즘이 실행되는 동안은 Heap의 사용이 제한된다.

![mark sweep](https://plumbr.io/app/uploads/2015/06/GC-sweep.png)

​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)



또한, 위 그림과 같이 지우는 객체와 남길 객체가 뒤죽박죽으로 되어있어 외부 단편화 현상이 발생할 수 있다.

### Mark-and-Compact Algorithm

`Mark-and-Sweep` 알고리즘의 외부 단편화 현상을 없애기 위해 나온 알고리즘으로 `Mark-and-Sweep` 알고리즘 과정에서 한가지가 추가되었다.

`Mark`,`Sweep` 과정까지는 동일하나 뒤에 `Compact` 과정이 추가 되었다. 이는 할당, 비할당으로 뒤죽박죽으로 되어있는 메모리를 할당된 객체들만 모아 메모리를 정리하는 과정이다. 예를 들면, 'window에서 디스크 조각 모음'과 같은 것은 것이다.

![mark compact](https://plumbr.io/app/uploads/2015/06/GC-mark-sweep-compact.png)

​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)



Compaction 작업이 이루어진 후에 살아남은 객체들을 모두 업데이트를 하는 작업을 해주므로 부가적인 오버헤드가 발생할 수 있다는 단점도 존재한다.

### Copying Algorithm

`Mark-and-Sweep` 알고리즘의 외부 단편화 현상을 업애기 위한 다른 방법으로 고안된 알고리즘이다.

해당 알고리즘은 Heap을 `Active`, `InActive`라는 두개의 영역으로 나누어 `Active`영역에만 객체를 할당할 수 있게끔한다. `Active`영역이 꽉차게 된다면, GC를 수행하여 살아남은 객체들만 `InActive`영역으로 Copy하는 작업을 한다. 그 후에 `Active`영역에 있는 쓰레기 객체들을 다 지우게 된다. 마지막으로는 `Active`영역과 `InActive`영역을 바꾸어 계속해서 메모리를 할당한다. 이를 `Scavenge`라고 하며 `Active`영역과 `InActive`영역은 물리적으로 구분하는 것이 아닌 논리적 구분일 뿐이다.

![](https://plumbr.io/app/uploads/2015/06/GC-mark-and-copy-in-Java.png)

​																																출처: [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)



위 그림을 보면 GC를 수행하기 전에는 A영역이 Active 영역이며, B가 InActive 영역이다. 하지만, GC를 수행한뒤의 A영역은 InActive 영역이 되고, B는 Active 영역이 된다.

해당 알고리즘도 InActive 영역에 Copy하는 과정에 있어 객체의 정보를 업데이트 해주어야하므로 오버헤드가 발생하며, 전체 Heap 영역에 반정도만 사용할 수 밖에 없다는 단점을 가지고 있다.

### Generational Algorithm

`Copying` 알고리즘을 발전시킨 형태로 현재 가장 많이 사용되고 있는 알고리즘중 하나이다. 해당 알고리즘은 다음과 같은 두가지 가설을 전재로하여 고안된 알고리즘이다.

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
  > Young 영역보다 크게 할당하며 GC는 더 적게 발생  
  > 여기서 발생하는 GC를 Major GC  

그렇다면 Old 영역에 있는 객체가 Young 영역의 객체를 참조할 경우는 어떻게 될까?

이는 Old 영역에 512Byte의 카드 테이블이 존재하여 참조가 가능해진다. 이러한 카드 테이블에는 Young 영역에 있는 객체를 참조할 때마다 정보가 표시(`write barrier`)가 되므로 Young 영역에서 GC를 실행할 때에는 Old 영역에 있는 모든 객체를 참조하지 않아도 된다는 장점도 생긴다.

<br>

## JVM 안에서의 동작 과정

여기서는 Hotspot VM을 예로들어 설명할 것이다.  

Hotspot VM은 JVM의 종류 중 하나로 Sun Micosystem사의 JVM이다. OpenJDK에서는 이 Hotspot VM을 사용하고 있으며, 많이 알려진 VM이므로 해당 VM의 garbage collection 동작을 알아보도록 하자.

### Young Area

앞서 설명한 내용에서 Young 영역은 객체가 가장 먼저 생기며, 많은 객체들이 사라지는 영역이라고 설명했다. 이러한 Young 영역은 3가지 영역으로 나눌 수 있는데, 바로 `Eden`, `Survivor`(2개)이다.

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

### Old Area

Old 영역에서 기본적으로 메모리가 꽉차게 되면 GC를 발생하게 되는데 GC의 방식에 따라 처리 절차가 달라진다. GC 방식은 JDK 7을 기준으로 5개의 방식이 존재한다.

* **Serial GC**

  Young 영역에서는 위에 설명한 것처럼 GC가 동작하고, Old 영역에서 `Mark-and-Compact` 알고리즘을 사용하며, 싱글 스레드로 GC를 진행하기 때문에 Stop-the-world 시간이 다른 GC에 비해 상대적으로 긴편이다. 따라서 적은 메모리와 CPU 코어 개수가 적을 때 적합한 방식이다.
  
* **Parallel GC**

  Serial GC와 사용하는 알고리즘은 같으나, GC를 처리하는 스레드가 하나가 아닌 여러개를 사용한다. 그렇기에 Serial GC보다 빠르게 객체를 처리할 수 있다. 해당 GC는 메모리가 충분하고 CPU 코어 개수가 많을 때 유리하다.
  
* **Parallel Old GC**

  앞서 설명한 Parallel GC와 비교하면 Old 영역의 GC만 다르다. 해당 GC는 Sweep 단계가 아닌 Summary 단계를 거치게 된다. Sweep 단계보다는 약간 더 복잡한 단계를 거친다고 생각하면 된다.
  
* **Concurrent Mark Sweep(CMS) GC**

  `Mark-and-Sweep` 알고리즘과 비슷하나 조금 더 복잡한 동작을 하는 GC이다.

  초기 Initial Mark 단계에서는 클래스 로더에서 가장 가까운 객체 중 살아 있는 객체만 찾는 것으로 끝낸다. 따라서 Stop-the-world이 너무 짧다.
  
다음으로는 Concurrent Mark 단계에서는 Initial Mark 단계에서 확인한 객체에서 참조하고 있는 객체 들을 따라 가며 확인을 하는데 해당 단계에서는 병렬로 처리가 된다.
  
Remark 단계에서는 이전 단게에서 새로 추가 되거나 참조가 끊긴 객체를 찾으며, 마지막 Concureent Sweep 단계 에서는 쓰레기 객체를 정리하는 작업을 실시한다.
  
이와 같은 단계로 진행되므로 Stop-the-world 시간이 굉장이 짧지만, 다른 GC에 비해 메모리와 CPU를 많이 사용하며 Compaction 단계가 기본적으로 제공되지 않으므로 단편화가 일어날 수 있는 단점이 있다.
  
이로 인해 CMS GC는 JDK 9 부터는 deprecated되었다.
  
* **G1 GC**

  CMS GC를 보완하기 위해 나온 GC로 대량의 힙과 멀티 프로세서 환경에서 사용되도록 만들어졌으며, JDK 7 Update 4 부터 공식적으로 도입되었다.

  G1 GC는 기존 GC들과 다른점이 있는데 바로, 메모리를 페이징 하듯 논리적인 단위로 나눠서 관리한다는 것이다.

  <img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FND99u%2Fbtqvt7WGCt0%2FIYR53llz43qHJaaGzMnixk%2Fimg.png" width="400">

  출처 : [Garbage Collection (Hotspot JVM GC)](https://lazymankook.tistory.com/83)

  위의 그림처럼 바둑판 같은 공간이 논리적 단위(Region)이다. CMS와는 달리 Compaction 단계를 진행하며 단편화 문제를 없앴으며, Stop-the-world의 시간을 예측할 수 있다는 것이 가장 큰 장점중 하나이다.

---

<br>

### Reference

*  [Garbage Collection (Hotspot JVM GC)](https://lazymankook.tistory.com/83)
*  [GC Algorithms: Basics](https://plumbr.io/handbook/garbage-collection-algorithms)
*  [[JVM] Garbage Collection Algorithms](https://medium.com/@joongwon/jvm-garbage-collection-algorithms-3869b7b0aa6f)
*  [자바 메모리 관리 - 가비지 컬렉션](https://yaboong.github.io/java/2018/06/09/java-garbage-collection/)
*  [NAVER D2 - Java Garbage Collection](https://d2.naver.com/helloworld/1329)

