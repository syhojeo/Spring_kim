package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    ModelView를 이용해
    HttpServletRequest, HttpServletResponse를 Controller에서 사용하지않고,
    viewName(논리경로) 또한 담아서 ViewResolver에서 ModelView을 사용하여 전체경로를 만들 수 있게 해준다
    (ModelView객체에 자세한 설명있음)
    
    
 */

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //ParamMap 넘기기
        //request의 값을 paramMap에 담아 controller에서 사용할 수 있게 해주는 Map 자료구조 생성
        Map<String, String> paramMap = createParamMap(request);
        //모든 requset의 parameter가 담긴 Map을 컨트롤러에 전달 -> 컨트롤러에서 HttpServletRequest를 사용할 필요 x
        ModelView mv = controller.process(paramMap);

        //논리이름 -> 실제 경로로 만들기
        String viewName = mv.getViewName(); //논리이름 new-form
        //view resolver를 통해 논리이름을 실제 경로로 만들기 new-form -> /WEB-INF/views/new-form.jsp
        //실제 경로를 이용하여 View 객체 생성
        MyView view = viewResolver(viewName);

        //view 객체의 render를 통해 Controller로 부터 전달받은 Model의 값을
        //request.setAttribute()를 통해 request객체에 넣은 후
        //dispatcher를 통해 foward 시킨다
        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    //request의 값을 paramMap에 담아 controller에서 사용할 수 있게 해주는 Map 자료구조 생성
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        //request.getParameterNames() 를 통해 모든 파라미터 정보를 가져오고
        request.getParameterNames().asIterator()
            //iterator를 돌리며 모든 파라미터 정보를 paramMap에 담는다
            .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
