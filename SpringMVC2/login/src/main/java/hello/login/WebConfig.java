package hello.login;

import hello.login.web.filter.LogFilter;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    /*
        FilterRegistrationBean
        스프링 부트로 사용시 필터 등록 (스프링 부트가 WAS를 들고 띄우기 때문에 필터를 등록해준다)
     */
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        //등록할 필터
        filterFilterRegistrationBean.setFilter(new LogFilter());
        //필터의 순서 (낮을 수록 먼저 동작)
        filterFilterRegistrationBean.setOrder(1);
        //적용할 URL 패턴
        filterFilterRegistrationBean.addUrlPatterns("/*");

        //logback mdc : 같은 요청에 모두 같은 식별자 로그를 남기는 방법
        return filterFilterRegistrationBean;
    }
}
