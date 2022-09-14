package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

//app 환경설정 (배역에 대한 섭외는 여기서!) AppConfig = 공연기획자
//AppConfig를 통해 관심사를 분리한다!!
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    //역할 추가

    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        //할인정책 변경
        //역할이 정해졌으면 배우 변경만 해주면된다
        return new RateDiscountPolicy();
    }
}
