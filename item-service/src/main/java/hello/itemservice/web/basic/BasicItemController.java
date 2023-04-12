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
    //@PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);
        return "basic/item";
    }

    /*
        PRG를 이용한 문제해결 (redirect 사용)
        상품 등록 시 Post 방식으로 상품을 등록할 정보가 query String으로 들어오게 되는데
        상품을 완료하고 redirect를 하지 않고 view로 렌더링만 할경우
        렌더링된 페이지에서 새로고침을 했을떄 이전에 보냈던 요청(Post로 상품 저장 query String요청)이 다시 보내지고
        새로고침을 할때마다 이전에 등록한 정보로 계속해서 상품을 등록하게 되는 문제가 발생한다

        이를 막기 위해 PRG를(redirect) 사용하면 상품 등록 이후 새로고침을 해도 redirect url로 이동하기 때문에        PRG 문제가 해결된다
        PRG -> Post/ redirect / GET
        Post 요청을 redirect 를 이용하여 GET 요청으로 변경하여 새로고침을 해도 GET 요청이 가게 만든다
        (의도치 않은 POST를 방지할 수 있다)
     */
    @PostMapping("add")
    public String addItemV5(Item item) {

        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    /*
        redirect 사용 이유
        만약 redirect를 안하고 서블릿 상의 이동만 한다면
        http://localhost:8080/basic/items/2/edit 과 같이 edit이 남는다
        (서버에서만 이동하는것이기 때문에 url이 변경되지 않는다)

        상품 상세페이지인데 url 에 edit이 남는것 자체가 문제가 있다

        redirect를 한다면 http://localhost:8080/basic/items/2 으로 url 상 완전 처음으로 돌아가게된다
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        //{itemId}는 @PathVariable의 itemId를 표현한것 (이렇게 표현하면 변수값이 넣어진다)
        return "redirect:/basic/items/{itemId}";
    }
}
