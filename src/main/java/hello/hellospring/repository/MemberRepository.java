package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

    /**
     * Show Context Actions : option + command + enter
     *
     * Optional은 자바 8에 들어간 기능
     * findById , findByName 로 데이터를 가져오는게 NULL 일 수 있음
     * Null을 그대로 반환하기보다 OPTIONAL 로 감싸서 반환한다
     */
}
