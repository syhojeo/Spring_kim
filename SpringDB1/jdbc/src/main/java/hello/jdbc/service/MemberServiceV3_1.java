package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_1 {

    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    /*
        계좌이체하는 serivce를 구현한 클래스
        fromId -> toId로 money 보내기
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        //트랜잭션 시작! , 파라미터는 디폴트 트랜잭션으로 Txmanager.begin() 역할
        TransactionStatus status = transactionManager.getTransaction(
            new DefaultTransactionDefinition());

        try {
            //트랜잭션을 관리하는 로직과 실제 비즈니스 로직을 분리하기 위해 비즈니스로직 메서드로 분리
            bizLogic(fromId, toId, money);
            transactionManager.commit(status); //bizLogic이 문제 없이 성공할 경우 commit 을 통해 트랜잭션 종료
        } catch (Exception e) {
            transactionManager.rollback(status); //실패시 롤백
            throw new IllegalStateException(e);
        }
    }

    private void bizLogic(String fromId, String toId, int money)
        throws SQLException {
        //비즈니스 로직, con을 param으로 넘겨 하나의 con에서 query를 실행 -> 트랜잭션 가능하게 만들어준다
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
    /*
        트랜잭션을 구현할때 발생하는 문제들을 보완하기 위해 스프링에서는 트랜잭션 매니저를 제공한다
        transactionManager.getTransaction() 을 통해 TransactionStatus를 받아오며 트랜잭션을 시작하고
        원하는 구간에서 transaction.commit(), transaction.rollback을 통해 트랜잭션을 종료 시킬 수 있다

        이를 통해 트랜잭션을 러프하게 구현할때 생기던 문제였던
        1. 서비스 계층에 JDBC 기술 누수
        2. JDBC 관련된 예외 누수 (SQLException 과 같은)
     */
}
