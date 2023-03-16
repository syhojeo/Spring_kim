package hello.core.scope;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class singletonWithPrototypeTest1 {

    /*
        싱글톤이 프로토타입을 사용하게 되면 문제가 발생한다

        보통 프로토타입을 사용하는 이유는 매번 새로운 객체를 받아서 사용하고 싶어서 일것이다

        하지만 싱글톤타입에서 프로토타입을 사용하면 다음과 같이 과정이 이루어진다
        1. 스프링 컨테이너 생성
        2. 스프링 컨테이너에서 싱글톤 타입 생성
        3. 싱글톤 타입을 생성하는 도중 프로토타입 생성 및 주입
        4. 싱글톤 타입 생성완료
        5. 이후 요청에 따라 싱글톤 타입 주입
        -> 이 때 주입 받는 싱글톤은 매번 같은 싱글톤 객체일 것이고 그렇기 때문에
        매번 같은 프로토타입 객체를 가지고 있을 수 밖에 없게 된다

        결국 싱글톤안에 프로토타입 객체를 사용한다고 해서 싱글톤을 주입받을 때마다 매번 새로운 프로토타입 객체가
        싱글톤 객체안에 주입되지 않는다 (Provider와 같은 다른 방법을 사용해야한다)
     */

    @Test
    void prototypeFind() {
        //프로토타입 간단 테스트
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

       ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }

    @Scope("singleton")
    static class ClientBean {
        private final PrototypeBean prototypeBean; // 생성시점에 주입

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }
    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
