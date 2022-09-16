package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {


    @Test
    @DisplayName("싱글톤의 문제점 확인")
    void StatefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA: A사용자 10000원 주문
        int userAPrice = statefulService1.order("userA", 10000);
        //ThreadB: B사용자 20000원 주문
        int userBPrice = statefulService2.order("userB", 20000);

        //ThreadA: 사용자A 주문 금액 조회
//        int price = statefulService1.getPrice();
        System.out.println("price = " + userAPrice);

        Assertions.assertThat(userAPrice).isEqualTo(10000);

        /*
            싱글톤의 문제점
            statefulService1 과 statefulService2 는 싱글톤으로 인해 같은 인스턴스를 공유하기 때문에
            위와 같이 사용자 A가 10000원을 주문했음에도 B사용자가 20000원을 주문했기 때문에
            사용자 A의 주문금액이 20000원이 찍히는 문제가 발생한다

            StatefulService의 Price 필드는 값을 공유한다 (문제)
            때문에 공유필드를 없애고 무상태(stateless)로 설계해야한다
            stateless
            - 특정 클라이언트에 의존적인 필드가 있으면 안된다
            - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다
            - 가급적 읽기만 가능해야 한다
            - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다

            스프링 빈의 필드에 공유값을 설정하면 정말 큰 장애가 발생할 수 있다
        * */
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}