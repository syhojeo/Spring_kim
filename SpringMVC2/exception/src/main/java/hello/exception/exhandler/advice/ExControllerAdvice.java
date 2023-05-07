package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
    @ControllerAdvice를 통해 @ExceptionHandler를 한번에 관리한다 (예외를 공통으로 처리하기!)

    원하는 타겟 지정방법
    // 컨트롤러 종류별 예외 지정 방법
    @ControllerAdvice(annotations = RestController.class)
    public class ExampleAdvice1 {}

    // 컨트롤러의 패키지별 예외 지정 방법 (해당 패키지의 하위 패키지까지 모두 적용)
    @ControllerAdvice("org.example.controllers")
    public class ExampleAdvice2 {}

    // 컨트롤러의 클래스별 예외 지정 방법
    @ControllerAdvice(assignableTypes = {ControllerInterface.class,
    AbstractController.class})
 */
@Slf4j
//@RestControllerAdvice = @ControllerAdvice + @ResponseBody
@RestControllerAdvice // 아무설정하지 않으면 모든 Controller에 적용
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illlegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
