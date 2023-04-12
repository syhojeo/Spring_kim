package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

//@Data // 추가되는 기능이 많기 때문에 오히려 위험하다
@Getter @Setter //분리하게 쓸것
public class Item {

    private Long id;
    private String itemName;
    // Integer를 쓰는 이유 값이 안들어 갈 가능성이 있다. null로 들어올 수 도 있기 때문(price가 0인경우는 말이 안되니까)
    private Integer price;
    private Integer quantity;

    public Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
