package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    /**
     * 테스트는 과감하게 한글로 바꿔도 된다
     * 프로덕션 코드가 나가는건 실제 테스트코드를 제외한 나머지 동작 코드는
     * 한글로 이름 적기가 애매한데,
     * 테스트는 영어권 사람들이랑 일하는게 아니면 한글로도 많이 적는다
     * 바로 직관적으로 알아보기 쉽기 때문에
     */

    // MemberService 과 MemberServiceTest에 있는 MemoryMemberRepository는 서로 다른 객체이기 때문에 외부에서 넣어서 @BeforeEach로 수정
    //MemberService memberService = new MemberService(new MemoryMemberRepository());
    //MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given (이런 상황이 주어졌고)
        Member member = new Member();
        member.setName("spring");

        //when (이걸 실행했을 때)
        Long saveId = memberService.join(member);

        //then (결과가 이게 나와야해)
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());

        /**
         * 이 테스트는 너무 간단하다.
         * 테스트는 정상 flow도 중요하지만 예외 flow도 중요하다
         */
    }

    /**
     * join의 핵심은 저장이 되는것도 중요하지만
     * 중복회원 검증을 거쳐서 예외가 터트려지는것도 봐야한다
     */
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
/*

        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
*/

        //then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}