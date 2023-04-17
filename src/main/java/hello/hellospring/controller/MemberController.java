package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller // 스프링이 뜰때 스프링 컨테이너에 해당 컨트롤러를 넣어서 스프링 빈으로 관리된다 : 컴포넌트 스캔
public class MemberController {

    // 스프링이 관리를 하게 되면 스프링 컨테이너에서 받아서 써야 한다.
    // 이렇게 new 를 해서 사용할 경우 문제점?
    // MemberController 이외에도 주문 컨트롤러같이 MemberService를 가져다가 쓸 상황이 있을 것 이다.
    // 여러개를 new 해서 생성할 필요가 없고 하나만 생성해서 공용으로 사용하면 된다 -> 스프링 컨테이너에 스프링 빈으로 등록을 한다.
    private final MemberService memberService;

    @Autowired // 생성자에 Autowired가 있으면 MemberService를 스프링이 MemberController와 연결 시켜준다 : 연관관계
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * @Component 를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다
     * @Controller
     * @Service
     * @Repository
     */
}
