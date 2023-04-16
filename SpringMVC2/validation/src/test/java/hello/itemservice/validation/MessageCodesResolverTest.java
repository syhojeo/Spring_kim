package hello.itemservice.validation;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName",
            String.class);
        //String.class 가 MessageCodesResolver에 들어가는 이유는
        //String 타입으로 입력해야한다, Integer 타입으로 입력해야 한다 등 안내 메시지에 단계를 부여하기 위해서이다
        //ex) "문자를 입력하시오" , "숫자를 입력하시오",
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        assertThat(messageCodes).containsExactly("required.item.itemName",
            "required.itemName", "required.java.lang.String", "required");
    }

}
