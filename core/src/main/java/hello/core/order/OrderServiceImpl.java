package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    // 할인정책을 변경하려면 OrderServiceImpl의 소스코드를 변경해야한다
    // OCP DIP 위반
    // DIP 위반: OrderServiceImpl 은 FixDiscountPolicy(), RateDiscountPolicy() 두 구현체에 의존한다
    // OCP 위반: 소스코드의 변경에 폐쇄되어야 하는데 밑의 코드와 같이 할인정책을 변경시 소스코드를 변경해줘야만 한다

    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    //해결 but, 구현체 없이는 사용 불가능하다
    //누군가 OrderServiceImpl 에 DiscountPolicy 의 구현 객체를 대신 생성하고 주입해줘야한다 (DI)
    //관심사의 분리가 필요하다!!
    //private DiscountPolicy discountPolicy; // 에러

    // AppConfig를 통해 철저하게 DIP,OCP 지킬수 있다
//    private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy;
//

    //생성자 자동주입
//    private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy;
//
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }

    //수정자 자동주입
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
