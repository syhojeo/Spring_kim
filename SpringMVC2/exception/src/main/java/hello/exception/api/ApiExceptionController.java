package hello.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
