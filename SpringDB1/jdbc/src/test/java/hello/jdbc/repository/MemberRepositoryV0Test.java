package hello.jdbc.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hello.jdbc.domain.Member;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV2", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        //log.info("member != findMember {}", member == findMember);
        //log.info("member == findMember {}", member.equals(findMember));
        assertThat(findMember).isEqualTo(member);

        /*
            @Data - Member
            Member 객체의 Lombok의 @Data 애노테이션이 사용되어 다음과 같은 효과를 가져온다

            log.info("findMember={}", findMember); - toString 이 구현됨
            findMember를 log로 출력했을때 객체의 주소가 출력되는것이 아니라
            findMember의 멤버 변수가 순서대로 출력된다

            log.info("member != findMember {}", member == findMember); 를 찍었을때에는 객체의 주소가
            다르기 때문에 false가 출력되지만

            log.info("member == findMember {}", member.equals(findMember)); - EqualsAndHashCode 이 구현
            EqualsAndHashCode가 구현되어 있기 때문에 객체의 멤버 변수를 비교하여 true가 출력되게 해준다
            isEqualTo 에도 똑같이 적용된다

            @Data는 사용하면 Test코드를 매우 편리하게 작성할 수 있도록 해준다
         */
    }
}