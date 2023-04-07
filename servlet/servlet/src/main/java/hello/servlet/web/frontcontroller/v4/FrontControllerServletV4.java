package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    기존 V3 구조에서 frontController 의 model 객체 생성해서 사용하는것만 추가됨
    => frontController에서 model 객체 관리
    그것만으로 소스코드가 매우 줄어든다
 */

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //ParamMap 넘기기
        //request의 값을 paramMap에 담아 controller에서 사용할 수 있게 해주는 Map 자료구조 생성
        Map<String, String> paramMap = createParamMap(request);
        //모든 requset의 parameter가 담긴 Map을 컨트롤러에 전달 -> 컨트롤러에서 HttpServletRequest를 사용할 필요 x
        Map<String, Object> model = new HashMap<>(); //frontController 에서 model 객체 생성해서 사용만 추가됨
        String viewName = controller.process(paramMap, model);

        //viewResolver를 통해 view의 절대 경로 받기
        MyView view = viewResolver(viewName);

        //view 객체의 render를 통해 Controller로 부터 전달받은 Model의 값을
        //request.setAttribute()를 통해 request객체에 넣은 후
        //dispatcher를 통해 foward 시킨다
        view.render(model, request, response);
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
