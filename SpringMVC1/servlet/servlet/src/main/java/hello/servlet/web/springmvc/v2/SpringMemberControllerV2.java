package hello.servlet.web.springmvc.v2;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
//공통된 논리 경로는 클래스위의 @RequestMapping을 통하여 반복되는 경로 설정을 줄일 수 있다
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        //Map에서 값을 꺼내 사용
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //이후 필요한 값인 viewName(논리경로)와 view에서 사용해야할 정보를 model에 담아 ModelView 리턴
        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);

        return mv;

    }

    //"/springmvc/v2/members"
    @RequestMapping
    public ModelAndView members() {
        List<Member> members = memberRepository.findAll();
        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);

        return mv;
    }

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        return new ModelAndView("new-form"); //논리 경로 보내기
    }
}
