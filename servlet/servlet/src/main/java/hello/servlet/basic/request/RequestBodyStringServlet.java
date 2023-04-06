package hello.servlet.basic.request;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

/*
    postman을 이용한 HTTP API 요청 정보 받기
    body는 raw로 보낼것
 */

@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        //메시지 바디의 내용을 byte코드로 바로 얻기
        ServletInputStream inputStream = request.getInputStream();
        //byte 코드를 String으로 변환 (Spring에서 제공하는 유틸리티 객체 사용)
        //byte 변환시 어떤 encoding인지 알려줘야한다 (UTF-8)
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("messageBody = " + messageBody);

        response.getWriter().write("ok");
    }
}
