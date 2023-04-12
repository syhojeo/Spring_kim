package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import java.util.Map;

//controllerV3를 만들 인터페이스
public interface ControllerV3 {

    //servlet request or response기술이 없다
    ModelView process(Map<String, String> paramMap);
}
