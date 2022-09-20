package hello.core.lifecycle;

//Bean의  initMethod = "", destroyMethod = "" 만드는 것의 장점
//1. 메서드 이름을 자유롭게 줄 수 있다
//2. 스프링 빈이 스프링 코드에 의존하지 않는다
//3. 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화 종료 메서드를 적용할 수 있다 !!
public class NetworkClient{

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

    //@Bean(init = "" , destroy = "") 를 사용 하기 위해 init, destroy 메서드만들기

    //객체 생성완료 후 실행
    public void init() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    //객체 소멸완료 후 실행
    public void close() throws Exception {
        disconnect();
    }
}
