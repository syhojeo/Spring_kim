package hello.servlet.web.springmvc.old;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/*
    최근 사용하고 있는 @Controller 어노테이션이 아닌
    과거에 스프링에서 사용하는 Controller를 가지고 핸들러메핑과 핸들러 어댑터를 알아본다

    DispatcherServlet에서 이를 어떻게 사용하는지 확인하면 구조가 이전과 같다는것을 알 수 있다
 */

//component의 이름을 클래스 명의 uri로 맞춘다
//spring 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 메핑을 사용
@Component("/springmvc/old-controller")
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}
