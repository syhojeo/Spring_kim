package hello.jdbc.domain;

import lombok.Data;

@Data
public class Member {

    //회원 ID와 소지하고 있는 금액
    private String memberId;
    private int money;

    public Member() {
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }

}
