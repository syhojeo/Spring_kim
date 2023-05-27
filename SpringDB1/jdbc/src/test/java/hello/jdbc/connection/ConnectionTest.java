package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.*;

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
