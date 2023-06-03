package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 * 트랜잭션을 사용한 SerivceV2 구현
 * MemberRepositoryV2를 통해 Repositroy에서 하나의 Con만 사용하여 트랜잭션을 할 수 있도록 구현
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    //con을 비즈니스 로직에서 사용해야 하기 때문에 dataSource 의 주입이 필요하다
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    /*
        계좌이체하는 serivce를 구현한 클래스
        fromId -> toId로 money 보내기
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false); //트랜잭션 시작
            //트랜잭션을 관리하는 로직과 실제 비즈니스 로직을 분리하기 위해 비즈니스로직 메서드로 분리
            bizLogic(con, fromId, toId, money);
            con.commit(); //bizLogic이 문제 없이 성공할 경우 commit 을 통해 트랜잭션 종료
        } catch (Exception e) {
            con.rollback(); //실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            if (con != null) {
                try {
            /*
                커넥션 풀 고려하여 다시 AutoCommit을 자동 상태로 변경
                트랜잭션을 위해 AutoCommit을 false로 해놨기 때문에 커넥션을 커넥션 풀에 반환할때에는
                해당 설정을 다시 AutoCommit을 true로 변경해놔야 예상치 못한 에러를 막을 수 있다
             */
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception e) {
                    //exception 만을 로그로 사용할때에는 {} 사용하지 않는다
                    log.info("error", e);
                }
            }
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money)
        throws SQLException {
        //비즈니스 로직, con을 param으로 넘겨 하나의 con에서 query를 실행 -> 트랜잭션 가능하게 만들어준다
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
