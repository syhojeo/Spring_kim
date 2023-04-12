package hello.springmvc.basic.requestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
    여러가지 매핑 방법
 */

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    //url Mapping을 여러개 할 수 있다 -> 이름은 다르지만 같은 요청으로 매핑된다 (/hello-basic = /hello-go)
    //@RequestMapping({"/hello-basic", "/hello-go"})
    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    //HttpMethod 설정 기능
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    //HttpMethod를 직접 어노테이션으로 설정
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }
    /**
    * PathVariable 경로 변수 (최근에 많이 사용)
    *  * 변수명이 같으면 생략 가능
    *  * @PathVariable("userId") String userId -> @PathVariable userId
    */
    @GetMapping("/mapping/{userId}")
    //public String mappingPath(@PathVariable String userId) -> url 과 변수명을 맞추면 이렇게도 사용가능
    public String mappingPath(@PathVariable("userId") String data) {

        log.info("mappingPath userId={}", data);
        return "ok";
    }

    /**
     * PathVariable 사용 다중
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long
        orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 파라미터로 추가 매핑 (잘 사용x) 보통 경로변수 사용
     * 조건 설정
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    //URL 매핑을 /mapping-param?mode-debug를 해야만 매핑이 된다
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * 조건 설정
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    //위와 비슷하지만 파라미터가 아닌 헤더에 value = mode, key = debug 가 있어야한다
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }


    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    //요청 header 의 content-Type이 application/json일 경우에만 매핑 된다
    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type 기반 매핑
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    //요청 header 의 accept가 "text/html" 일 경우에만 매핑이 가능하다
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }

    /*
        Content-Type 헤더와 Accept 헤더 둘 다 데이터 타입(MIME)을 다루는 헤더이다.
        하지만  Content-Type 헤더는 현재 전송하는 데이터가 어떤 타입인지에 대한 설명을 하는 개념이고
        Accept 헤더는 클라이언트가 서버에게 어떤 특정한 데이터 타입을 보낼때
        클라이언트가 보낸 특정 데이터 타입으로만 응답을 해야한다.

        Content-Type 이 요청에 헤더에 없다면 message Body를 단순 텍스트로 읽어 들인다
     */
 }
