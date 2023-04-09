package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import java.util.List;
import org.apache.coyote.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/*
    실용적인 방식의 spring mvc -> ModelAndView를 반환하지 않는 방식
    1. String 형식의 논리 경로 반환(ModelAndView 반환 대신 String으로 논리경로만 반환한다)
    2. @RequestParam 을 이용하여 파라미터값만 받아오기 떄문에
       HttpServletRequest, HttpServletResponse을 사용하지않는다
    3. Model 객체를 받아와 사용하기 때문에 ModelAndView 를 이용하지 않고, 1번의 논리경로만 반환한다
    4. method = RequestMethod.GET 을 이용하여 GET과 POST 방식 구분하기
        => 대신 @GetMapping, @PostMapping 사용

    굉장히 심플하고 보기 좋은 Spring mvc 구조 완성!
 */
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @PostMapping("/save")
    public String save(
        @RequestParam("username") String username,
        @RequestParam("age") int age,
        Model model
    ) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        //model을 이용하여 View 에서 사용할 값을 저장한다 (HttpServletRequest 대용)
        model.addAttribute("member", member);
        return "save-result";
    }

    //"/springmvc/v2/members"
    @GetMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();
        ModelAndView mv = new ModelAndView("members");

        model.addAttribute("members", members);
        return "members";
    }

    @GetMapping("/new-form")
    public String newForm() {
        return "new-form"; //논리 경로 보내기
    }

}
