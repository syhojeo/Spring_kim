package hello.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/*
    Controller의 두가지 역할
    1. Controller 내부에 @Component가 존재하기 때문에 ComponentScan의 대상이 된다
    2. RequestMappingHandlerMapping은 @RequestMapping 또는 Controller 가 있는 경우 매핑 정보로 인식한다
    즉, 해당 Controller(Handler)가 자동으로 매핑된다

    @RequestMapping
    RequestMappingHandlerMapping -> 내가 처리할 수 있는 핸들러다 라고 조회 (핸들러 Mapping성공)
    RequestMappingHandlerAdapter -> 나와 호환이 맞는 핸들러다 핸들링 해줄게 (adapter 호환 성공)
 */
@Controller
//@Controller는 밑의 두가지 어노테이션의 역할을 모두 포함한다
//@Component
//@RequestMapping
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new ModelAndView("new-form"); //논리 경로 보내기
    }
}
