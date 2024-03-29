package hello.itemservice.domain.item;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

@Data
//Object Error 정의 (field error 가 아닌 global error)
//권장하지 않음(이렇게 사용하면 현재 객체로 에러를 체크하는데 보통 필요한 범위가 객체 이상으로 넘어가는 경우가 많다)
/*@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000",
    message = "총합을 10000원 넘게 해주세요.")*/
public class Item {
    
    @NotNull(groups = UpdateCheck.class) // 요구사항 추가
    private Long id;

    //default message 설정도 가능
    @NotBlank(message = "공백 x", groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = SaveCheck.class) //수정 요구사항 추가
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
