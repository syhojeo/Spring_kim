package hello.itemservice.message;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode() {
        //message가 없기 때문에 NoSuchMessageException 예외가 발생함
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null)).
            isInstanceOf(NoSuchMessageException.class);
    }

    //default 메시지 사용하기 (메시지를 못찾을 경우 사용되는 default message)
    @Test
    void notFountMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    //argument 파라미터로 기존 메시지에서 메시지 추가하기
    //message : 안녕 {0} => 0번째 인덱스(Spring)이 추가됨
    //만약 message 에 파라미터가 있는데 arg를 안넣어주면 {0} 파라미터가 그대로 출력된다
    //ex) message: 안녕 {0} {1} arg: Spring => "안녕 Spring {1}" 이 리턴된다
    @Test
    void argumentMessage() {
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(message).isEqualTo("안녕 Spring");
    }

    @Test
    void defaultLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    //국제화 테스트
    @Test
    void enLang() {
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
