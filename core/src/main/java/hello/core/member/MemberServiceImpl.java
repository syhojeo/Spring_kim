package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

//스프링 빈 이름 직접 설정 가능
@Component("memberService2")
public class MemberServiceImpl implements MemberService{

    private MemberRepository memberRepository;

    //생성자 주입

    /*
        자동주입
        Component를 사용하는 경우 의존성 주입을 해주지 못하게 된다
        
        기존의 AppConfig를 이용한 의존성 주입
        public MemberService memberService() {
            return new MemberService(MemberRepository()) <- MemberRespository 의존성 주입
        } 
        
        하지만 ComponentScan을 사용할 경우 이렇게 의존성 주입에 대한 직접 정의를 해주지 못하기 때문에
        이를 해결하기 위해서 @Component가 붙은 곳에서 직접 의존성 주입에 대한 내용이 있어야한다
        우리는 이것을 @Autowired 자동 주입을 통해서 해결한다

        자동 주입시 타입 기준으로 스프링 빈에서 찾아서 주입해준다
    */

    //생성자 자동주입
    @Autowired // = ac.getBean(MemberRepository.class)
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
