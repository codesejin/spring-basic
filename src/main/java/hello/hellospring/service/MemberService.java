package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;

    // DI
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입 // command + x 는 cut 지우기용
     */
    public Long join(Member member) {
        // 같은 이름이 있는 중복 회원 X

        // 과거에는 If != null 이런식으로 했지만
        // optional을 통해서 Member를 감싼 덕분에 ifPresnet() 같음 함수를 사용할 수 있다

//        Optional<Member> result = memberRepository.findByName(member.getName());
//        result.ifPresent(m -> {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        });

            validateDuplicateMember(member);
            memberRepository.save(member);
            return member.getId();

        /**
         * 함수로 뽑아내는 단축키(리팩토링 기능) : control + T
         */
    }

    // 반환타입에 Optional을 보여주는건 별로 안 이쁘다
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw  new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }


    /**
     * 서비스단은 비지니스에 의존해서 설계하고
     * 리포지토리는 개발적으로 기능적인 용어를 선택한다
     *
     * ex) join 회원가입 함수 / save 저장 함수
     */

    /**
     * 비즈니스 메소드를 만들고,
     * 회원가입 시 중복회원이면 오류(Exception)가 발생하는지 검증을 해봐야하는데
     * 1. 메인 메소드를 만들기
     * 2. 컨트롤러에서 db에 넣고 테스트하기
     * 3. 테스트케이스를 만들기 ( BEST )
     */

}
