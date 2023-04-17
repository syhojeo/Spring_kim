package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

public class BeanValidationTest {

    //beanValidation 사용해보기
    @Test
    void beanValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //검증기 생성
        Validator validator = factory.getValidator();

        Item item = new Item();
        //일부로 에러 만들기
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation = " + violation.getMessage());
        }
    }
}
