package hello.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
//테스트 코드에서 isEqualTo 했을때 객체 주소가 아닌 내부 값을 비교할 수 있도록 하여 테스트를 원활하게 진행 가능
@EqualsAndHashCode
public class IpPort {

    private String ip;
    private int port;

    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
