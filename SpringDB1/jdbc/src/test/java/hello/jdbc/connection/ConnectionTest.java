package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.*;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
public class ConnectionTest {

    //DriverManager를 이용한 커넥션 받아오기
    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("conneciton={}, class={}", con1, con1.getClass());
        log.info("conneciton={}, class={}", con2, con2.getClass());
    }

    //DataSource(HikariCP - Connection Pool)를 이용하여 커넥션 가져오기
    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10); //기본이 10
        dataSource.setPoolName("MyPool"); //connection pool 이름 지정

        useDataSource(dataSource);
        /*
           풀에서 커넥션을 생성하는 것은 현재 메서드를 실행하는 스레드와 다른 별도의 스레드에서 동작하기 때문에
           커넥션을 생성하는 로그를 확인하기 위해서는 sleep 메서드를 사용하여
           dataSourceConnectionPool(현재 메서드) 메서드를 실행하는 스레드가 종료되지 않도록 해야 로그를 확인할 수 있다
         */

        Thread.sleep(1000);
    }

    //DataSource(DriverManager)를 이용하여 커넥션 가져오기
    @Test
    void dataSourceDriverManager() throws SQLException {
        //DriverManagerDataSource - 항상 새로운 커넥션을 획득 (DataSource를 구현한 DriverManager 구현체)
        /*
            DataSource를 사용할때에는 처음 객체를 생성할 때만 필요한 파라미터(URL, USERNAME, PASSWORD)를 한번만
            넘겨주면된다 (설정과 사용의 분리)
            설정과 관련된 속성들은 한곳에 있는 것이 향후 변경에 더 유연하게 대처할 수 있다
         */
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("conneciton={}, class={}", con1, con1.getClass());
        log.info("conneciton={}, class={}", con2, con2.getClass());
    }
}
