package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional // 해당 어노테이션을 테스트케이스에 달면, 트랜잭션을 먼저 실행하고 db에 쿼리 날리고 롤백을 해준다
public class MemberServiceIntegrationTest {
    // 테스트케이스는 다른 곳에 쓸게 아니기 때문에 가장 편한 필드 주입해라
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Commit// DB에 저장
    void 회원가입() {
        //given (이런 상황이 주어졌고)
        Member member = new Member();
        member.setName("spring");

        //when (이걸 실행했을 때)
        Long saveId = memberService.join(member);

        //then (결과가 이게 나와야해)
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }


    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        //then
    }

}

