package hello.jdbc.service;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *  트랜잭션 - 커넥션 파라미터 전달 방식 동기화 (하나의 커넥션을 사용하여 트랜잭션 사용하기)
 */
@Slf4j
class MemberServiceV2Test {

    public static final String Member_A = "memberA";
    public static final String Member_B = "memberB";
    public static final String Member_EX = "ex";

    private MemberRepositoryV2 memberRepository;
    private MemberServiceV2 memberService;

    @BeforeEach
    void before() {
        //DB연결을 위한 dataSource 생성
        //커넥션 풀을 예시로 들었기 때문에 HikariDataSource를 사용하는게 맞지만
        //빠른 테스트를 위해 DriverManagerDataSource 사용
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME,
            PASSWORD);
        //repository 객체 생성 (dataSource 주입)
        memberRepository = new MemberRepositoryV2(dataSource);
        //service 객체 생성 (repostory 주입)
        memberService = new MemberServiceV2(dataSource, memberRepository);
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
        log.info("START TX"); //트랜잭션 시작
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
        log.info("END TX"); //트랜잭션 종료
        //START TX 와 END TX 사이의 로그에서 새로운 conn을 받아오지 않는다 (하나의 커넥션만 사용)

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
        //계좌이체가 중간 실패했을때 롤백했는지 검증
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberEX.getMemberId());
        //트랜잭션을 사용하여 롤백을 했기 때문에 초기 설정 money 10000으로 돌아온걸 확인할 수 있다
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }
}