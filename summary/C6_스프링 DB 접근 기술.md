# 스프링 DB 접근 기술

- 스프링 데이터 엑세스

## H2 데이터베이스 설치
- 개발이나 테스트 용도로 가볍고 편리한 DB, 웹 화면 제공
- https://www.h2database.com/html/main.html

- 권한 주기: chmod 755 h2.sh (윈도우 사용자는 x)
- 실행: ./h2.sh (윈도우 사용자는 h2.bat)

- 데이터베이스 파일 생성 방법
- jdbc:h2:~/test (최초 한번)
- 터미널에서 ~/test.mv.db 파일 생성 확인
- 이후부터는 jdbc:h2:tcp://localhost/~/test 이렇게 접속

## 순수 JDBC
- MemoryMemberRepository에서 JdbcMemberRepository로 구현 클래스를 변경

```java
public class SpringConfig {

    private DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();
        return new JdbcMemberRepository(dataSource);
    }
}
```

## 스프링 통합 테스트
스프링 컨테이너와 DB까지 연결한 통합 테스트를 진행해보자

- test
  - @SpringBootTest: 스프링 컨테이너와 테스트를 함께 실행한다.
  - @Transactional: 테스트케이스에 이 애노테이션이 있으면, 테스트 시작 전에 트랜잭션을 시작하고, 테스트 완료 후에 항상 롤백한다. -> DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지 않는다.
- 스프링 컨테이너랑 DB까지 연결하는걸 통합테스트 라고 함
- 통합 테스트가 아닌 가급적 순수한 단위테스트가 훨씬 좋은 테스트일 확률이 높음 - 스프링 컨테이너 없이 테스트 할 수 있도록 훈련해야함


## 스프링 JdbcTemplate

- JdbcTemplate, MyBatis 같은 라이브러리는 JDBC API에서 본 반복 코드를 대부분 제거해줌
- 그러나 SQL 직접 작성해야함

```java
public class JdbcTemplateMemberRepository implements  MemberRepository{

    private final JdbcTemplate jdbcTemplate; // 디자인 패턴 중 템플릿 메서드 패턴, 콜백 사용

    // 생성자가 하나일때 autowired 안써도 됨
    public JdbcTemplateMemberRepository(DataSource dataSource) { // JdbcTemplate 주입 불가 대신 데이터소스
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result  = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }
}
```

## JPA
- 기존의 반복 코드는 물론이고, 기본적인 SQL도 JPA가 직접 만들어서 실행해준다.
- JPA를 사용하면, SQL과 데이터 중심의 설계에서 **객체 중심의 설계**로 패러다임을 전환할 수 있다. -> 개발 생산성을 크게 높일 수 있다.
- 스프링 만큼 기술적인 깊이가 있고, 스프링 만큼 넓이와 깊이가 있다.


- DB가 id를 자동으로 생성해주는것

```
@GeneratedValue(strategy = GenerationType.IDENTITY)
```
- JPA는 EntityManager를 통해서 동작한다
- 조회, 저장, 수정,삭제는 sql 을 짤 필요없다. pk 기반이 아닌 나머지 조회는 JPQL로 작성해줘야 한다
- JPA는 대이터를 저장하거나 변경할때 항상 트랜잭션을 사용해야 한다. 
```java
@Transactional
public class JpaMemberReposiotry implements MemberRepository{

    private final EntityManager em; // 내부적으로 DataSource 를 들고 있어서 DB랑 통신할 수 있다

    public JpaMemberReposiotry(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        // jpql이라는 객체지향 쿼리언어를 사용해야 한다
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
```

## 스프링 데이터 JPA

- 리포지토리에 구현 클래스 없이 인터페이스 만으로 개발을 완료 할 수 있다.
- 반복 개발해온 기본 CRUD 기능도 스프링 데이터 JPA가 모두 제공합니다.
- 인터페이스에 JpaRepository만 상속해놓으면, 스프링 데이터 JPA가 인터페이스에 대한 구현체를 만들어준다.

```java
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    Optional<Member> findByName(String name); // 구현 안해도 끝
}

```

> 실무에서는 JPA와 스프링 데이터 JPA를 기본으로 사용ㅎ고, 복잡한 동적 쿼리는 Querydsl 을 사용하면 된다.