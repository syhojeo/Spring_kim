package hello.servlet.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

    //json 라이브러리 jackson에서 제공 (json 데이터를 getter, setter가 있는 객체에 값을 넣어준다)
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        //byte코드로 요청 메시지 바디의 내용을 받아오기
        ServletInputStream inputStream = request.getInputStream();
        //byte코드로된 요청 메시지 바디를 String으로 변환
        //byte->string 변환시 UTF_8과 같은 인코딩 타입 명시하기
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("mesageBody = " + messageBody);

        //helloData 객체에 맞게 요청 값을 읽어온다 (json-jackson 라이브러리의 ObjectMapper 사용)
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        System.out.println("helloData.username = " + helloData.getUsername());
        System.out.println("helloData.age = " + helloData.getAge());
    }
}
