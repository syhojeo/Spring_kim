package hello.springmvc.basic;

import lombok.Data;

/*
    @Data (lombok)
    @Getter @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 자동 적용
 */
@Data
public class HelloData {

    private String username;
    private int age;
}
