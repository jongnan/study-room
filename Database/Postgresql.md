# PostgreSQL

<p align="center"><img src="https://miro.medium.com/max/1400/1*iNVrmliw1DH1O3Wx2m_ggA.gif" width="800" height="300"></p>



### PostgreSQL?

PostgreSQL은 객체-관계형 데이터베이스 관리 시스템(ORDBMS)중 하나로 버클리 대학에서 개발한 Postgres(ver 4.2)를 기반으로 개발되었다.  
Postgres를 계승하며 SQL을 지원한다는 이유로 이름을 PostgreSQL로 이름을 짓게되었다고 한다.  
이러한 Postgres는 BSD(Berkeley Software Distribution) 라이센스를 가지고 있어 아무나 수정 가능하며, 수정판의 경우 개발한 사람이 공개 여부를 정할 수 있다.

<br>

### PostgreSQL 구조

![](https://severalnines.com/sites/default/files/blog/node_5122/image17.jpg)

출처 : [Understanding the PostgreSQL Architecture](https://severalnines.com/database-blog/understanding-postgresql-architecture)

Postgres의 물리적 구조는 굉장히 간단하다.  
위 그림을 보면 Shared Memory, 4개 유형의 프로세스 그리고 Data files로 이루어져 있다.

<br>

#### Shared Memory

> Shared Memory는 데이터베이스 캐싱 및 트랜잭션 로그 캐싱을 위해 예약된 메모리이며, 가장 중요한 요소는 `Shared Buffer`와 `WAL Buffer`이다.

* 공유 버퍼(Shared Buffer)

  공유 버퍼의 목적은 DISK I/O를 최소화 하는 것이다.  
  이러한 공유 버퍼는 다음과 같은 원칙을 충족해야된다.

  * 매우 큰 버퍼에 빠르게 액세스 가능
  * 많은 사용자가 동시에 액세스 할 때 경합을 최소화
  * 자주 사용되는 블록은 가능한 오랫동안 버퍼에 존재

* WAL 버퍼(Write Ahead Log Buffer)

  WAL 버퍼는 데이터베이스 변경 사항을 임시로 저장하는 버퍼이다.  
  WAL 버퍼에 저장된 내용은 임의의 시점에 WAL 파일에 기록이 되며, 백업 및 복구를 하기 위한 목적으로 사용된다.

<br>

#### 프로세스 유형

> Postgres에는 4가지의 프로세스 유형이 존재한다.
>
> 1. Postmaster (Daemon) Process
> 2. Background Process
> 3. Backend Process
> 4. Client Process

* **Postmaster Process**

  포스트 마스터 프로세스는 PostgreSQL을 시작할 때 첫 번째로 시작되는 프로세스이다.  
  시작시 복구를 수행하며 공유 메모리를 포기화하고, 백그라운드 프로세스를 실행한다.  
  또한, 클라이언트 프로세스로부터 연결 요청이 있을때 백엔드 프로세스를 작성한다. 

  ![](https://severalnines.com/sites/default/files/blog/node_5122/image2.jpg)

  출처 : [Understanding the PostgreSQL Architecture](https://severalnines.com/database-blog/understanding-postgresql-architecture)

* **Background Process**
  Postgresql 작업에 필요한 백그라운드 프로세스 목록은 다음과 같다.

  * logger 

    > 오류 메세지를 로그 파일에 기록

  * checkpointer

    > 체크 포인트가 발생하면 dirty 버퍼가 파일에 기록

  * writer

    > dirty 버퍼를 주기적으로 파일에 기록

  * wal writer

    > WAL 버퍼의 내용을 WAL 파일에 기록

  * Autovacuum launcher

    > autovacuum이 활성화 된 경우, autovacuum 작업자 프로세스를 Fork

  * archiver

    > Archive.log 모드일때, WAL 파일을 지정된 디렉토리에 복사

  * stats collector

    > 세션 실행 정보(pg_stat_activity) 및 테이블 사용 통계 정보(pg_stat_all_tables)와 같은 DBMS 사용 통계가 수집

  Vacuum..?

  > Vacuum은 다음과 같은 작업을 수행한다.
  >
  > 1. 테이블 및 인덱스 통계 수집
  >
  > 2. 테이블 재구성
  >
  > 3. 테이블 및 데드 블록 인덱스 정리
  >
  > 4. XID 랩 어라운드 방지를 위해 레코드 XID로 고정
  >
  >    1,2는 일반적으로 DBMS 관리에 필요하나 3,4번은 PostgreSQL MVCC 기능으로 필요

* **Backend Process**

  백엔드 프로세스는 사용자 프로세스의 쿼리 요청을 수행 한 뒤의 결과를 전송한다.  
  기본적으로 백엔드 프로세스는 100개가 생성되며 `max_connections`를 설정하여 변경이 가능하다.  
  쿼리 실행에는 일부 메모리 구조가 필요하며 이를 로컬 메모리라고 한다.  
  로컬 메모리와 관련된 매개 변수는 다음과 같다.

  * work_mem

    > 정렬, 비트 맵 작업, 해시 조인 및 병합 조인에 사용되는 공간  
    > 기본 4MB

  * Maintenance_work_mem

    > CREATE INDEX에 사용되는 공간  
    > 기본 64MB

  * Temp_buffers

    > 임시 테이블에 사용되는 공간  
    > 기본 8MB

* **Client Process**

  클라이언트 프로세스는 모든 백엔드 프로세스와 연결 된 백그라운드 프로세스를 말한다.  
  질의를 수행하기 위해 Client Interface Libarary를 사용한다.

<br>

### 프로세스 과정

1. 클라이언트에서 인터페이스 라이브러리(libpg, JDBC, ODBC...)를 통해 백엔드 프로세스와 연결 요청
2. Postmaster 프로세스가 해당 연결을 중계
3. 클라이언트는 할당된 프로세스와 연결을 통해 질의를 수행



<br>

<br>

<br>

### Reference

* [Understanding the PostgreSQL Architecture](https://severalnines.com/database-blog/understanding-postgresql-architecture)

