package hello.exception.api;

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
public class ApiExceptionV3Controller {

    @GetMapping("/api3/members/{id}")
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
