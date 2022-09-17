package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//하나의 빈이 여러타입을 가지고 있을때 방법2
//@Qualifier를 통해 타입이름 명시하기 (빈 이름이 변경되는것은 아니다)
//@Qualifier("mainDiscountPolicy")

@Primary //하나의 빈이 여러타입을 가지고 있을때 방법3
public class RateDiscountPolicy implements DiscountPolicy {

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if (member.getGradle() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
