package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //Lombok 생성자 주입시 생성자 생략가능 (final 붙어있어야함)
public class BasicItemController {

    private final ItemRepository itemRepository;

    //@Autowired 생성자 하나만 있으니 생략 가능
/*    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }*/

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    /**
     * 테스트용 데이터 추가
     *
     * @PostConstruct 를 이용해 스프링이 bean에 등록하기 위해 컨트롤러를 생성할때 실행된다
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping("{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
    //같은 url (http Method로 구분)
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                        @RequestParam int price,
                        @RequestParam Integer quantity,
                        Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    /*
        @ModelAttribute
        @ModelAttribute는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXXX)으로 입력해준다

        추가 기능
        Model 에 @ModelAttribute로 지정한 객체를 자동으로 넣어준다
        따로 model 에 객체를 넣을 필요가 없다
        대신 model.addAtrribute("name", object)의 name을 @ModelAttribute("name")에서의 name과 같게 맞춰줘야한다
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {

        itemRepository.save(item);

        //model.addAttribute("item", item); 자동 추가, 생략 가능

        return "basic/item";
    }

    //@ModelAttribute의 name 생략시 클래스명의 첫글자를 소문자로 바꿔서 적용된다
    //Item -> item
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {

        itemRepository.save(item);
        return "basic/item";
    }

    //@ModelAttribute 자체를 생략 (위와 같다)
    @PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);
        return "basic/item";
    }
}
