package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection() {
        try {
            /*
                class=class org.h2.jdbc.JdbcConnection
                DriverManager.getConnection()에 입력된 정보를 이용하여 각각의 데이터베이스 드라이버에
                커넥션을 요청한다

                URL: jdbc:h2:tcp://localhost/~/test 이기 때문에
                h2 데이터베이스 드라이버와의 커넥션 요청이 성공하고, 해당 커넥션을 반환한다
             */
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException();
        }
    }
}
