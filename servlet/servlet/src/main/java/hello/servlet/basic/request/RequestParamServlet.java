package hello.servlet.basic.request;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    1. 파라미터 전송 기능
    http://localhost:8080/request-param?username=hello&age=20

    HTTP 에 요청된 쿼리스트링을 request 객체를 이용하여 받아오기
    request.getParameter(), request.getParameterValues() 를 통한 GET 방식 쿼리스트링 요청 값 받아오기
 */
@WebServlet(name = "requestParamSevlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("[전체 파라미터 조회] - start");

        //paramName = key(username,age), request.getParameter(paramName) = value(hello,20)
        request.getParameterNames().asIterator()
            .forEachRemaining(paramName -> System.out.println(paramName + "=" + request.getParameter(paramName)));
        Enumeration<String> parameterNames = request.getParameterNames();

        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println("username = " + username);
        System.out.println("age = " + age);

        //하나의 변수명에 두개의 value를 넣었을때 (username=a&username=b)
        //만약 그냥 getParameter() 를 사용했을 경우 쿼리 스트링 순서상 가장 처음에 입력된 value가 출력된다
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username = " + name); // 쿼리스트링 순서대로 출력된다
        }

        response.getWriter().write("ok");
    }
}
