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
  - 통합 테스트가 아닌 가급적 순수한 단위테스트가 좋은 테스트일 확률이 높음