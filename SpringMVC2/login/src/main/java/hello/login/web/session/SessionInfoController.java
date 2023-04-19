package hello.login.web.session;

import java.util.Date;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SessionInfoController {

    //세션 정보 로그로 찍어보기
    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        //세션에 있는 Attribute 모두 로그로 출력
        session.getAttributeNames().asIterator()
            .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        //비활성화 시키는 최대 시간 (사실상 만료기간) 초단위
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        //세션생성일자
        log.info("createTime={}", new Date(session.getCreationTime()));
        //마지막에 세션에 접근한 시간
        log.info("lastAccessedtime={}", new Date(session.getLastAccessedTime()));
        //방금 나온 세션인지 사용하던 세션인지
        log.info("isNew={}", session.isNew());

        return "세션 출력";
    }
}
