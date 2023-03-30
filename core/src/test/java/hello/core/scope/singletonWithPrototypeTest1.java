package hello.core.scope;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
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
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBean {

        /*
            Provider: Bean에서 원하는 객체를 수동적으로 찾아온다 (Dependency Lookup)

            Provider를 이용하여 싱글톤 안의 프로토타입이 매번 새로운 객체를 받을 수 있도록 만들어준다
            스프링 컨테이너로부터 DL (dependency Lookup) 기능만 사용하여 필요한 객체를 받아온다(주입의 반대개념)
            Provider는 DL의 기능을 하며 프로토타입이기 때문에 객체를 받아오는게 아니라 원래 객체를 받아오는 (DL)의
            기능을 수행한다 -> 프로토타입에서만 쓰이는 것이 아니라 DL기능을 쓰기 위해 사용된것

            Provider를 사용하지 않는다면 스프링 컨테이너를 해당 클래스내에서 불러와서 프로토타입을 가져와야하지만
            스프링 컨테이너의 방대한 기능을 사용하지 않고 DL의 기능만 사용하고 싶을때 Provider를 사용한다
         */

        /*
            //provider를 사용하지 않을 경우 스프링 컨테이너를 사용해야하고
            //이럴 경우 스프링 컨테이너에 종속적인 코드가되며 단위테스트도 하기 어려운 코드로 변한다

            @Autowired
            private ApplicationContext ac;

            public int logic() {
            //PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
         */

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            //getObject(JSR inject Provider사용시 .get()사용)를 통해 프로토타입 빈이 생성된다
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
        /*
            Spring의 Provider vs JSR-330 (자바표준)의 Provider
            Spring에서 제공하는 Provider는 DL 기능 외에도 여러가지 추가 기능들을 지원해준다
            JSR-330의 Provider는 DL기능만 지원하지만 스프링 컨테이너가 아닌 다른 컨테이너에서도 사용가능하다
            하지만 JSR-330의 경우 라이브러리를 추가해줘야한다

            따라서 선택의 문제이지만 여러가지 기능과 라이브러리를 추가하고 싶지 않을 경우 Spring에서 제공하는 기능을
            사용하고 단순한게 좋거나 다른 컨테이너에서 사용해야한다면 JSR-330 에서 제공하는 기능을 사용하는것이 좋다
            (둘중 기능이 겹치는 경우 많이 발생하니 잘 생각해서 선택할것)
         */
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
