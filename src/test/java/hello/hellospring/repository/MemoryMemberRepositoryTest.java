package hello.hellospring.repository;


import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 먼저 개발하고 테스트를 작성하는 것 보다
 * 테스트 클래스를 먼저 작성하고 구현 클래스를 만드는게 TDD이다.
 * 틀을 먼저 만들어놓는걸 테스트 주도 개발인 TDD라고 한다.
 *
 * 나 혼자 개발할때는 상관없는데, 여러명에서 개발을 할때나 코드가 길어지면 테스트코드 없이는 불가능하다
 */
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository =  new MemoryMemberRepository();

    /**
     * 각 테스트가 끝나고 호출될 메소드
     */
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        /**
         * 그냥 내려오는게 아니라 command + shift + enter
         */

        repository.save(member);

        /**
         * 반환타입이 뭔지 알기 위한 단축키 command + shift + v / 되돌리리면 command + Z
         */
        Member result = repository.findById(member.getId()).get();

        /**
         * new로 저장한거랑 DB에 저장한게 똑같으면 '참'
         */

        Assertions.assertEquals(result, member); // org.junit.jupiter.api    expected, actual

//        org.assertj.core.api.Assertions.assertThat(member).isEqualTo(result);
        // option + enter로 static import
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        // Rename : shift + f6
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1); // 객체가 달라서 FAILED 나옴
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }

    /**
     * 통합 테스트 할때 findByName()부분에서 에러가 난다. 그 이유는 ?
     * 전체 테스트 돌려보면 테스트 순서가 findAll()먼저 나온다
     * 순서는 보장이 안된다. 모든 테스트는 순서랑 상관없이 메소드별로 다 따로 동작하게 끔 설계를 해야한다.
     * 순서에 의존적으로 설계하면 안된다.
     * findAll()함수에서 저장된 spring1 , spring2 객체가 저장이 되서 findByName()함수에서 나와버림
     * 그래서 테스트가 하나 끝나고 나면 클리어를 해줘야 한다.
     */
}
