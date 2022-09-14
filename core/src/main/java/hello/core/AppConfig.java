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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//JAVA 코드를 이용한 bean 설정 (FactoryBean을 이용한 빈설정방법)
//직접 Bean을 설정하는 방식은 XML을 이용한 방식이다 (appConfig.xml)

//app 환경설정 (배역에 대한 섭외는 여기서!) AppConfig = 공연기획자
//AppConfig를 통해 관심사를 분리한다!!
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    //역할 추가

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        //할인정책 변경
        //역할이 정해졌으면 배우 변경만 해주면된다
        return new RateDiscountPolicy();
    }
}
