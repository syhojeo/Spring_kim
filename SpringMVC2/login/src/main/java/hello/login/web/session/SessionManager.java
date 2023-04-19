package hello.login.web.session;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * 세션 관리
 * 세션 : 서버에 중요한 정보를 보관하고 연결을 유지하는 방법
 */
@Component
public class SessionManager {

    //세션 쿠키 이름
    public static final String SESSION_COOKIE_NAME = "mySessionID";
    //세션 저장소
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 1. sessionId  생성 (임의의 추정 불가능한 랜덤 값)
     * 2. 세션 저장소에 sessionId와 보관할 값 저장
     * 3. sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {

        //session ID 생성, 값을 session에 저장
        //UUID 생성
        String sessionId = UUID.randomUUID().toString();
        //세션 저장소에 세션 id와 세션id와 메핑하여 저장할 값 value 저장
        sessionStore.put(sessionId, value);

        //쿠키 생성 -> new Cookie(쿠키 이름, 쿠키정보(여기서는 회원Id가 들어감))
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        //response에 생성한 쿠키 추가
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        //쿠키 조회, request에 우리가 원하는 Cookie 이름이 있는지 확인
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        //찾아온 쿠키의 value 값 즉, UUID로 세션 저장소 조회
        //조회 시 세션저장소에 저장된 UUID 와 메핑된 정보가 나온다 (ex 회원정보)
        //정보 리턴
        //sessionCookie.getValue() -> UUID
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            //세션 저장소의 해당 uuid 정보 삭제
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    //request에 우리가 원하는 cookieName의 쿠키가 존재하는지 확인 있다면 해당 쿠키 리턴
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        //쿠키가 하나도 없는 경우
        if (cookies == null) {
            return null;
        }
        /*for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return sessionStore.get(cookie.getValue());
            }
        }*/
        
        //필요한 쿠키이름 (cookieName)과 똑같은 쿠키를 찾고 리턴한다
        //배열을 스트림으로
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(cookieName))
            //순서와 상관없이 빨리나온것을 뽑아낸다
            .findAny()
            .orElse(null);
    }
}
