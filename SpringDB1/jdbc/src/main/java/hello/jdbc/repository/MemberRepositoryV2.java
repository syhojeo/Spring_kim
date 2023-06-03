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
import org.springframework.jdbc.support.JdbcUtils;

/**
 * JDBC - ConnectionParam (트랜잭션을 위해 하나의 커넥션만 사용하기)
 *  - 기존 코드에서 findById, update 을 오버로딩한 메서드에서 사용
 */
@Slf4j
public class MemberRepositoryV2 {

    /*
        DataSource는 외부에서 주입 받아서 사용한다
        외부에서 DataSource를 설정할때
        1. connection pool 방식을 사용할지 DriverManager 방식을 사용할지 정한다
        2. 추가로 필요한 파라미터 (URL, Username, Password)를 세팅한다
     */
    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource) {
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

    //Connection을 파라미터로 받아 하나의 Connection 만 사용하기 (findById 오버로딩)
    public Member findById(Connection con, String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        //Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //con = getConnection();
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
            //connection은 여기서 닫지 않는다 (하나의 커넥션을 계속 사용해서 써야하기 때문)
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
            //JdbcUtils.closeConnection(con);
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

    //Connection을 파라미터로 받아 하나의 Connection 만 사용하기 (update 오버로딩)
    public void update(Connection con, String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        //Connection con = null;
        PreparedStatement pstmt = null;

        try {
            //con = getConnection();
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
            //connection은 여기서 닫지 않는다 (하나의 커넥션을 계속 사용해서 써야하기 때문)
            JdbcUtils.closeStatement(pstmt);
            //JdbcUtils.closeConnection(con);
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

    private void close(Connection con, Statement stmt, ResultSet rs) {

        //JdbcUtils 사용 하여 편하게 객체를 소멸시킬 수 있다
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);

        /*if (rs != null) {
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
        }*/
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
