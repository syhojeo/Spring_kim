package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

//URL 요청시 에러 생성해주는 컨트롤러
@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        //id가 ex라면 런타임 에러
        if (id.equals("ex")) {
            //RuntimeException -> 500error 발생
            //예외 발생시 만들어둔 오류 페이지 500HTML 이 응답된다
            // -> HTTP API 이기 때문에 에러에 대해 다른 방식으로 응답해야한다
            // => 오류 페이지 컨트롤러도 JSON 방식으로 응답할 수 있어야한다!
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        //사용자 에러 발생 (UserException)
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        //아니라면 memberDto 반환
        return new MemberDto(id, "hello " + id);
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    /*
       ResponseStatusException 사용하는 방법
       @ResponseStatus는 어노테이션이기 때문에 라이브러리와 같은 곳에서 정의된 예외에 대해서 사용할 수 없다
       ResponseStatusException을 컨트롤러에서 사용하여 예외를 재정의 함으로써
       개발자가 원하는대로 statusCode나 에러 메시지, Exception 종류를 정할 수 있다
     */
    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2(){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad",
            new NullPointerException());
    }

    /*
        DefaultHandlerExceptionResolver (Spring에서 제공)
        스프링 내부에서 발생하는 스프링 예외를 해결한다
        밑의 핸들러의 경우 RequestParameter를 Integer로 보내지 않았을 경우 TypeMismatchException이 발생한다
        (type binding 에러)
        보통 Exception 이 발생하는 경우 StatusCode로 500을 보내게 된다
        하지만 Spring에서는 DefaultHandlerExceptionResolver를 사용해 400 에러로 설정하여 응답한다
        실제로 DefatulHandlerExceptionResolver가 정의된 곳을 확인하면 TypeMismatchException 말고도
        많은 Exception 에 대해 알맞은 StatusCode가 설정되도록 구현되어 있다
     */
    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        return "ok";
    }

    @Data
    @AllArgsConstructor //필드 개수만큼 생성자에 추가
    static class MemberDto {

        private String memberId;
        private String name;
    }
}
