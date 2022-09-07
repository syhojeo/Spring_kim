package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    //매번 테스트가 끝날때마다 Map을 clear 할 수 있도록 해준다
    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }
            
    @Test
    void 회원가입() {
        //given
        //Step1. 테스트할 member 만들기
        Member member = new Member();
        member.setName("hello");

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
        member1.setName("hello");

        Member member2 = new Member();
        member2.setName("hello");
        
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
        //then
    }

    @Test
    void findOne() {
    }
}