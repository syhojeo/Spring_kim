package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    RestController
    return 하는 String이 Controller의 경우 view Resolver를 위한 논리경로를 리턴하지만
    RestController의 경우 Http messageBody 부분에 return 하는 String(OK) 그대로를 넣어 화면에 보여준다
 */
@RestController
@Slf4j//롬복을 통해 LogggerFactory 사용없이 로그를 사용할 수 있다
public class LogTestController {

    //@Slf4j로 대체
    //private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {

        String name = "Spring";
        System.out.println("name = " + name);

        //심각도순 (낮은 순서부터)
        //트레이싱이 필요할시
        log.trace("trace log={}", name);
        //디버깅시
        log.debug("debug log={}", name);
        //정보
        log.info(" info log={}", name);
        //경고
        log.warn(" warn log={}", name);
        //에러
        log.error("error log={}", name);

        //밑의 방법은 로그 레벨에 걸려 출력을 하지 않더라도
        //String 연산이 먼저 일어나기 때문에 쓸모없는 리소스 발생이 일어난다 (밑의 방식 사용x)
        //log.trace("trace log=" + name);

        return "ok";
    }
}
