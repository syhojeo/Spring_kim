package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

//초기화 , 소멸 인터페이스 사용 (Spring에서만 사용가능)
//외부 라이브러리 같은 경우 적용 불가능
//위와 같은 단점들이 있기 때문에 사용하지 않는다
public class NetworkClient implements InitializingBean, DisposableBean {

    private  String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
        connect();
        call("초기화 연결 메시지");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    //서비스 종료시 호출
    public void disconnect() {
        System.out.println("close " + url);
    }

    //객체 생성완료 후 실행
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    //객체 소멸완료 후 실행행
   @Override
    public void destroy() throws Exception {
        disconnect();
    }
}
