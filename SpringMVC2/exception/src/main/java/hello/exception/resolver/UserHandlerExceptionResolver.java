package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//User 가 정의 한 Exception(UserException)을 처리하는 HandlerExceptionResolver
/*
    HandlerExceptionResolver를 이용하면
    WAS까지 가게되는 에러가 다시 BasicErrorController와 같은 예외 에러 처리 컨트롤러를 다시 호출할 일이없다
    handler 에서 예외가 발생하면
    handler -> handlerAdapter -> dispatcherServlet -> ExceptionResolver로 오게 되고
    밑의 UserHandlerExceptionResolver의 로직이 실행된다
    실행 후 ModelAndView 를 반환하여 마치 아무일이 없었다는 듯이 try-catch와 같이 예외 발생 상황을 먹어버리고
    정상적으로 클라이언트에게 Response 한다
    (ModelAndView 객체에 정보가 있는 경로가 있는 경우 view 렌더링 후 WAS에서 response)
    (ModelAndView 객체가 비어있는 경우(null x) WAS로 가서 response)
 */
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) {

        try {
            if (ex instanceof UserException) {

                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                //400 상태코드 지정
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                //에러가 생긴 요청의 요청 타입이 JSON 인 경우
                if ("application/json".equals(acceptHeader)) {
                    //JSON 정보 저장 Map
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());
                    /*
                        ObjectMapper를 통해 java객체를 JSON 으로 직렬화
                        {"name":"zooneon","age":25,"address":"seoul"}
                        writeValueAsString (자바 객체를 String JSON으로 변환 -> Map 변환시 사용)
                     */
                    String result = objectMapper.writeValueAsString(errorResult);

                    //response에 필요한 값 하나하나 세팅
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    //Body message에 변환된 JSON 데이터 저장
                    response.getWriter().write(result);
                    return new ModelAndView(); //렌더링할 뷰가 없을 경우 ModelAndView에 아무값도 넣지 않는다
                }
                // TEXT/HTML 렌더링할 ModelAndView 객체를 만들어 반환
                else {

                    // /templates/error/500.html 을 렌더링
                    return new ModelAndView("error/500");
                }
            }
        } catch (IOException e) {
            log.info("resolver ex", e);
        }

        return null;
    }
}
