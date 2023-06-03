package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

/*
    트랜잭션을 사용하지 않는 Service에서 발생하는 문제를 알아보기 위한 코드
    ServiceV1 에서는 잘못된 계좌이체 로직에 대해서 작성하고 ServiceV1Test를 통해 이를 확인한다
    계좌이체 할때 중간에 예외 발생시
    보낸사람의 돈은 차감되어있지만 받는 사람의 돈은 추가되어있지 않는 로직 구현
 */
@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    /*
        계좌이체하는 serivce를 구현한 클래스
        fromId -> toId로 money 보내기
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        //계좌이체 로직
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
