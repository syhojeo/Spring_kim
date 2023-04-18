package hello.login.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        /*List<Member> all = findAll();
        for (Member m : all) {
            if (m.getLoginId().equals(loginId)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();*/

        //findAll을 통해 모든 로그인 정보를 .stream() 스트림 형태로 가져온다
        //filter를 통해 필터 조건에 맞는 값을 찾고
        //findFirst가 있다면 필터조건에 맞는 값을 찾고 그뒤로는 찾지 않도록 필터링을 중지하고 리턴시킨다
        return findAll().stream()
            .filter(m -> m.getLoginId().equals(loginId))
            .findFirst();
    }

    public List<Member> findAll() {
        //store 에 있는 모든 value를 list로 변환하여 반환 (리스트로 한번 감싸서 안전성 있게 사용)
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
