package hello.springmvc.basic.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        //inputStream을 이용한 요청 메시지 바디 내용 담기
        ServletInputStream inputStream = request.getInputStream();
        //Stream은 byte코드이기 때문에 항상 charset을 설정해줘야한다
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        //outputStream으로 Writer사용
        response.getWriter().write("ok");
    }

    //매개변수로 직접 inputStream을 받을 수 있다 (read)
    //outputStream은 Writer사용 (write)
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter)
        throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

    /*
        HttpEntity 이용하기
        (HttpMessageConverter가 알아서 v1, v2에서 했던 messageBody의 stream을 읽어오고 String으로 변환하는걸 해줌)
        HttpEntity: Http header, body 정보를 편리하게 조회

        요청파라미터 (@RequestParam, @ModelAttribute)와는 전혀 상관없다
        => 요청파라미터는 GET 방식에서의 쿼리스트링, HTML Form 으로 전송되는 값만을 요청 파라미터라고 한다
        때문에 HTTP API를 통해 보내지는 HttpHeader 나 body정보는 @RequestParam, @ModelAttribute를 사용할수 없다
        HttpEntity 사용할것

        HttpEntity는 응답에도 사용가능
        - 메시지 바디 정보 직접 반환
        - 헤더 정보 포함 가능
        - view 조회를 하지 않고 @RestController의 리턴값과 같이 바디에 직접 정보를 넣어서 보낸다
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity requestBodyStringV3(HttpEntity<String> httpEntity)
        throws IOException {

        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        return new HttpEntity<>("ok");
    }

    //HttpEntity를 RequestEntity와 ResponseEntity 두개로 나눠서 사용 가능하다
    /*@PostMapping("/request-body-string-v3")
    public HttpEntity requestBodyStringV3(RequestEntity<String> httpEntity)
        throws IOException {

        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        //ResponseEntity의 경우 상태코드를 넣을 수 있다
        return new ResponseEntity("ok", HttpStatus.CREATED);
    }*/

    //가장 많이 쓰는 방법
    //단 Header 정보가 필요하다면 HttpEntity를 사용하거나 @RequestHeader를 통해 원하는 header값을 받으면 된다
    @ResponseBody //view를 사용하지 않고 String 그대로 respose messageBody에 값을 넣는다
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody)
        throws IOException {

        log.info("messageBody={}", messageBody);
        return "ok";
    }
}
