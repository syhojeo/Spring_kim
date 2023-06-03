package hello.jdbc.service;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import java.sql.SQLException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 기본 동작, 트랜잭션이 없어서 문제 발생
 */
class MemberServiceV1Test {

    public static final String Member_A = "memberA";
    public static final String Member_B = "memberB";
    public static final String Member_EX = "ex";

    private MemberRepositoryV1 memberRepository;
    private MemberServiceV1 memberService;

    @BeforeEach
    void before() {
        //DB연결을 위한 dataSource 생성
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME,
            PASSWORD);
        //repository 객체 생성 (dataSource 주입)
        memberRepository = new MemberRepositoryV1(dataSource);
        //service 객체 생성 (repostory 주입)
        memberService = new MemberServiceV1(memberRepository);
        // dataSource <- repository <- service 순의 의존관계
    }

    @AfterEach
    void after() throws SQLException {
        //반복 수행을 위한 저장된 DB 데이터 삭제
        memberRepository.delete(Member_A);
        memberRepository.delete(Member_B);
        memberRepository.delete(Member_EX);
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        //given
        //초기 계좌 정보 DB 저장
        Member memberA = new Member(Member_A, 10000);
        Member memberB = new Member(Member_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        //계좌이체 할때
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        //검증
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEX() throws SQLException {
        //given
        //초기 계좌 정보 DB 저장
        Member memberA = new Member(Member_A, 10000);
        Member memberEX = new Member(Member_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEX);

        //when
        //계좌 이체시 예외발생이 정상적으로 되는지 테스트
        assertThatThrownBy(
            () -> memberService.accountTransfer(memberA.getMemberId(), memberEX.getMemberId(),
                2000))
            .isInstanceOf(IllegalStateException.class);

        //then
        //계좌이체가 중간에 실패했는지 검증
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberEX.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        //validation 에서 실패했기 때문에 memberB의 돈은 -2000되지 않는다는을 검증하는 테스트
        //즉, 트랜잭션을 사용하지 않으면 중간에 예외가 발생했을때 문제가 생긴다라는것을 알 수 있다
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }
}