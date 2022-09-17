package hello.core.order;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Test
    void createOrder() {
        MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();
        memoryMemberRepository.save(new Member(1L, "name", Grade.VIP));

        //1. 생성자 자동주입으로 할경우 DI 가 없는 환경에서 테스트하기 편하다 (생성자 매개변수로 원하는 인스턴스를 넣어줄 수 있기 때문이다)
        //2. final 키워드를 사용할 수 있다
        // (생성자에서만 값을 넣을 수 있다 (불변) + 실수로 생성자를 적지 않았을때 누락을 막을 수 있다)
        OrderServiceImpl orderService = new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());

        //수정자 자동주입 사용시
        //하지만 setter가 존재하는것 자체가 실수로 변경될 위험이 있기 때문에 생성자 자동주입을 사용하는것을 권장한다
//        OrderServiceImpl orderService = new OrderServiceImpl();
//        orderService.setDiscountPolicy(new FixDiscountPolicy());
//        orderService.setMemberRepository(new MemoryMemberRepository());

        //필드 자동주입은 아예 넣어주는 방법 자체가 존재 하지 않는다 (setter 함수를 만들어 주지 않는이상)
        //때문에 필드 자동 주입은 DI 컨테이너가 없는 환경에서는 테스트가 불가능하기 때문에 사용하지않는것을 권장한다

        Order order = orderService.createOrder(1L, "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);


    }
}