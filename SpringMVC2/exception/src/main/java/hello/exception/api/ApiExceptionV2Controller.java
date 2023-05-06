package hello.exception.api;

import hello.exception.api.ApiExceptionController.MemberDto;
import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /*
        @ExceptionHandler - ExceptionHandlerExceptionResolver (Spring의 ExceptionResolver 중 하나)
        ExceptionHandlerExceptionResolver 는 Spring의 ExceptionResolver 중 우선순위가 가장 높다
        @ExceptionHandler 어노테이션을 원하는 Controller에 사용하여 원하는 Exception 의 처리에 대해 설정할 수 있다
        ExceptionHandlerExceptionResolver는 HandlerExceptionResolver 보다 우선순위가 높고
        편리하기 때문에 이를 대체하여 사용한다

        ExceptionResolver는 Exception이 발생하지 않은것 처럼 정상 동작화 하기 때문에 예외가 발생하지 않은 상태로
        ErrorResult 객체를 리턴 하여 HTTP Message Converter 를 통해 JSON 형식으로 변환 후
        Response의 MessageBody 에 싣어 응답한다

        ExceptionResolver를 통해 예외 발생을 WAS 에 전달하지 않았기 때문에 HttpStatusCode는 200이다
        때문에 이를 원하는 StatusCode로 변환하기 위해
        @ResponseStatus를 이용하여 StatusCode를 변환해준다 -> @ResponseStatus(HttpStatus.BAD_REQUEST)


        HttpMessageConverter
        HttpMessageConverter는 HandlerAdapter의 ArgumentResolver 뿐 아니라
        ExceptionHandlerExceptionResolver에서도 사용된다.
        이 경우 ResponseBody(RestController) 혹은 ResponseEntity를 사용해야 HttpMessageConverter가 동작한다

        단, 기존의 HandlerExceptionResolver 에서는 HttpMessageConverter를 지원하지 않기 때문에
        ObjectMapper를 사용하여 JSON 변환을 직접 해줬다
     */

    //ResponseBody를 이용하여 리턴할때
    /*
        동작원리
        컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러 밖으로 던져진다
        예외가 발생하였으므로 ExceptionResolver가 작동하며 우선순위가 가장 높은 ExceptionHandlerExceptionResolver가 실행된다
        ExceptionHnadlerExceptionResolver는 해당 컨트롤러에 IllegalArgumentException을 처리할 수 있는 @Exceptionhandler가 있는지 확인한다
        illegalExHandler가 실행되고 @RestController이므로 IllegalExHandler()  에도 @ResponseBody가 적용된다
        @ResponseBody가 적용되었기 때문에 HTTP Message Converter가 사용되고 JSON으로 반환된다
        @ResponseStatus(HttpStatus.BAD_REQUEST)를 지정했기 때문에 HTTP 상태코드 400으로 응답한다
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illlegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    //ResponseEntity를 이용하여 리턴할때
    //ExceptionHandelr(UserException.class) 매개 변수 정의로 인해 소괄호 생략 가능
    /*
        위와 다른점은 ResponseEntity를 사용할 경우 프로그래밍을 통해 동적으로 HttpStatus값을 지정할 수 있다
        @ResponseStatus는 에노테이션이므로 HTTP 응답코드를 적으면 끝이기 때문에 동적으로 변경할 수 없다
    */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    //Exception 을 Exception 으로 설정해서 놓친 Exception에 대한 공통 처리를 한다
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        //id가 ex라면 런타임 에러
        if (id.equals("ex")) {
            //RuntimeException -> 500error 발생
            throw new RuntimeException("잘못된 사용자");
        }
        //error 400 발생
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

    @Data
    @AllArgsConstructor //필드 개수만큼 생성자에 추가
    static class MemberDto {

        private String memberId;
        private String name;
    }
}
