package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//웹 서버 오류페이지 사용하기
//@Component //스프링에 꼭 등록해야 함
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        //404 에러발생시 명시한 페이지로 이동하라 (명시한 페이지를 호출한다 -> 해당 URL을 처리할 수 있는 컨트롤러가 필요하다)
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        //입력한 클래스의 자식 예외들도 모두 해당 페이지로 이동시켜준다 (RuntimeException.class 또는 자식타입이 타겟)
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        //에러 페이지 등록
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
