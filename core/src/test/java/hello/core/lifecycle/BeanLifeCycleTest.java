package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean
        public NetworkClient networkClient() {
            //Step1. 객체 생성을 한후
            NetworkClient networkClient = new NetworkClient();
            //Step2. setUrl을 통해 값을 넣어준다
            networkClient.setUrl("http://hello-spring.dev");
            // 이는 스프링 빈의 생명주기와 매우 유사하다

            //스프링 빈의 이벤트 라이프사이클
            //스프링 컨테이너 생성->스프링 빈 생성->의존관계 주입->초기화 콜백->사용->소멸전 콜백->스프링 종료
            //초기화 콜백: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
            //소멸전 콜백: 빈이 소멸되기 직전에 호출

            return networkClient;
        }
    }

}
