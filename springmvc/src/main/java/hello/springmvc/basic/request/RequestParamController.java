package hello.springmvc.basic.request;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    //매개변수로 @RequestParam 사용
    @ResponseBody //RestContorller와 같은 효과
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
        @RequestParam("username") String memberName,
        @RequestParam("age") int memberAge
        ) {

        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    //@RequestParam request 데이터명 지정 안하기 (단, request parameter 명과 매개 변수명이 같아야함)
    //권장
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
        @RequestParam String username,
        @RequestParam int age
    ) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //@RequestParam 생략하기 (단, request parameter 명과 매개 변수명이 같아야한다
    // + String, int, Integer와 같은 단순 타입어야한다)
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //required = true 가 default 이며
    //required = false로 설정할 경우 requestParameter로 값을 안넣어줘도 에러가 발생하지 않는다
    //required = false로 설정할 경우 int 대신 Integer를 사용해야 null을 받을 수 있다
    //만약 설정을 하지 않는다면 500예외 에러 발생
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
        @RequestParam(required = true) String username,
        @RequestParam(required = false) Integer age
    ) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /*
        defaultValue를 설정하면 int의 경우 null값이 들어오는것에 대해 컴파일에러가 안생기고
        String도 username= 과 같이 아무런값도 안넣을떄 "" 빈문자 대신 defaultValue가 들어오게 된다
        사실상 defaultValue를 설정하면 required가 의미가 없어진다
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
        @RequestParam(required = true, defaultValue = "guest") String username,
        @RequestParam(required = false, defaultValue = "-1") Integer age
    ) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam Map, MultiValueMap
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     */
    //모든 requestParameter를 Map을 통해 하나의 변수에 받을 수 있다
    //@RequestParam MultiValueMap 으로도 사용가능
    // URL?userIds=id1&userIds=id2 와 같이 하나의 key에 여러 value일때 사용
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"),
            paramMap.get("age"));
        return "ok";
    }

}
