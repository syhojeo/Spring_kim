package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyJsonController {

    //json의 jackson 라이브러리에 있는 ObjectMapper를 사용한다
    //json 데이터를 파싱해서 객체에 넣는 역할을 해준다
    private ObjectMapper obejectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        //objectMapper를 이용하여 messageBody의 json값을 HelloData 객체에 넣는다
        HelloData helloData = obejectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    //@ResponseBody, @RequestBody 사용 (단순텍스트에서 사용하던 방식과 동일)
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody)
        throws IOException {

        log.info("messageBody={}", messageBody);
        //objectMapper를 이용하여 messageBody의 json값을 HelloData 객체에 넣는다
        HelloData helloData = obejectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    //@RequestBody에 객체를 넣으면 meesageBody를 ObjectMapper.readValue할 필요 없이 프로퍼티에 값을 넣는다
    //HttpMessageConverter가 대신 ObjectMapper.readValue() 를 대신해준다

    //@RequestBody를 생략시 단순타입이 아닌 객체이기 때문에 @RequestParam 대신 @ModelAttribute이 붙게되고
    //@ModelAttribute는 Json타입의 MessageBody request를 받을 수 없기 때문에
    //@RequestBody를 생략해서는 안된다
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData)
        throws IOException {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    //HttpEntity 또한 똑같이 동작한다(Json 에서의 객체 전환이 가능)
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {

        HelloData helloData = httpEntity.getBody();
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    //@ResponsBody가 HttpMessageConverter를 통해 리턴되는 값을 객체 -> Json 변환을 해준다
    //따라서 객체를 return에 넣어도 Json으로 변환해서 MessageBody에 넣어 response한다
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV53(@RequestBody HelloData data)
        throws IOException {

        log.info("username={}, age={}", data.getUsername(), data.getAge());

        return data;
    }

    /*
        HTTP Message Converter 사용과정
        @RequestBody
        Json 요청 -> HTTP Message Converter -> 객체
        @ResponseBody
        객체 -> HTTP Message Converter -> Json 요청
     */
}
