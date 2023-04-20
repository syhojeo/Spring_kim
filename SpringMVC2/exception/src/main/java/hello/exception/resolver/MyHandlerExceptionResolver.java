package hello.exception.resolver;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//HandlerExceptionResolver 사용하기
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    //Exception을 보고 정상적인 ModelAndView로 반환해준다
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) {

        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                //response.sendError를 이용하여
                //SC_BAD_REQUEST = 400 error로 설정한다 (Exception은 원래 500 에러 -> 400에러로 변경)
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView(); //아무 값 없어도 was 까지 정상적 리턴이된다 (예외 삼키기)
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null; //null 리턴시 예외 터진 그대로 원래대로 동작함 (기존에 발생한 예외)
    }
}
