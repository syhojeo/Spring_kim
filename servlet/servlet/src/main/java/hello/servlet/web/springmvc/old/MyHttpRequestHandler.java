package hello.servlet.web.springmvc.old;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

/*
    스프링 부트가 제공하는 이전 버전의 핸들러 중 하나인 HttpRequestHandler 이다
    DispatcherServlet에서 이를 어떻게 사용하는지 확인하면 구조가 이전과 같다는것을 알 수 있다
 */

@Component("/springmvc/request-handler")
public class MyHttpRequestHandler implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
