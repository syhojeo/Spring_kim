package hello.servlet.domain.member;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    회원관리 객체 만들기

    밑의 예제는 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려할것
 */
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    //싱글톤 패턴
    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance() {
        return instance;
    }

    private MemberRepository() {

    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        //스토어의 모든 값을 ArrayList에 넣어서 리턴
        //스토어 자체값을 보호하기 위해 List에 넣어서 사용
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
