package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository{

    /**
     * 메모리 니까 저장을 어딘가에 해두어야 한다
     * 실무에서는 동시성 문제가 있을 수 있어서 공유되는 변수 일때는 concurutansive?로 해줘야 하는데,
     * 일단은 Map으로 만들어준다
     *
     * sequence도 0,1,2 처럼 KEY값을 생성해주는 앤데, 얘도 실무에서는 long으로 해주는 것 보다는  동시성 문제를 고려해서 AUTOm long을 해줘야 하지만
     * 여기서는 단순하게 LONG으로 해주겠다
     */

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() // loop로 돌리는 것이다
                .filter(member -> member.getName().equals(name))
                .findAny(); // findAny는 하나라도 찾는건데, 루프를다 돌면서 멥에서 다 돌면서 하나를 찾으면 걔를 반환하는거고, 없으면 옵셔널에 NULL이 반환된다.
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }


    public void clearStore() {
        store.clear();
    }
    /**
     * Show Context Actions : option + enter -> implement methods
     */
}
