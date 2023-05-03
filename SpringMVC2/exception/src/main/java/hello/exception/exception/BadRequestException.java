package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
    @ResponseStatus (ResponseStatusExceptionResolver)
    RuntimeException은 StatusCode가 500이지만
    @ResponseStatus를 이용하여 StatusCode를 원하는 대로 바꿀 수 있다
    (+ reason을 이용한 오류 메시지 추가 가능)
 */
//사용자 정의 예외(Exception) 클래스
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
//reason을 이용항여 메시지 소스를 사용할 수 도 있다. 만약 해당 메시지 소스가 존재하지 않으면 문자 그대로 출력
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {

}
