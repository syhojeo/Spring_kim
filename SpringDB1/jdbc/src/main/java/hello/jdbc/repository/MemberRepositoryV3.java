package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * 트랜잭션 - 트랜잭션 매니저
 * DataSourceUtils.getConnection()
 * DataSourceUtils.releaseConnection()
 */
@Slf4j
public class MemberRepositoryV3 {

    /*
        DataSource는 외부에서 주입 받아서 사용한다
        외부에서 DataSource를 설정할때
        1. connection pool 방식을 사용할지 DriverManager 방식을 사용할지 정한다
        2. 추가로 필요한 파라미터 (URL, Username, Password)를 세팅한다
     */
    private final DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        //?를 통한 파라미터 바인딩 방식 사용 -> SQL Injection 공격 예방
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            //connection 가져오기
            con = getConnection();
            //sql 을 세팅할 PrepareStatement 객체를 connection 으로부터 가져오기
            pstmt = con.prepareStatement(sql);
            //sql 세팅
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            //데이터베이스에 쿼리 실행
            //executeUpdate()는 update 한 row 수 만큼을 리턴한다
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            //로그만 남기고 예외를 밖으로 던진다
            throw e;
        } finally {
            //쿼리 실행 후 리소스 정리
            //Connection Pool 의 Close 는 커넥션을 소멸 시키지 않고, 커넥션 풀에 반환한다
            close(con, pstmt, null);
        }
    }

    //Member id를 이용하여 쿼리 조회하기
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            //sql 세팅
            pstmt.setString(1, memberId);

            //executeQuery() 를 통해 조회한 정보가 담긴 ResultSet을 받아온다
            rs = pstmt.executeQuery();
            //ResultSet은 next() 메서드를 통해 iterator 한다
            //조회할 row가 2개 이상이라면 while을 통해 iterator 하면 된다
            if (rs.next()) {
                Member member = new Member();
                //getString, getInt 등을 사용하여 해당 row 의 column을 통해 데이터를 조회할 수 있다
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            //쿼리를 실행하고 영향받은 row수를 반환
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //쿼리 실행 후 리소스 정리
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //쿼리 실행 후 리소스 정리
            close(con, pstmt, null);
        }
    }

    /*
        DataSourceUtils.getConnection()
        트랜잭션 동기화 매니저가 관리하는 connection 인지 아닌지를 확인하고
        connection 을 새로 만들지, 트랜잭션 동기화 매니저로 부터 보관하고 있는 connection을 가져올지 판단

        DataSourceUtils.releaseConnection()
        close 또한 트랜잭션 동기화 매니저가 관리하고 있지 않다면 close 동작을 수행하고, 관리하고 있다면
        트랜잭션 동기화 매너지에 반납한다
     */

    private void close(Connection con, Statement stmt, ResultSet rs) {
        //JdbcUtils 사용 하여 편하게 객체를 소멸시킬 수 있다
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다
        //connection 과 dataSource 구현체를 파라미터로 보낸다
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection() throws SQLException {
        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야한다
        //dataSourceUtils를 이용해 트랜잭션 매니저 사용
        //트랜잭션 매니저는 파라미터로 넘어온 dataSource를 이용해 connection을 생성 및 관리, connection 리턴
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}

/*
    Repository v0~v3 정리
    v0 - DriverManager를 이용한 DB 사용
    v1 - dataSource를 이용하여 DB를 접근함으로써 hikari Cp 와 같은 connection pool 사용가능 (dataSource 이용)
        dataSource를 이용하면 여러가지 connection을 받아오는 방식을 추상화하여 코드 변경없이 쉽게 connection 방식 변경 가능
        OCP 준수
    v2 - 트랜잭션 적용 하지만 트랜잭션을 적용하면 JDBC 기술이 서비스계층에 누수되고, 여러가지 반복코드가 발생하는등 문제발생
    v3 - 트랜잭션 매니저를 사용하여 트랜잭션을 사용하기 편하게 만들어준다
 */