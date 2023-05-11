package hello.typeconverter.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
public class HelloController {

    /*
        RequestParameter는 문자타입의 데이터로 넘어오게 된다
        때문에 숫자로 사용하기 위해서는 Integer 변환이 필요하다
    */
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {

        String data = request.getParameter("data"); //문자 타입 조회
        Integer intValue = Integer.valueOf(data); //문자 타입을 숫자 타입으로 변환
        System.out.println("intValue = " + intValue);

        return "ok";
    }

    /*
        @RequestParam 을 이용한 스프링의 문자 -> 숫자 자동 변환
     */
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }
}
