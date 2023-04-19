package hello.login.web.session;

import static org.assertj.core.api.Assertions.*;

import hello.login.domain.member.Member;
import javax.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/*
    테스트용 request객체와 response 객체를 만들어주는 
    MockHttpServletRequest, MockHttpservletResponse를 사용해 세션 로직(SessionManager) 테스트를 진행
 */
public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        //HTTP Servlet을 test할 수 있도록 MockHttpServletResponse 가 도와준다
        //MockHttpServletResponse -> response 를 만들 수 있다
        MockHttpServletResponse response = new MockHttpServletResponse();

        //세션 생성 테스트 (서버에서 클라이언트로 세션을 생성해서 보내줌)
        Member member = new Member();
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장 (클라이언트에서 서버로 요청이 들어옴)
        //MockHttpServletRequest을 이용한 요청 테스트 기능
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //mySessionId = 123123-4123-123123 들어있음

        //세션 조회 테스트(요청에 대한 세션 테스트)
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션 만료 테스트(요청에 대한 세션 테스트)
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }
}
