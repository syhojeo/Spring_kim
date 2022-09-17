package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutoWiredTest {

    @Test
    void AutowiredOption() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    //수정자 주입시 주입받을 대상이 없는 경우 옵션 설정 방법 3가지
    //아무런 설정안할 경우 주입받을 대상이 없다는 오류가 발생한다 (UnsatisfiedDependencyException)
    static class TestBean {

        //required = false (주입 받을 대상이 없으면 아예 스프링 빈 등록이 되지 않는다)
        //true 가 디폴트
        @Autowired(required = false)
        public void setNoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);
        }

        //@Nullable 사용시 null 값을 넣어준다
        @Autowired
            public void setNoBean2(@Nullable Member noBean2) {
                System.out.println("noBean2 = " + noBean2);
        }

        //Optional 사용시 Optional.empty 를 넣어준다
        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);
        }
    }

}
