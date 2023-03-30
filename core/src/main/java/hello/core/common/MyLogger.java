package hello.core.common;

import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
//프록시 방식 사용
/*
    프록시 -> 주입받을 클래스를 상속 받은 가짜 프록시 객체를 사용하여 실제 request가 들어올때 까지 지연시킨다
    싱글톤 처럼 동작하며 실제 요청이오면 내부에서 실제 빈을 요청하는 위임 로직이 들어 있다
    꼭 필요한곳에서만 사용할것을 권장한다
 */
@Scope(value = "request", proxyMode =  ScopedProxyMode.TARGET_CLASS) //proxyMode 설정 추가
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("["+ uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("["+ uuid + "] request scope bean create:" + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("["+ uuid + "] request scope bean close:" + this);
    }
}
