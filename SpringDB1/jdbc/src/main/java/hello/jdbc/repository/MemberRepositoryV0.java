package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

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

    //Statement 는 PrepareStatement 의 부모이다
    private void close(Connection con, Statement stmt, ResultSet rs) {
        //사용 역순으로 닫아줘야한다
        //close() 도중 Exception 이 발생하면 다음 객체의 close() 를 할 수 없기 때문에
        //각각을 try-catch 문으로 감싸줘야 한다
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
