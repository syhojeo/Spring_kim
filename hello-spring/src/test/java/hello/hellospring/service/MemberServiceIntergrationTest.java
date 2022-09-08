package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
//@Transactional 테스트가 끝나면 테스트하기 이전으로 DB를 롤백시켜줌 (여러번 같은 테스트해도 문제가없다)
@Transactional
public class MemberServiceIntergrationTest {

    //테스트할때는 편하게 필드 자동주입 (테스트를 다른곳에서 가져다 쓰지않기때문에 상관없음)
    @Autowired MemberService memberService;
    //인터페이스지만 Bean 에 memberRepository를 JdbcMemberRepository로 올려놨기 때문에 문제없이 실행된다 (인터페이스의 다형성을 이용)
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        //given
        //Step1. 테스트할 member 만들기
        Member member = new Member();
        member.setName("spring10");

        //when
        //Step2. 테스트하는 메서드의 리턴값 받기
        Long saveId = memberService.join(member);

        //then
        //Step3. 받아온 리턴값이 유효한지 assertThat을 이용하여 확인하기
        Member findMember = memberService.findOne(saveId).get();
        //테스트하기위해 만든 이름과 메서드를 테스트하고 나온 결과를 사용한 이름이 같은지 확인
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        //assertThrows( , )
        //'() -> memberService.join(member2)' 로직을 실행할때
        //'IllegalStateException.class' 이것이 발생해야한다
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
/*
        try {
            //같은이름으로 회원가입 (member1.getName() = member2.getName())
            memberService.join(member2);
            //에러 catch 가 안될경우 실패
            fail();
            //에러가 catch 될 경우 성공
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
*/
    }
}
