package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {

        //ModelAndView : 생성시 viewName(논리경로)을 넣어주고, 
        // addObject를 통해 view 에 보낼 데이터(attributeValue)를 넣어준다
        ModelAndView mav = new ModelAndView("response/hello")
            .addObject("data", "hello!");

        return mav;
    }

    //@Controller 아래에서 String을 반환하면 view의 논리경로로 적용되어 view를 잘 찾아간다
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {

        model.addAttribute("data", "hello!");

        return "response/hello";
    }

    //view의 경로 이름을 RequestMapping의 경로 이름으로 맞춰줬을 경우 return 을 넣지 않아도 view를 찾아간다
    //@Controller를 사용해야함
    //권장 x ->  명시성이 너무 떨어지며 요청 url이 같을 가능성이 낮다
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {

        model.addAttribute("data", "hello!");
    }

    /*
        위와 같이 response할 경우에는 절대 @responseBody, @RestController, HttpEntity를사용해서는 안된다
        사용할 경우 ViewResolver를 실행하지 않고, return값을 바로 messageBody에 넣어버리기 때문에
        경로를 찾아갈 수가 없다
     */
}
