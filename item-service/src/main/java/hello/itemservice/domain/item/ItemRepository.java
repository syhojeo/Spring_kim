package hello.itemservice.domain.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {
    /*
        실무에서는 여러 쓰레드에서 hashMap 에 접근하게 된다 때문에 문제가 발생
        이 경우 ConcurrentHashMap 을 사용해한다
        여러 쓰레드가 접근하게되는 실무에선 항상 thread-safe 상태인 자료구조를 사용해야한다
        (멀티쓰레드에서 synchronized 가 없도록 -> thread-safe)
     */
    private static final Map<Long, Item> store = new HashMap<>(); //static (실무에선 HashMap 쓰지말것)
    //long 도 동시에 접근하면 값이 꼬인다 그래서 atomic long 등 다른걸 사용해야한다
    private static long sequence = 0L; //static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        //리스트로 감싸서 반환 (store 한번 지키기 위함)
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {

        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear(); // store HashMap 초기화
    }
}
