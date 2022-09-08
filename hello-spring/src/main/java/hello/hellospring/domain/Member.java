package hello.hellospring.domain;

import javax.persistence.*;

@Entity
public class Member {
    
    @Id
    //DB 가 id를 알아서 생성하는 전략을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
