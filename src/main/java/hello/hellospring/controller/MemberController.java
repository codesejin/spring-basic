package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller // 스프링이 뜰때 스프링 컨테이너에 해당 컨트롤러를 넣어서 스프링 빈으로 관리된다 : 컴포넌트 스캔
public class MemberController {

    // 스프링이 관리를 하게 되면 스프링 컨테이너에서 받아서 써야 한다.
    // 이렇게 new 를 해서 사용할 경우 문제점?
    // MemberController 이외에도 주문 컨트롤러같이 MemberService를 가져다가 쓸 상황이 있을 것 이다.
    // 여러개를 new 해서 생성할 필요가 없고 하나만 생성해서 공용으로 사용하면 된다 -> 스프링 컨테이너에 스프링 빈으로 등록을 한다.
    private final MemberService memberService;

    // 생성자 주입
    @Autowired // 생성자에 Autowired 가 있으면 MemberService 를 스프링이 MemberController 와 연결 시켜준다 : 연관관계
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm memberForm) {
        Member member = new Member();
        member.setName(memberForm.getName());
        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    /*
     *  의존관계가 실행중에 동적으로 변하는 겨웅는 거의 없으므로 생성자 주입을 권장
     *
     * // 필드주입
     * @Autowired private MemberService memberService;
     *
     * // 새터주입 : public으로 열려있어야 사용가능한데, 접근제어자 public은 위험
     *     @Autowired
     *     public void setMemberService(MemberService memberService) {
     *         this.memberService = memberService;
     *     }
     */

    /**
     * @Component 를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다
     * @Controller
     * @Service
     * @Repository
     */
}
