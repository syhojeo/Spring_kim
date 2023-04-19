package hello.login.web.filter;

import hello.login.web.SessionConst;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

@Slf4j
public class LoginCheckFilter implements Filter {

    //로그인 안한 상태로 접근 가능한 URL 리스트 (whitelist)
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);
            //로그인을 체크해야하는가? (whitelist가 아닌경우)
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행{}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    //로그인으로 redirect
                    //현재 페이지 정보를 넘겨서 로그인에 성공하면 현재 페이지로 올 수 있도록 해준다
                    // /login 을 통해 login창으로 보내고 쿼리파라미터로 redirectURL 을 현재 URI로 넘겨서
                    // 로그인이 성공하여 권한을 얻은뒤 현재 페이지를 다시 redirect를 통해서 요청할 수 있도록 해준다
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return; //미인증 사용자는 리턴시켜서 컨트롤러 호출 자체를 막는다 (필터 기능) 중요!
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; //예외 로깅 가능하지만, 톰캣까지 예외를 보내주어야 함 (안올리면 정상인줄 안다)
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 x
     */
    private boolean isLoginCheckPath(String requestURI) {
        //PatternMatchUtils.simpleMatch를 통해 URI 패턴 비교
        //화이트 리스트에 들어있는 URI면 true, 안들어있다면 false
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
