package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        log.info("supportsParameter 실행");

        //로그인 어노테이션이 파라미터에 있는가?
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Login.class);
        //Member 타입을 가지고 있는가? (사용하고 있는가?)
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        // => @Login 어노테이션이 Member타입에 붙어 있는가?

        //둘다 true여야 true 리턴
        //@Login 에노테이션이 있으면서 Member 타입이면 해당 ArgumentResolver가 사용된다
        return hasParameterAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        //HttpServletRequest 뽑아내기
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();
        //세션이 없는 경우 (쿠키가 없는 경우) null 리턴
        if (session == null) {
            return null;
        }

        //session 에서 Member객체를 가져와 리턴한다
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
