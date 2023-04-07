package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MyHandlerAdapter {

    //해당 버전의 컨트롤러를 사용할 수 있는지 확인
    boolean supports(Object handler);

    //프론트 컨트롤러 대신 컨트롤러를 호출하는 핸들러 어댑터 메서드
    //컨트롤러와 같이 ModelView를 반환 (만약 컨트롤러가 ModelView를 반환 안한다면 만들어서라도 반환해줘야함)
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws ServletException, IOException;
}
