package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//JPA에서 데이터를 저장하거나 변경할때에는 Transactional 이 필요하다 (JPA는 항상 Transaction 안에서 사용해야함)
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    //dependency Injection (DI)
    //내가 객체를 만드는게 아니라 외부에서 객체를 넣어준다
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*회원 가입*/
    public Long join(Member member) {

        long start = System.currentTimeMillis();

        try {
            //같은 이름이 있는 중복 회원X
            validateDuplicateMember(member); //중복회원 검증
            memberRepository.save(member);
            return member.getId();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = " + timeMs + "ms");
        }

        /*
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
        */

    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                //값이 있다면 로직 동작 (Optional 을 통해 ifPresent 사용)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /*
        전체 회원 조회
     */
    public List<Member> findMembers() {
        long start = System.currentTimeMillis();
        try {
            return memberRepository.findAll();
        }
        finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("findMembers " + timeMs + "ms");
        }

    }

    /*
        id를 통한 조회
     */
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
